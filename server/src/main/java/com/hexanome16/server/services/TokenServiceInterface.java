package com.hexanome16.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

/**
 * Interface for managing in-game tokens backend requests.
 */
public interface TokenServiceInterface {

  /**
   * Gets all the token types one can take 2 of in a given game with sessionId
   * identification number.
   *
   * @param sessionId the session's Identification number.
   * @return String representation of a list of all the available token types
   */
  ResponseEntity<String> availableTwoTokensType(long sessionId) throws JsonProcessingException;

  /**
   * Gets all the token types one can take 3 of in a given game with sessionId
   * identification number.
   *
   * @param sessionId the session's Identification number.
   * @return String representation of a list of all the available token types
   */
  ResponseEntity<String> availableThreeTokensType(long sessionId) throws JsonProcessingException;

  /**
   * Allows to take 2 tokens of a given type.
   *
   * @param sessionId the session's Identification number.
   * @param authenticationToken authentication token of the player who wants to take the tokens.
   * @param tokenType String representing the token type selected by the player, the acceptable
   *                  strings are : "RED", "GREEN", "BLUE", "WHITE", "BLACK"
   * @pre This function checks if the action is valid
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *         HTTP BAD_REQUEST otherwise.
   *         </p>
   */
  ResponseEntity<String> takeTwoTokens(long sessionId,
                                 String authenticationToken,
                                 String tokenType);

  /**
   * Allows to take 3 tokens of 3 given, distinct types.
   *
   * @param sessionId the session's Identification number.
   * @param authenticationToken authentication token of the player who wants to take the tokens.
   * @param tokenTypeOne String representing the first token type selected by the player,
   *                     the acceptable strings are : "RED", "GREEN", "BLUE", "WHITE", "BLACK"
   * @param tokenTypeTwo String representing the second token type selected by the player,
   *                     the acceptable strings are : "RED", "GREEN", "BLUE", "WHITE", "BLACK"
   * @param tokenTypeThree String representing the Third token type selected by the player,
   *                       the acceptable strings are : "RED", "GREEN", "BLUE", "WHITE", "BLACK"
   * @pre the Three token types need to be distinct.
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *         HTTP BAD_REQUEST otherwise.
   *         </p>
   */
  ResponseEntity<String> takeThreeTokens(long sessionId,
                                   String authenticationToken,
                                   String tokenTypeOne,
                                   String tokenTypeTwo,
                                   String tokenTypeThree);
}
