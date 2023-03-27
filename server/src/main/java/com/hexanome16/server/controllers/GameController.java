package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameService;
import com.hexanome16.server.services.game.GameServiceInterface;
import com.hexanome16.server.services.longpolling.LongPollingServiceInterface;
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
  @PutMapping(value = "/games/{sessionId}")
  public String createGame(@PathVariable long sessionId, @RequestBody SessionJson payload) {
    System.out.println(payload);
    return gameManager.createGame(sessionId, payload);
  }

  /**
   * Deletes a game from the game server. (callback from LS)
   *
   * @param sessionId the id of the game to delete
   */
  @DeleteMapping(value = "/games/{sessionId}")
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
  * Returns the level two cards on board.
  *
  * @param sessionId Identification number of the session.
  * @return level two cards on board.
  * @throws JsonProcessingException throws an exception if fails to convert to json.
   */
  @GetMapping(value = {"games/{sessionId}/board/cards/levelTwo"})
  public ResponseEntity<String> getLevelTwoOnBoard(@PathVariable long sessionId)
      throws JsonProcessingException {
    return gameService.getLevelTwoOnBoard(sessionId);
  }

  /**
   * Returns the level one cards on board.
   *
   * @param sessionId Identification number of the session.
   * @return level one cards on board.
   * @throws JsonProcessingException throws an exception if fails to convert to json
   */
  @GetMapping(value = "games/{sessionId}/board/cards/levelOne")
  public ResponseEntity<String> getLevelOneOnBoard(@PathVariable long sessionId)
      throws JsonProcessingException {
    return gameService.getLevelOneOnBoard(sessionId);
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
   * Returns cities present on the game board.
   *
   * @param sessionId   session id
   * @param accessToken access token
   * @param hash        the hash
   * @return nobles present on the game board
   */
  @GetMapping(value = "/games/{sessionId}/cities", produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getCities(@PathVariable long sessionId,
                                                          @RequestParam String accessToken,
                                                          @RequestParam String hash) {
    return longPollingService.getCities(sessionId, accessToken, hash);
  }


  /**
   * Return the username of current player.
   *
   * @param sessionId   game id
   * @param accessToken player access token
   * @param hash        hash for long polling
   * @return current player username
   */
  @GetMapping(value = "/games/{sessionId}/players", produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getPlayers(@PathVariable long sessionId,
                                                                 @RequestParam String accessToken,
                                                                 @RequestParam String hash) {
    return longPollingService.getPlayers(sessionId, accessToken, hash);
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

  /**
   * Allows client to see how many of each gem the game bank has.
   *
   * @param sessionId sessionId.
   * @return String representation of the Purchase map
   * @throws com.fasterxml.jackson.core.JsonProcessingException if Json processing fails
   */
  @GetMapping(value = "/games/{sessionId}/gameBank")
  public ResponseEntity<String> getGameBankInfo(@PathVariable long sessionId)
      throws JsonProcessingException {
    return gameService.getGameBankInfo(sessionId);
  }


  /**
   * Gets player's top action.
   *
   * @param sessionId session Id.
   * @param username username of player whose action we want to retrieve.
   * @param accessToken access token of player.
   * @return Response entity of the top action.
   */
  @GetMapping(value = {"/games/{sessionId}/players/{username}/actions"})
  public ResponseEntity<String> getPlayerAction(@PathVariable long sessionId,
                                                @PathVariable String username,
                                                @RequestParam String accessToken) {
    return gameService.getPlayerAction(sessionId, accessToken);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
}
