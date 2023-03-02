package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameServiceInterface;
import com.hexanome16.server.services.longpolling.LongPollingServiceInterface;
import dto.SessionJson;
import models.price.PurchaseMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Not implemented.
 */
@RestController
public class GameController {

  private final GameServiceInterface gameService;
  private final GameManagerServiceInterface gameManager;
  private final LongPollingServiceInterface longPollingService;

  /**
   * Instantiates a new Game controller.
   *
   * @param gameService        game service to use for backend manipulations of individual games
   * @param gameManagerService game manager to manage different game instances
   * @param longPollingService long polling service to use for long polling
   */
  public GameController(@Autowired GameServiceInterface gameService,
                        @Autowired GameManagerServiceInterface gameManagerService,
                        @Autowired LongPollingServiceInterface longPollingService) {
    this.gameService = gameService;
    this.gameManager = gameManagerService;
    this.longPollingService = longPollingService;
  }

  /**
   * Create a new game as client requested.
   *
   * @param sessionId sessionId
   * @param payload   the payload
   * @return error if present
   */
  @PutMapping(value = {"/games/{sessionId}", "/games/{sessionId}/"})
  public String createGame(@PathVariable long sessionId, @RequestBody SessionJson payload) {
    System.out.println(payload);
    return gameManager.createGame(sessionId, payload);
  }

  /**
   * Deletes a game from the game server. (callback from LS)
   *
   * @param sessionId the id of the game to delete
   */
  @DeleteMapping(value = {"/games/{sessionId}"})
  public void deleteGame(@PathVariable long sessionId) {
    gameManager.deleteGame(sessionId);
  }

  /**
   * Long polling on update on onboard deck.
   *
   * @param sessionId   game session id
   * @param level       deck level
   * @param accessToken account access token
   * @param hash        hash used for long polling
   * @return updated game deck
   */
  @GetMapping(value = "/games/{sessionId}/deck", produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getDeck(@PathVariable long sessionId,
                                                        @RequestParam String level,
                                                        @RequestParam String accessToken,
                                                        @RequestParam String hash) {
    return longPollingService.getDeck(sessionId, level, accessToken, hash);
  }

  /**
   * Returns nobles present on the game board.
   *
   * @param sessionId   session id
   * @param accessToken access token
   * @param hash        the hash
   * @return nobles present on the game board
   */
  @GetMapping(value = "/games/{sessionId}/nobles", produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getNobles(@PathVariable long sessionId,
                                                          @RequestParam String accessToken,
                                                          @RequestParam String hash) {
    return longPollingService.getNobles(sessionId, accessToken, hash);
  }

  /**
   * Return the username of current player.
   *
   * @param sessionId   game id
   * @param accessToken player access token
   * @param hash        hash for long polling
   * @return current player username
   */
  @GetMapping(value = "/games/{sessionId}/player", produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getCurrentPlayer(@PathVariable long sessionId,
                                                                 @RequestParam String accessToken,
                                                                 @RequestParam String hash) {
    return longPollingService.getCurrentPlayer(sessionId, accessToken, hash);
  }

  /**
   * Return the winners of the game.
   *
   * @param sessionId   game id
   * @param accessToken player access token
   * @param hash        hash for long polling
   * @return winners of the game
   */
  @GetMapping(value = "/games/{sessionId}/winners", produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getWinners(@PathVariable long sessionId,
                                                           @RequestParam String accessToken,
                                                           @RequestParam String hash) {
    return longPollingService.getWinners(sessionId, accessToken, hash);
  }

  // Buy Prompt Controllers ////////////////////////////////////////////////////////////////////////

  /**
   * Allows client to see how many of each gem a player has.
   *
   * @param sessionId sessionId.
   * @param username  username of the player.
   * @return String representation of the Purchase map
   * @throws com.fasterxml.jackson.core.JsonProcessingException if Json processing fails
   */
  @GetMapping(value = {"/games/{sessionId}/playerBank", "/games/{sessionId}/playerBank/"})
  public ResponseEntity<String> getPlayerBankInfo(@PathVariable long sessionId,
                                                  @RequestParam String username)
      throws JsonProcessingException {
    return gameService.getPlayerBankInfo(sessionId, username);
  }

  /**
   * Allows client to see how many of each gem the game bank has.
   *
   * @param sessionId sessionId.
   * @return String representation of the Purchase map
   * @throws com.fasterxml.jackson.core.JsonProcessingException if Json processing fails
   */
  @GetMapping(value = {"/games/{sessionId}/gameBank", "/games/{sessionId}/gameBank/"})
  public ResponseEntity<String> getGameBankInfo(@PathVariable long sessionId)
      throws JsonProcessingException {
    return gameService.getGameBankInfo(sessionId);
  }

  /**
   * Allows client to buy card, given that they send a valid way to buy that card.
   *
   * @param sessionId           sessionID.
   * @param cardMd5             Card we want to purchase's md5.
   * @param accessToken         token of the player trying to buy the card.
   * @param purchaseMap         PurchaseMap denoting player's offer.
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *     HTTP BAD_REQUEST otherwise.</p>
   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
   */
  @PutMapping(value = {"/games/{sessionId}/{cardMd5}", "/games/{sessionId}/{cardMd5}/"})
  public ResponseEntity<String> buyCard(@PathVariable long sessionId, @PathVariable String cardMd5,
                                        @RequestParam String accessToken,
                                        @RequestBody PurchaseMap purchaseMap)
      throws JsonProcessingException {
    return gameService.buyCard(sessionId, cardMd5, accessToken, purchaseMap);
  }

  /**
   * Let the player reserve a face up card.
   *
   * @param sessionId           game session id.
   * @param cardMd5             card hash.
   * @param accessToken         player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  @PutMapping(value = {"/games/{sessionId}/{cardMd5}/reservation"})
  public ResponseEntity<String> reserveCard(@PathVariable long sessionId,
                                            @PathVariable String cardMd5,
                                            @RequestParam String accessToken)
      throws JsonProcessingException {
    return gameService.reserveCard(sessionId, cardMd5, accessToken);
  }

  /**
   * Let the player reserve a face down card.
   *
   * @param sessionId           game session id.
   * @param level               deck level.
   * @param accessToken         player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  @PutMapping(value = {"/games/{sessionId}/deck/reservation"})
  public ResponseEntity<String> reserveFaceDownCard(@PathVariable long sessionId,
                                                    @RequestParam String level,
                                                    @RequestParam String accessToken)
      throws JsonProcessingException {

    return gameService.reserveFaceDownCard(sessionId, level, accessToken);
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////
}
