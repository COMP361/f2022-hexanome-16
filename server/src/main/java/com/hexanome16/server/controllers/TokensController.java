package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.services.TokenServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class containing endpoints for everything related to tokens.
 */
@RestController
public class TokensController {

  private final TokenServiceInterface tokenService;


  /**
   * Constructor for the Tokens Controller class.
   *
   * @param tokenService token service used for backend manipulations
   */
  public TokensController(@Autowired TokenServiceInterface tokenService) {
    this.tokenService = tokenService;
  }


  /**
   * Gets all the token types one can take 2 of in a given game with sessionId
   * identification number.
   *
   * @param sessionId the session's Identification number.
   * @return String representation of a list of all the available token types
   * @throws com.fasterxml.jackson.core.JsonProcessingException if tokens cannot be converted
   */
  @GetMapping(value = {"/games/{sessionId}/twoTokens"})
  public ResponseEntity<String> availableTwoTokensType(@PathVariable long sessionId)
      throws JsonProcessingException {
    return tokenService.availableTwoTokensType(sessionId);
  }

  /**
   * Gets all the token types one can take 3 of in a given game with sessionId
   * identification number.
   *
   * @param sessionId the session's Identification number.
   * @return String representation of a list of all the available token types
   * @throws com.fasterxml.jackson.core.JsonProcessingException if tokens cannot be converted
   */
  @GetMapping(value = {"/games/{sessionId}/threeTokens"})
  public ResponseEntity<String> availableThreeTokensType(@PathVariable long sessionId)
      throws JsonProcessingException {
    return tokenService.availableThreeTokensType(sessionId);
  }

  /**
   * Allows to take 2 tokens of a given type, This function checks if the action is valid.
   *
   * @param sessionId the session's Identification number.
   * @param authenticationToken authentication token of the player who wants to take the tokens.
   * @param tokenType String representing the token type selected by the player, the acceptable
   *                  strings are : "RED", "GREEN", "BLUE", "WHITE", "BLACK"
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *         HTTP BAD_REQUEST otherwise.
   *         </p>
   */
  @PutMapping(value = {"/games/{sessionId}/twoTokens"})
  public ResponseEntity<String> takeTwoTokens(@PathVariable long sessionId,
                                              @RequestParam String authenticationToken,
                                              @RequestParam String tokenType) {
    return tokenService.takeTwoTokens(sessionId, authenticationToken, tokenType);
  }

  /**
   * Allows to take 3 tokens of 3 given, distinct types,
   * This function checks if the action is valid.
   * The Three token types need to be distinct.
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
  @PutMapping(value = {"/games/{sessionId}/threeTokens"})
  public ResponseEntity<String> takeThreeTokens(@PathVariable long sessionId,
                                                @RequestParam String authenticationToken,
                                                @RequestParam String tokenTypeOne,
                                                @RequestParam String tokenTypeTwo,
                                                @RequestParam String tokenTypeThree) {
    return tokenService.takeThreeTokens(sessionId, authenticationToken,
        tokenTypeOne, tokenTypeTwo, tokenTypeThree);
  }
}
