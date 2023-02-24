package com.hexanome16.server.util;

import com.hexanome16.server.dto.PlayerJson;
import com.hexanome16.server.dto.WinJson;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.services.GameManagerServiceInterface;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.util.Arrays;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtils {
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
   * @param gameManagerService
   * @param authService
   * @return The pair of response and a pair of game and player
   */
  public Pair<ResponseEntity<String>, Pair<Game, Player>> validRequestAndCurrentTurn(
      long sessionId,
      String authToken,
      GameManagerServiceInterface gameManagerService,
      AuthServiceInterface authService) {

    var response = validRequest(sessionId, authToken, gameManagerService, authService);
    if (!response.getLeft().getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game currentGame = response.getRight().getLeft();
    final Player requestingPlayer = response.getRight().getRight();

    if (currentGame.isNotPlayersTurn(requestingPlayer)) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.NOT_PLAYERS_TURN),
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
   * @param sessionId game's identification number.
   * @param authToken access token.
   * @return The pair of response and a pair of game and player
   */
  public Pair<ResponseEntity<String>, Pair<Game, Player>> validRequest(long sessionId,
                                                                          String authToken,
                                                                          GameManagerServiceInterface gameManagerService,
                                                                          AuthServiceInterface authService) {
    final Game currentGame = gameManagerService.getGame(sessionId);

    if (currentGame == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID),
          new ImmutablePair<>(null, null));
    }

    boolean isValidPlayer = authService.verifyPlayer(authToken, currentGame);

    Player requestingPlayer = findPlayerByToken(currentGame, authToken, authService);

    if (!isValidPlayer || requestingPlayer == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_ACCESS_TOKEN),
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
   * @param authService
   * @return player with that token, null if no such player
   */
  public Player findPlayerByToken(@NonNull Game game, String accessToken, AuthServiceInterface authService) {
    ResponseEntity<String> usernameEntity = authService.getPlayer(accessToken);

    String username = usernameEntity.getBody();


    for (Player e : game.getPlayers()) {
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
      Player[] winners = game.getWinCondition().isGameWon(game);
      if (winners.length > 0) {
        BroadcastContentManager<WinJson> broadcastContentManagerWinners =
            (BroadcastContentManager<WinJson>) game.getBroadcastContentManagerMap().get("winners");
        broadcastContentManagerWinners.updateBroadcastContent(
            new WinJson(Arrays.stream(winners).map(Player::getName).toArray(String[]::new)));
      }
    } else {
      BroadcastContentManager<PlayerJson> broadcastContentManagerPlayer =
          (BroadcastContentManager<PlayerJson>) game.getBroadcastContentManagerMap().get("player");
      broadcastContentManagerPlayer.updateBroadcastContent(
          new PlayerJson(game.getCurrentPlayer().getName()));
    }
  }
}
