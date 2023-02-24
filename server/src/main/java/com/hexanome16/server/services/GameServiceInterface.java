package com.hexanome16.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface GameServiceInterface {
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
   * Allows client to see how many of each gem the game bank has.
   *
   * @param sessionId sessionId.
   * @return String representation of the Purchase map
   * @throws com.fasterxml.jackson.core.JsonProcessingException if Json processing fails
   */
  ResponseEntity<String> getGameBankInfo(long sessionId)
      throws JsonProcessingException;

  /**
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
   * @return The pair of response and a pair of game and player
   */
  Pair<ResponseEntity<String>, Game> validGame(long sessionId);
}
