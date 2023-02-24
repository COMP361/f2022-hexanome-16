package com.hexanome16.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import lombok.NonNull;
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
   * @param rubyAmount          amount of ruby gems proposed.
   * @param emeraldAmount       amount of emerald gems proposed.
   * @param sapphireAmount      amount of sapphire gems proposed.
   * @param diamondAmount       amount of diamond gems proposed.
   * @param onyxAmount          amount of onyx gems proposed.
   * @param goldAmount          amount of gold gems proposed.
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *         HTTP BAD_REQUEST otherwise.
   *         </p>
   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
   */
  ResponseEntity<String> buyCard(long sessionId,
                                 String cardMd5,
                                 String authenticationToken,
                                 int rubyAmount,
                                 int emeraldAmount,
                                 int sapphireAmount,
                                 int diamondAmount,
                                 int onyxAmount,
                                 int goldAmount)
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

}
