package com.hexanome16.server.services.longpolling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface LongPollingServiceInterface {
  /**
   * Long polling on update on onboard deck.
   *
   * @param sessionId   game session id
   * @param level       deck level
   * @param accessToken account access token
   * @param hash        hash used for long polling
   * @return updated game deck
   */
  DeferredResult<ResponseEntity<String>> getDeck(long sessionId, String level,
                                                 String accessToken,
                                                 String hash);

  /**
   * Returns nobles present on the game board.
   *
   * @param sessionId   session id
   * @param accessToken access token
   * @param hash        the hash
   * @return nobles present on the game board
   */
  DeferredResult<ResponseEntity<String>> getNobles(long sessionId,
                                                   String accessToken,
                                                   String hash);

  /**
   * Return the username of current player.
   *
   * @param sessionId   game id
   * @param accessToken player access token
   * @param hash        hash for long polling
   * @return current player username
   */
  DeferredResult<ResponseEntity<String>> getCurrentPlayer(long sessionId,
                                                          String accessToken,
                                                          String hash);

  /**
   * Return the winners of the game.
   *
   * @param sessionId   game id
   * @param accessToken player access token
   * @param hash        hash for long polling
   * @return game winners
   */
  DeferredResult<ResponseEntity<String>> getWinners(long sessionId,
                                                    String accessToken,
                                                    String hash);

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
  DeferredResult<ResponseEntity<String>> validRequestLongPolling(long sessionId,
                                                                 String authToken,
                                                                 String key, String hash);
}
