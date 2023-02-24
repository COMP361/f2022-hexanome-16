package com.hexanome16.server.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.price.PurchaseMap;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
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
   * @param serviceUtils the utility used by services
   */
  public GameService(@Autowired AuthServiceInterface authService,
                     @Autowired GameManagerServiceInterface gameManagerService,
                     @Autowired ServiceUtils serviceUtils) {
    this.serviceUtils = serviceUtils;
    this.authService = authService;
    this.gameManagerService = gameManagerService;
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
