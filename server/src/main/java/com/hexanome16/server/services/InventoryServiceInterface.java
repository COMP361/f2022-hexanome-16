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
   * @param accessToken username of the player trying to buy the card.
   * @param proposedDeal        Purchase map denoting player's offer.
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *     HTTP BAD_REQUEST otherwise.</p>
   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
   */
  ResponseEntity<String> buyCard(long sessionId,
                                 String cardMd5,
                                 String accessToken,
                                 PurchaseMap proposedDeal)
      throws JsonProcessingException;

  /**
   * Let the player reserve a card.
   *
   * @param sessionId           game session id.
   * @param cardMd5             card hash.
   * @param accessToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  ResponseEntity<String> reserveCard(@PathVariable long sessionId,
                                     @PathVariable String cardMd5,
                                     @RequestParam String accessToken)
      throws JsonProcessingException;


  /**
   * Let the player reserve a face down card.
   *
   * @param sessionId           game session id.
   * @param level               deck level.
   * @param accessToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  ResponseEntity<String> reserveFaceDownCard(@PathVariable long sessionId,
                                             @RequestParam String level,
                                             @RequestParam String accessToken)
      throws JsonProcessingException;

  /**
   * Takes a card of level two if allowed to.
   *
   * @param sessionId Id of game.
   * @param accessToken token of requesting player.
   * @param chosenCard chosen card's hash
   * @return Response Entity with the next action that needs to be done.
   */
  ResponseEntity<String> takeLevelTwoCard(long sessionId, String accessToken,
                                          String chosenCard);


  /**
   * Takes a card of level one if allowed to.
   *
   * @param sessionId Id of game.
   * @param accessToken token of requesting player.
   * @param chosenCard chosen card's hash
   * @return Response Entity with the next action that needs to be done.
   */
  ResponseEntity<String> takeLevelOneCard(long sessionId, String accessToken, String chosenCard);


  /**
   * Acquire noble response entity.
   *
   * @param sessionId           game session id
   * @param nobleHash           md5 hash of noble
   * @param accessToken player's authentication token
   * @return HttpStatus.ok if the request is valid, one of our CustomHttp responses otherwise.
   * @throws JsonProcessingException the json processing exception
   */
  ResponseEntity<String> acquireNoble(long sessionId, String nobleHash, String accessToken)
      throws JsonProcessingException;

  /**
   * Acquire noble response entity.
   *
   * @param sessionId           game session id
   * @param cityHash           md5 hash of noble
   * @param accessToken player's authentication token
   * @return HttpStatus.ok if the request is valid, one of our CustomHttp responses otherwise.
   * @throws JsonProcessingException the json processing exception
   */
  ResponseEntity<String> acquireCity(long sessionId, String cityHash, String accessToken)
      throws JsonProcessingException;

  /**
   * Associates a bag card to a token type.
   *
   * @param sessionId session id.
   * @param accessToken access token.
   * @param tokenType chosen token type.
   * @return information on next action or invalid request message
   */
  ResponseEntity<String> associateBagCard(long sessionId, String accessToken, String tokenType);

  /**
   * Get owned bonuses for player with token in game.
   *
   * @param sessionId session id of game.
   * @param accessToken access token of player.
   * @return Response entity with a Json string representation of
   *          a String[gem.getBonusTypeEquivalent]
   */
  ResponseEntity<String> getOwnedBonuses(long sessionId, String accessToken);
}
