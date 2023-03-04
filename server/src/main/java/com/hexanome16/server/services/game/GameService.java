package com.hexanome16.server.services.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.dto.PlayerJson;
import com.hexanome16.common.dto.WinJson;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
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
 * Service is responsible for managing game state requests from
 * the {@link com.hexanome16.server.controllers.GameController}.
 */
@Service
public class GameService implements GameServiceInterface {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AuthServiceInterface authService;
  private final GameManagerServiceInterface gameManagerService;

  /**
   * Instantiates the game service.
   *
   * @param authService        the authentication service used to validate requests
   * @param gameManagerService the game manager service used to find games
   */
  public GameService(@Autowired AuthServiceInterface authService,
                     @Autowired GameManagerServiceInterface gameManagerService) {
    this.authService = authService;
    this.gameManagerService = gameManagerService;
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  }

  @Override
  public ResponseEntity<String> getGameBankInfo(long sessionId) throws JsonProcessingException {
    var request = validGame(sessionId);
    ResponseEntity<String> left = request.getLeft();
    if (!left.getStatusCode().is2xxSuccessful()) {
      return left;
    }
    Game game = request.getRight();

    PurchaseMap gameBankMap = game.getGameBank().toPurchaseMap();


    return new ResponseEntity<>(objectMapper.writeValueAsString(gameBankMap.getPriceMap()),
        HttpStatus.OK);
  }

  @Override
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
          new PlayerJson(game.getCurrentPlayer().getName()));
  }

  @Override
  public ServerPlayer findPlayerByName(@NonNull Game game, String username) {
    for (ServerPlayer e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }

  @Override
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

  @Override
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

  @Override
  public Pair<ResponseEntity<String>, Pair<Game, ServerPlayer>> validRequest(long sessionId,
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

  @Override
  public Pair<ResponseEntity<String>, Game> validGame(long sessionId) {
    final Game currentGame = gameManagerService.getGame(sessionId);
    if (currentGame == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID), null);
    }
    return new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK), currentGame);
  }

}
