package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.services.TradePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for trade post requests.
 */
@RestController
public class TradePostController {
  TradePostService tradePostService;

  /**
   * Controller for trade post requests.
   *
   * @param tradePostService trade post service.
   */
  public TradePostController(@Autowired TradePostService tradePostService) {
    this.tradePostService = tradePostService;
  }

  /**
   * Request for the trade post obtained by the player.
   *
   * @param sessionId sessionId of the game.
   * @param username  username of the player.
   * @return the trade posts the player has.
   */
  @GetMapping(value = {"/games/{sessionId}/tradePost"})
  public ResponseEntity<String> getPlayerTradePosts(@PathVariable long sessionId,
                                                    @RequestParam String username) {
    return tradePostService.getPlayerTradePosts(sessionId, username);
  }
}
