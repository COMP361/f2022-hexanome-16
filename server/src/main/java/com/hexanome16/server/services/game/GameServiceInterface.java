package com.hexanome16.server.services.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerPlayer;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;

/**
 * Interface for managing game state backend requests.
 */
public interface GameServiceInterface {
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

  /**
   * Gets all the cards of level two on the board.
   *
   * @param sessionId session Id of the game of interest.
   * @return Response entity with the list as an array.
   * @throws JsonProcessingException throws an exception if fail to Json convert.
   */
  ResponseEntity<String> getLevelTwoOnBoard(long sessionId) throws JsonProcessingException;
}
