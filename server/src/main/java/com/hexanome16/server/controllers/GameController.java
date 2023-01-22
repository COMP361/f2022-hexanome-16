package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.services.GameServiceInterface;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

  private final GameServiceInterface gameServiceInterface;

  /**
   * Instantiates a new Game controller.
   *
   * @param gameServiceInterface game service to use for backend manipulations
   */
  public GameController(@Autowired GameServiceInterface gameServiceInterface) {
    this.gameServiceInterface = gameServiceInterface;
  }

  /**
   * Gets game map.
   *
   * @return the game map
   */
  public Map<Long, Game> getGameMap() {
    return gameServiceInterface.getGameMap();
  }

  /**
   * Create a new game as client requested.
   *
   * @param sessionId sessionId
   * @param payload   the payload
   * @return error if present
   */
  @PutMapping(value = {"/games/{sessionId}", "/games/{sessionId}/"})
  public String createGame(@PathVariable long sessionId, @RequestBody Map<String, Object> payload) {
    return gameServiceInterface.createGame(sessionId, payload);
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
    return gameServiceInterface.getDeck(sessionId, level, accessToken, hash);
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
    return gameServiceInterface.getNobles(sessionId, accessToken, hash);
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
    return gameServiceInterface.getCurrentPlayer(sessionId, accessToken, hash);
  }

  // Buy Prompt Controllers ////////////////////////////////////////////////////////////////////////

  /**
   * Allows client to see how many of each gem a player has.
   *
   * @param sessionId sessionId.
   * @param username  username of the player.
   * @return String representation of the Purchase map
   * @throws JsonProcessingException if Json processing fails
   */
  @GetMapping(value = {"/games/{sessionId}/playerBank", "/games/{sessionId}/playerBank/"})
  public ResponseEntity<String> getPlayerBankInfo(@PathVariable long sessionId,
                                                  @RequestParam String username)
      throws JsonProcessingException {
    return gameServiceInterface.getPlayerBankInfo(sessionId, username);
  }

  /**
   * Allows client to see how many of each gem the game bank has.
   *
   * @param sessionId sessionId.
   * @return String representation of the Purchase map
   * @throws JsonProcessingException if Json processing fails
   */
  @GetMapping(value = {"/games/{sessionId}/gameBank", "/games/{sessionId}/gameBank/"})
  public ResponseEntity<String> getGameBankInfo(@PathVariable long sessionId)
      throws JsonProcessingException {
    return gameServiceInterface.getGameBankInfo(sessionId);
  }

  /**
   * Ends current player's turn and starts next player's turn.
   *
   * @param game the game the player is in
   */
  public void endCurrentPlayersTurn(Game game) {
    gameServiceInterface.endCurrentPlayersTurn(game);
  }

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
   *          HTTP BAD_REQUEST otherwise.
   *         </p>
   * @throws JsonProcessingException the json processing exception
   */
  @PutMapping(value = {"/games/{sessionId}/{cardMd5}", "/games/{sessionId}/{cardMd5}/"})
  public ResponseEntity<String> buyCard(@PathVariable long sessionId,
                                        @PathVariable String cardMd5,
                                        @RequestParam String authenticationToken,
                                        @RequestParam int rubyAmount,
                                        @RequestParam int emeraldAmount,
                                        @RequestParam int sapphireAmount,
                                        @RequestParam int diamondAmount,
                                        @RequestParam int onyxAmount,
                                        @RequestParam int goldAmount)
      throws JsonProcessingException {
    return gameServiceInterface.buyCard(sessionId, cardMd5, authenticationToken, rubyAmount,
        emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount);
  }
}