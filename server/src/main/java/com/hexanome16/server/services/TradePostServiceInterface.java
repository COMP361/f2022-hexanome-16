package com.hexanome16.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

/**
 * Interface for trade post service.
 */
public interface TradePostServiceInterface {
  /**
   * Request for the trade post obtained by the player.
   *
   * @param sessionId sessionId of the game.
   * @param username  username of the player.
   * @return the trade posts the player has.
   * @throws JsonProcessingException json exception
   */
  public ResponseEntity<String> getPlayerTradePosts(long sessionId, String username);
}
