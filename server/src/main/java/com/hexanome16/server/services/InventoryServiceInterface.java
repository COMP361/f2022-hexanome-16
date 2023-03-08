package com.hexanome16.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.common.models.price.PurchaseMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interface for managing inventory related backend requests.
 */
public interface InventoryServiceInterface {

  /**
   * Allows client to see how many of each gem a player has.
   *
   * @param sessionId sessionId.
   * @param username  username of the player.
   * @return String representation of the Purchase map
   * @throws com.fasterxml.jackson.core.JsonProcessingException if Json processing fails
   */
  ResponseEntity<String> getPlayerBankInfo(long sessionId,
                                           String username)
      throws JsonProcessingException;

  /**
   * Allows client to buy card, given that they send a valid way to buy that card.
   *
   * @param sessionId           sessionID.
   * @param cardMd5             Card we want to purchase's md5.
   * @param authenticationToken username of the player trying to buy the card.
   * @param proposedDeal        Purchase map denoting player's offer.
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *     HTTP BAD_REQUEST otherwise.</p>
   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
   */
  ResponseEntity<String> buyCard(long sessionId,
                                 String cardMd5,
                                 String authenticationToken,
                                 PurchaseMap proposedDeal)
      throws JsonProcessingException;

  /**
   * Let the player reserve a card.
   *
   * @param sessionId           game session id.
   * @param cardMd5             card hash.
   * @param authenticationToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  ResponseEntity<String> reserveCard(@PathVariable long sessionId,
                                     @PathVariable String cardMd5,
                                     @RequestParam String authenticationToken)
      throws JsonProcessingException;


  /**
   * Let the player reserve a face down card.
   *
   * @param sessionId           game session id.
   * @param level               deck level.
   * @param authenticationToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  ResponseEntity<String> reserveFaceDownCard(@PathVariable long sessionId,
                                             @RequestParam String level,
                                             @RequestParam String authenticationToken)
      throws JsonProcessingException;

  /**
   * Takes a card of level two if allowed to.
   *
   * @param sessionId Id of game.
   * @param authenticationToken token of requesting player.
   * @param chosenCard chosen card's hash
   * @return Response Entity with the next action that needs to be done.
   */
  ResponseEntity<String> takeLevelTwoCard(long sessionId, String authenticationToken,
                                          String chosenCard);

  /**
   * Acquire noble response entity.
   *
   * @param sessionId           game session id
   * @param nobleHash           md5 hash of noble
   * @param authenticationToken player's authentication token
   * @return HttpStatus.ok if the request is valid, one of our CustomHttp responses otherwise.
   * @throws JsonProcessingException the json processing exception
   */
  ResponseEntity<String> acquireNoble(long sessionId, String nobleHash, String authenticationToken)
      throws JsonProcessingException;
}
