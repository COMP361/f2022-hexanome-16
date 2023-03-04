package com.hexanome16.server.services.longpolling;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.broadcastmap.BroadcastMapKey;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Service for long polling requests.
 */
@Service
public class LongPollingService implements LongPollingServiceInterface {
  private final GameManagerServiceInterface gameManagerService;
  private final AuthServiceInterface authService;

  /**
   * Instantiates a new Long polling service.
   *
   * @param gameManagerService game manager service
   * @param authService        auth service
   */
  public LongPollingService(@Autowired GameManagerServiceInterface gameManagerService,
                            @Autowired AuthServiceInterface authService) {
    this.gameManagerService = gameManagerService;
    this.authService = authService;
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getDeck(long sessionId, String level,
                                                        String accessToken, String hash) {
    Level atLevel = Level.fromString(level);
    if (atLevel == null) {
      return CustomResponseFactory.getDeferredErrorResponse(CustomHttpResponses.BAD_LEVEL_INFO);
    }
    return validRequestLongPolling(sessionId, accessToken,
        BroadcastMapKey.fromLevel(atLevel), hash);
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getNobles(long sessionId, String accessToken,
                                                          String hash) {
    return validRequestLongPolling(sessionId, accessToken, BroadcastMapKey.NOBLES, hash);
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getCurrentPlayer(long sessionId, String accessToken,
                                                                 String hash) {
    return validRequestLongPolling(sessionId, accessToken, BroadcastMapKey.PLAYERS, hash);
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getWinners(long sessionId, String accessToken,
                                                           String hash) {
    return validRequestLongPolling(sessionId, accessToken, BroadcastMapKey.WINNERS, hash);
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
  @Override
  public DeferredResult<ResponseEntity<String>> validRequestLongPolling(long sessionId,
                                                                        String authToken,
                                                                        BroadcastMapKey key,
                                                                        String hash) {
    final Game currentGame = gameManagerService.getGame(sessionId);

    if (currentGame == null) {
      return CustomResponseFactory.getDeferredErrorResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }

    boolean isValidPlayer = authService.verifyPlayer(authToken, currentGame);

    if (!isValidPlayer) {
      return CustomResponseFactory.getDeferredErrorResponse(
          CustomHttpResponses.INVALID_ACCESS_TOKEN);
    }
    return ResponseGenerator.getHashBasedUpdate(
        10000,
        currentGame.getBroadcastContentManagerMap().getManager(key),
        hash
    );
  }
}
