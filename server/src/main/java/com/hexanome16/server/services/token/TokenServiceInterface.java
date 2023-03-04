package com.hexanome16.server.services.token;

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
   * @throws com.fasterxml.jackson.core.JsonProcessingException if tokens cannot be converted
   */
  ResponseEntity<String> availableTwoTokensType(long sessionId) throws JsonProcessingException;

  /**
   * Gets all the token types one can take 3 of in a given game with sessionId
   * identification number.
   *
   * @param sessionId the session's Identification number.
   * @return <p>CustomHttpResponse.INVALID_SESSION_ID if no such session, HttpStatus_OK
   *        otherwise with a body containing a string representation of the available token
   *        types that you can take one of</p>
   * @throws com.fasterxml.jackson.core.JsonProcessingException if tokens cannot be converted
   */
  ResponseEntity<String> availableThreeTokensType(long sessionId) throws JsonProcessingException;

  /**
   * Allows to take 2 tokens of a given type.
   * This function checks if the action is valid
   *
   * @param sessionId the session's Identification number.
   * @param authenticationToken authentication token of the player who wants to take the tokens.
   * @param tokenType String representing the token type selected by the player, the acceptable
   *                  strings are : "RED", "GREEN", "BLUE", "WHITE", "BLACK"
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *         HTTP BAD_REQUEST otherwise.
   *         </p>
   */
  ResponseEntity<String> takeTwoTokens(long sessionId,
                                 String authenticationToken,
                                 String tokenType);

  /**
   * Allows to take 3 tokens of 3 given, distinct types.
   * The three token types need to be distinct.
   *
   * @param sessionId the session's Identification number.
   * @param authenticationToken authentication token of the player who wants to take the tokens.
   * @param tokenTypeOne String representing the first token type selected by the player,
   *                     the acceptable strings are : "RED", "GREEN", "BLUE", "WHITE", "BLACK"
   * @param tokenTypeTwo String representing the second token type selected by the player,
   *                     the acceptable strings are : "RED", "GREEN", "BLUE", "WHITE", "BLACK"
   * @param tokenTypeThree String representing the Third token type selected by the player,
   *                       the acceptable strings are : "RED", "GREEN", "BLUE", "WHITE", "BLACK"
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *         HTTP BAD_REQUEST otherwise.
   *         </p>
   */
  ResponseEntity<String> takeThreeTokens(long sessionId,
                                   String authenticationToken,
                                   String tokenTypeOne,
                                   String tokenTypeTwo,
                                   String tokenTypeThree);

  /**
   * Allows to give back one token of a given token type.
   *
   * @param sessionId the session's Identification number.
   * @param authenticationToken authentication token of the player who wants to take the tokens.
   * @param tokenType String representing the token type selected by the player, the acceptable
   *                  strings are : "RED", "GREEN", "BLUE", "WHITE", "BLACK"
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *         HTTP BAD_REQUEST otherwise.
   *         </p>
   */
  ResponseEntity<String> giveBackToken(long sessionId,
                                       String authenticationToken,
                                       String tokenType);
}
