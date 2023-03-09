package com.hexanome16.server.util;

import com.hexanome16.common.dto.PlayerJson;
import com.hexanome16.common.dto.PlayerListJson;
import com.hexanome16.common.dto.WinJson;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.broadcastmap.BroadcastMapKey;
import java.util.Arrays;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Utility class for service.
 */
@Service
public class ServiceUtils {
  private final GameManagerServiceInterface gameManagerService;
  private final AuthServiceInterface authService;

  /**
   * Instantiates a new Service utils.
   *
   * @param gameManagerServiceInterface the game manager service interface
   * @param authServiceInterface        the auth service interface
   */
  public ServiceUtils(@Autowired GameManagerServiceInterface gameManagerServiceInterface,
                      @Autowired AuthServiceInterface authServiceInterface) {
    this.gameManagerService = gameManagerServiceInterface;
    this.authService = authServiceInterface;
  }

  /**
   * Returns HTTPS_OK if game with sessionId exists, if the authToken can be verified,
   * if such an id gives is owned by a real player and if it is that player's turn.
   * Returns HTTPS_BAD_REQUEST otherwise.
   *
   * <p>
   * Returns a pair of ResponseEntity and a pair of Game and Player.
   * If the request wasn't valid,
   * the ResponseEntity will have an error code and the game and player will be null,
   * If the request was valid,
   * the ResponseEntity will have a success code and the game and player will be populated.
   * </p>
   *
   * @param sessionId          game's identification number.
   * @param authToken          access token.
   * @return The pair of response and a pair of game and player
   */
  public Pair<ResponseEntity<String>, Pair<Game, ServerPlayer>> validRequestAndCurrentTurn(
      long sessionId,
      String authToken) {

    var response = validRequest(sessionId, authToken);
    if (!response.getLeft().getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game currentGame = response.getRight().getLeft();
    final ServerPlayer requestingPlayer = response.getRight().getRight();

    if (currentGame.isNotPlayersTurn(requestingPlayer)) {
      return new ImmutablePair<>(
          CustomResponseFactory.getResponse(CustomHttpResponses.NOT_PLAYERS_TURN),
          new ImmutablePair<>(null, null));
    }

    return new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK),
        new ImmutablePair<>(currentGame, requestingPlayer));
  }

  /**
   * Returns HTTPS_OK if game with sessionId exists, if the authToken can be verified,
   * if such an id gives is owned by a real player.
   * Returns HTTPS_BAD_REQUEST otherwise.
   *
   * <p>
   * Returns a pair of ResponseEntity and a pair of Game and Player.
   * If the request wasn't valid,
   * the ResponseEntity will have an error code and the game and player will be null,
   * If the request was valid,
   * the ResponseEntity will have a success code and the game and player will be populated.
   * </p>
   *
   * @param sessionId          game's identification number.
   * @param authToken          access token.
   * @return The pair of response and a pair of game and player
   */
  public Pair<ResponseEntity<String>, Pair<Game, ServerPlayer>> validRequest(
      long sessionId,
      String authToken) {
    final Game currentGame = gameManagerService.getGame(sessionId);

    if (currentGame == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID),
          new ImmutablePair<>(null, null));
    }

    boolean isValidPlayer = authService.verifyPlayer(authToken, currentGame);
    ServerPlayer requestingPlayer = findPlayerByToken(currentGame, authToken);

    if (!isValidPlayer || requestingPlayer == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_ACCESS_TOKEN),
          new ImmutablePair<>(null, null));
    }

    return new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK),
        new ImmutablePair<>(currentGame, requestingPlayer));
  }

  /**
   * Finds player with that authentication token in the game.
   *
   * @param game        game to search.
   * @param accessToken token associated to player
   * @return player with that token, null if no such player
   */
  public ServerPlayer findPlayerByToken(@NonNull Game game, String accessToken) {
    ResponseEntity<String> usernameEntity = authService.getPlayer(accessToken);

    String username = usernameEntity.getBody();


    for (ServerPlayer e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }

  /**
   * Ends current player's turn and starts next player's turn.
   *
   * @param game the game the player is in
   */
  public void endCurrentPlayersTurn(Game game) {
    game.goToNextPlayer();
    int nextPlayerIndex = game.getCurrentPlayerIndex();
    if (nextPlayerIndex == 0) {
      ServerPlayer[] winners = WinCondition.getWinners(game.getWinConditions(), game.getPlayers());
      if (winners.length > 0) {
        game.getBroadcastContentManagerMap().updateValue(
            BroadcastMapKey.WINNERS,
            new WinJson(Arrays.stream(winners).map(ServerPlayer::getName).toArray(String[]::new))
        );
      }
    }
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.PLAYERS,
        new PlayerListJson(Arrays.stream(game.getPlayers()).map(player -> new PlayerJson(
            player.getName(), !game.isNotPlayersTurn(player), player.getInventory()
            .getPrestigePoints())).toArray(PlayerJson[]::new))
    );
  }

  /**
   * Finds a player in a game given their username.
   *
   * @param game     game where player is supposed to be.
   * @param username name of player.
   * @return Player with that username in that game, null if no such player.
   */
  public ServerPlayer findPlayerByName(@NonNull Game game, String username) {
    for (ServerPlayer e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }
}
