package com.hexanome16.server.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.PlayerJson;
import com.hexanome16.server.dto.WinJson;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.price.PriceInterface;
import com.hexanome16.server.models.price.PurchaseMap;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import java.util.Arrays;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Service is responsible for managing game state requests from
 * the {@link com.hexanome16.server.controllers.GameController}.
 */
@Service
public class GameService implements GameServiceInterface {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AuthServiceInterface authService;
  private final GameManagerServiceInterface gameManagerService;

  private final ServiceUtils serviceUtils;

  /**
   * Instantiates the game service.
   *
   * @param authService        the authentication service used to validate requests
   * @param gameManagerService the game manager service used to find games
   */
  public GameService(@Autowired AuthServiceInterface authService,
                     @Autowired GameManagerServiceInterface gameManagerService,
                     @Autowired ServiceUtils serviceUtils) {
    this.authService = authService;
    this.gameManagerService = gameManagerService;
    this.serviceUtils = serviceUtils;
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getDeck(long sessionId, String level,
                                                        String accessToken, String hash) {
    Level atLevel = switch (level) {
      case "THREE" -> Level.THREE;
      case "TWO" -> Level.TWO;
      case "ONE" -> Level.ONE;
      // TODO: add red decks
      default -> null;
    };

    if (atLevel == null) {
      return CustomResponseFactory.getDeferredErrorResponse(CustomHttpResponses.BAD_LEVEL_INFO);
    }
    return validRequestLongPolling(sessionId, accessToken, level, hash);
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getNobles(long sessionId, String accessToken,
                                                          String hash) {
    return validRequestLongPolling(sessionId, accessToken, "noble", hash);
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getCurrentPlayer(long sessionId, String accessToken,
                                                                 String hash) {
    return validRequestLongPolling(sessionId, accessToken, "player", hash);
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getWinners(long sessionId, String accessToken,
                                                           String hash) {
    return validRequestLongPolling(sessionId, accessToken, "winners", hash);
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

  @Override
  public Player findPlayerByName(@NonNull Game game, String username) {
    for (Player e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }

  @Override
  public Player findPlayerByToken(@NonNull Game game, String accessToken) {
    ResponseEntity<String> usernameEntity = authService.getPlayer(accessToken);

    String username = usernameEntity.getBody();


    for (Player e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }

  @Override
  public Pair<ResponseEntity<String>, Pair<Game, Player>> validRequestAndCurrentTurn(
      long sessionId,
      String authToken) {

    var response = validRequest(sessionId, authToken);
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

  @Override
  public Pair<ResponseEntity<String>, Pair<Game, Player>> validRequest(long sessionId,
                                                                       String authToken) {
    final Game currentGame = gameManagerService.getGame(sessionId);

    if (currentGame == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID),
          new ImmutablePair<>(null, null));
    }

    boolean isValidPlayer = authService.verifyPlayer(authToken, currentGame);
    Player requestingPlayer = findPlayerByToken(currentGame, authToken);

    if (!isValidPlayer || requestingPlayer == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_ACCESS_TOKEN),
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
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID), null);
    }
    return new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK), currentGame);
  }

  /**
   * /**
   * Returns HTTPS_OK if game with sessionId exists,
   * Returns HTTPS_BAD_REQUEST otherwise.
   *
   * <p>
   * Returns a pair of ResponseEntity and Game.
   * If the request wasn't valid,
   * the ResponseEntity will have an error code and the game will be null,
   * If the request was valid,
   * the ResponseEntity will have a success code and the game will be populated.
   * </p>
   *
   * @param sessionId game's identification number.
   * @param authToken authentication token of player accessing resource.
   * @param key       broadcast map key from which to retrieve content.
   * @param hash      hash to put in hashBasedUpdate.
   * @return The pair of response and a pair of game and player
   */
  private DeferredResult<ResponseEntity<String>> validRequestLongPolling(long sessionId,
                                                                        String authToken,
                                                                        String key, String hash) {
    final Game currentGame = gameManagerService.getGame(sessionId);

    if (currentGame == null) {
      return CustomResponseFactory.getDeferredErrorResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }

    boolean isValidPlayer = authService.verifyPlayer(authToken, currentGame);

    if (!isValidPlayer) {
      return CustomResponseFactory.getDeferredErrorResponse(
          CustomHttpResponses.INVALID_ACCESS_TOKEN);
    }
    return ResponseGenerator.getHashBasedUpdate(10000,
        currentGame.getBroadcastContentManagerMap().get(key), hash);
  }

}
