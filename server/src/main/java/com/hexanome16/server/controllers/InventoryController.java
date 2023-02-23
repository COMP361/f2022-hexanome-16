package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameServiceInterface;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Inventory controller.
 *
 * @author Elea Dufresne
 */
@RestController
public class InventoryController {
  /* fields and controllers ********************************************************/
  private final RestTemplate restTemplate;
  private final UrlUtils urlUtils;
  private final GameServiceInterface gameServiceInterface;
  private final GameManagerServiceInterface gameManager;
  private final ObjectMapper objectMapper;

  private final AuthServiceInterface authService;

  /**
   * Controller for the Inventory.
   *
   * @param restTemplateBuilder  server
   * @param urlUtils             operations
   * @param gameServiceInterface controller for the whole game (used for helper)
   * @param objectMapper         the object mapper
   * @param authService          the authentication service
   * @param gameManager          the game manager for fetching games
   */
  public InventoryController(RestTemplateBuilder restTemplateBuilder, UrlUtils urlUtils,
                             @Autowired GameServiceInterface gameServiceInterface,
                             ObjectMapper objectMapper,
                             @Autowired AuthServiceInterface authService,
                             @Autowired GameManagerServiceInterface gameManager) {
    this.restTemplate = restTemplateBuilder.build();
    this.urlUtils = urlUtils;
    this.gameServiceInterface = gameServiceInterface;
    this.objectMapper = objectMapper;
    this.authService = authService;
    this.gameManager = gameManager;
  }

  /* helper methods ***************************************************************************/

  private Player getValidPlayer(long sessionId, String accessToken) {
    Game game = gameManager.getGame(sessionId);
    // verify that the request is valid
    if (!authService.verifyPlayer(accessToken, game)) {
      throw new IllegalArgumentException("Invalid Player.");
    }
    // get the player from the session id and access token
    return gameServiceInterface.findPlayerByToken(
        game, accessToken
    );
  }


  private Player getValidPlayerByName(long sessionId, String username) {
    Game game = gameManager.getGame(sessionId);

    Player myPlayer = gameServiceInterface.findPlayerByName(
        game, username
    );
    if (myPlayer == null) {
      throw new IllegalArgumentException("Invalid Player.");
    }
    // get the player from the session id and access token
    return myPlayer;
  }

  private JsonNode getInventoryNode(Player player) throws JsonProcessingException {
    // convert the inventory to string and return it as a String
    String inventoryString = objectMapper.writeValueAsString(player.getInventory());
    return objectMapper.readTree(inventoryString);
  }

  //  /* POST methods *****************************************************************************/
  //
  //  /** Create a new inventory for the given player.
  //   *
  //   * @param sessionId ID of the current section
  //   * @param accessToken access token for this request
  //   * @return {@link ResponseEntity} the inventory as a response entity
  //   * @throws JsonProcessingException com.fasterxml.jackson.core. json processing exception
  //   * */
  //  @PostMapping(value = {"/games/{sessionId}/inventory"})
  //  public ResponseEntity<String> createInventory(@PathVariable long sessionId,
  //                                                @RequestParam String accessToken)
  //          throws JsonProcessingException {
  //    // get the player (if valid) from the session id and access token
  //    Player player = getValidPlayer(sessionId, accessToken);
  //    // create a new inventory
  //    player.setInventory(new Inventory());
  //    // return the inventory as a response entity
  //    return new ResponseEntity<>(objectMapper.writeValueAsString(player.getInventory()),
  //        HttpStatus.CREATED);
  //  }

  /* GET methods ******************************************************************************/

  /**
   * get Cards.
   *
   * @param sessionId session id.
   * @param username  access token
   * @return response cards
   * @throws com.fasterxml.jackson.core.JsonProcessingException if json doesnt work.
   */
  @GetMapping(value = {"/games/{sessionId}/inventory/cards"})
  public ResponseEntity<String> getCards(@PathVariable long sessionId,
                                         @RequestParam String username)
      throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    Player player = getValidPlayerByName(sessionId, username);
    // return the cards in the inventory as a response entity
    return new ResponseEntity<>(
        objectMapper.writeValueAsString(player.getInventory().getOwnedCards()),
        HttpStatus.OK
    );
  }

  /**
   * get Nobles.
   *
   * @param sessionId session id.
   * @param username  access token.
   * @return response entity.
   * @throws com.fasterxml.jackson.core.JsonProcessingException if json doesnt work
   */
  @GetMapping(value = {"/games/{sessionId}/inventory/nobles"})
  public ResponseEntity<String> getNobles(@PathVariable long sessionId,
                                          @RequestParam String username)
      throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    Player player = getValidPlayerByName(sessionId, username);
    // return the cards in the inventory as a response entity
    return new ResponseEntity<>(
        objectMapper.writeValueAsString(player.getInventory().getOwnedNobles()),
        HttpStatus.OK);
  }

  /**
   * Get reserved Cards, with private cards.
   *
   * @param sessionId   session Id.
   * @param username    username.
   * @param accessToken access Token.
   * @return get reserve nobles.
   * @throws com.fasterxml.jackson.core.JsonProcessingException if json doesnt work.
   */
  @GetMapping(value = {"/games/{sessionId}/inventory/reservedCards"})
  public ResponseEntity<String> getReservedCards(@PathVariable long sessionId,
                                                 @RequestParam String username,
                                                 @RequestParam String accessToken)
      throws JsonProcessingException {

    // get the player (if valid) from the session id and access token
    Player player = getValidPlayerByName(sessionId, username);
    // return the reserved level cards in the inventory as a response entity
    return new ResponseEntity<>(objectMapper.writeValueAsString(
        player.getInventory().getReservedCards()), HttpStatus.OK);
  }

  /**
   * get reserved nobles.
   *
   * @param sessionId session id.
   * @param username  access token.
   * @return response entity.
   * @throws com.fasterxml.jackson.core.JsonProcessingException if json doesnt work.
   */
  @GetMapping(value = {"/games/{sessionId}/inventory/reservedNobles"})
  public ResponseEntity<String> getReservedNobles(@PathVariable long sessionId,
                                                  @RequestParam String username)
      throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    Player player = getValidPlayerByName(sessionId, username);
    // return the reserved nobles in the inventory as a response entity
    return new ResponseEntity<>(objectMapper.writeValueAsString(
        player.getInventory().getReservedNobles()), HttpStatus.OK);
  }

}
