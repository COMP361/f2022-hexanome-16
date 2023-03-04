package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.services.InventoryServiceInterface;
import com.hexanome16.server.util.ServiceUtils;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inventory controller.
 *
 * @author Elea Dufresne
 */
@RestController
public class InventoryController {
  /* fields and controllers ********************************************************/
  private final InventoryServiceInterface inventoryService;
  private final GameManagerServiceInterface gameManager;
  private final ServiceUtils serviceUtils;

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Controller for the Inventory.
   *
   * @param gameServiceInterface controller for the whole game (used for helper)
   * @param gameManager          the game manager for fetching games
   * @param serviceUtils         the utility used by services
   */
  public InventoryController(@Autowired InventoryServiceInterface gameServiceInterface,
                             @Autowired GameManagerServiceInterface gameManager,
                             @Autowired ServiceUtils serviceUtils) {
    this.inventoryService = gameServiceInterface;
    this.gameManager = gameManager;
    this.serviceUtils = serviceUtils;
  }

  /* helper methods ***************************************************************************/

  private Player getValidPlayerByName(long sessionId, String username) {
    Game game = gameManager.getGame(sessionId);

    Player myPlayer = serviceUtils.findPlayerByName(
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
    return inventoryService.getPlayerBankInfo(sessionId, username);
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
   *     HTTP BAD_REQUEST otherwise.
   *     </p>
   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
   */
  @PutMapping(value = {"/games/{sessionId}/{cardMd5}", "/games/{sessionId}/{cardMd5}/"})
  public ResponseEntity<String> buyCard(@PathVariable long sessionId, @PathVariable String cardMd5,
                                        @RequestParam String authenticationToken,
                                        @RequestParam int rubyAmount,
                                        @RequestParam int emeraldAmount,
                                        @RequestParam int sapphireAmount,
                                        @RequestParam int diamondAmount,
                                        @RequestParam int onyxAmount, @RequestParam int goldAmount)
      throws JsonProcessingException {
    return inventoryService.buyCard(sessionId, cardMd5, authenticationToken, rubyAmount,
        emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount);
  }

  /**
   * Let the player reserve a face up card.
   *
   * @param sessionId           game session id.
   * @param cardMd5             card hash.
   * @param authenticationToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  @PutMapping(value = {"/games/{sessionId}/{cardMd5}/reservation"})
  public ResponseEntity<String> reserveCard(@PathVariable long sessionId,
                                            @PathVariable String cardMd5,
                                            @RequestParam String authenticationToken)
      throws JsonProcessingException {
    return inventoryService.reserveCard(sessionId, cardMd5, authenticationToken);
  }

  /**
   * Let the player reserve a face down card.
   *
   * @param sessionId           game session id.
   * @param level               deck level.
   * @param authenticationToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  @PutMapping(value = {"/games/{sessionId}/deck/reservation"})
  public ResponseEntity<String> reserveFaceDownCard(@PathVariable long sessionId,
                                                    @RequestParam String level,
                                                    @RequestParam String authenticationToken)
      throws JsonProcessingException {

    return inventoryService.reserveFaceDownCard(sessionId, level, authenticationToken);
  }

}
