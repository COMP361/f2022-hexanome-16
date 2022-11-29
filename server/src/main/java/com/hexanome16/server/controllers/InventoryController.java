package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.Inventory;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.util.UrlUtils;
import java.util.Optional;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
  private final GameController gameController;
  private final ObjectMapper objectMapper;

  /**
   * Controller for the Inventory.
   *
   * @param gameController      controller for the whole game (used for helper)
   * @param urlUtils            operations
   * @param restTemplateBuilder server
   */
  public InventoryController(RestTemplateBuilder restTemplateBuilder, UrlUtils urlUtils,
                             GameController gameController, ObjectMapper objectMapper) {
    this.restTemplate = restTemplateBuilder.build();
    this.urlUtils = urlUtils;
    this.gameController = gameController;
    this.objectMapper = objectMapper;
  }

  /* helper methods ***************************************************************************/

  private Player getValidPlayer(long sessionId, String accessToken) {
    // verify that the request is valid
    if (!gameController.verifyPlayer(sessionId, accessToken)) {
      throw new IllegalArgumentException("Invalid Player.");
    }
    // get the player from the session id and access token
    return gameController.findPlayerByToken(
        gameController.getGameMap().get(sessionId), accessToken
    );
  }


  private Player getValidPlayerByName(long sessionId, String username) {

    Player myPlayer = gameController.findPlayerByName(
        gameController.getGameMap().get(sessionId), username
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

  /* POST methods *****************************************************************************/

  /** Create a new inventory for the given player.
   *
   * @param sessionId ID of the current section
   * @param accessToken access token for this request
   * @return {@link ResponseEntity} the inventory as a response entity
   * @throws JsonProcessingException com.fasterxml.jackson.core. json processing exception
   * */
  @PostMapping(value = {"/games/{sessionId}/inventory"})
  public ResponseEntity<String> createInventory(@PathVariable long sessionId,
                                                @RequestParam String accessToken)
          throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    Player player = getValidPlayer(sessionId, accessToken);
    // create a new inventory
    player.setInventory(new Inventory());
    // return the inventory as a response entity
    return new ResponseEntity<>(objectMapper.writeValueAsString(player.getInventory()),
        HttpStatus.CREATED);
  }

  /* DELETE methods ************************************************************************/

  /**
   * Delete inventory.
   *
   * @param sessionId session
   * @param accessToken access token
   * @return response entity
   */
  @DeleteMapping(value = {"/games/{sessionId}/inventory"})
  public ResponseEntity<Void> deleteInventory(@PathVariable long sessionId,
                                              @RequestParam String accessToken) {
    // get the player (if valid) from the session id and access token
    Player player = getValidPlayer(sessionId, accessToken);
    // delete the inventory and return the success
    player.deleteInventory();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /* GET methods ******************************************************************************/

  /**
   * get Cards.
   *
   * @param sessionId session id.
   * @param username access token
   * @return response.
   * @throws JsonProcessingException if json doesnt work.
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
   * @param username access token.
   * @return response entity.
   * @throws JsonProcessingException if json doesnt work
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

  // TODO : IMPLEMETNATIAAOSN
  /**
   * Get reserved Cards, with private cards.
   *
   * @param sessionId session Id.
   * @param username username.
   * @param accessToken access Token.
   * @return get reserve nobles.
   * @throws JsonProcessingException if json doesnt work.
   */
  @GetMapping(value = {"/games/{sessionId}/inventory/reservedCards"})
  public ResponseEntity<String> getReservedCards(@PathVariable long sessionId,
                                                        @RequestParam String username,
                                                        @RequestParam String accessToken)
          throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    Player player = getValidPlayer(sessionId, accessToken);
    // return the reserved level cards in the inventory as a response entity
    JsonNode node = getInventoryNode(player);
    return new ResponseEntity<>(node.get("reservedCards").asText(), HttpStatus.OK);
  }

  /**
   * get reserved nobles.
   *
   * @param sessionId session id.
   * @param username access token.
   * @return response entity.
   * @throws JsonProcessingException if json doesnt work.
   */
  @GetMapping(value = {"/games/{sessionId}/inventory/reservedNobles"})
  public ResponseEntity<String> getReservedNobles(@PathVariable long sessionId,
                                                  @RequestParam String username)
          throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    Player player = getValidPlayerByName(sessionId, username);
    // return the reserved nobles in the inventory as a response entity
    JsonNode node = getInventoryNode(player);
    return new ResponseEntity<>(node.get("reservedNobles").asText(), HttpStatus.OK);
  }

}
