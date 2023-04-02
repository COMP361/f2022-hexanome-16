package com.hexanome16.server.controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.OrientPurchaseMap;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.InventoryServiceInterface;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ServiceUtils serviceUtils;


  /**
   * Controller for the Inventory.
   *
   * @param inventoryServiceInterface controller for the whole game (used for helper)
   * @param gameManager               the game manager for fetching games
   * @param serviceUtils              the utility used by services
   */
  public InventoryController(@Autowired InventoryServiceInterface inventoryServiceInterface,
                             @Autowired GameManagerServiceInterface gameManager,
                             @Autowired ServiceUtils serviceUtils) {
    this.gameManager = gameManager;
    this.inventoryService = inventoryServiceInterface;
    this.serviceUtils = serviceUtils;
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  }



  //  private JsonNode getInventoryNode(ServerPlayer player) throws JsonProcessingException {
  //    // convert the inventory to string and return it as a String
  //    String inventoryString = objectMapper.writeValueAsString(player.getInventory());
  //    return objectMapper.readTree(inventoryString);
  //  }

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
  @GetMapping(value = "/games/{sessionId}/inventory/cards")
  public ResponseEntity<String> getCards(@PathVariable long sessionId,
                                         @RequestParam String username)
      throws JsonProcessingException {

    return inventoryService.getCards(sessionId, username);
  }

  /**
   * Get owned bonuses for player with token in game.
   *
   * @param sessionId session id of game.
   * @param accessToken access token of player.
   * @return Response entity with a Json string representation of
   *          a String[gem.getBonusTypeEquivalent]
   */
  @GetMapping(value = "/games/{sessionId}/cards/bonuses")
  public ResponseEntity<String> getOwnedBonuses(@PathVariable long sessionId,
                                                @RequestParam String accessToken) {
    return inventoryService.getOwnedBonuses(sessionId, accessToken);
  }

  /**
   * get Nobles.
   *
   * @param sessionId session id.
   * @param username  access token.
   * @return response entity.
   * @throws com.fasterxml.jackson.core.JsonProcessingException if json doesnt work
   */
  @GetMapping(value = "/games/{sessionId}/inventory/nobles")
  public ResponseEntity<String> getNobles(@PathVariable long sessionId,
                                          @RequestParam String username)
      throws JsonProcessingException {


    return inventoryService.getNobles(sessionId, username);
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
  @GetMapping(value = "/games/{sessionId}/inventory/reservedCards")
  public ResponseEntity<String> getReservedCards(@PathVariable long sessionId,
                                                 @RequestParam String username,
                                                 @RequestParam String accessToken)
      throws JsonProcessingException {


    return inventoryService.getReservedCards(sessionId, username, accessToken);
  }

  /**
   * get reserved nobles.
   *
   * @param sessionId session id.
   * @param username  access token.
   * @return response entity.
   * @throws com.fasterxml.jackson.core.JsonProcessingException if json doesnt work.
   */
  @GetMapping(value = "/games/{sessionId}/inventory/reservedNobles")
  public ResponseEntity<String> getReservedNobles(@PathVariable long sessionId,
                                                  @RequestParam String username)
      throws JsonProcessingException {

    return inventoryService.getReservedNobles(sessionId, username);
  }

  /**
   * Allows client to see how many of each gem a player has.
   *
   * @param sessionId sessionId.
   * @param username  username of the player.
   * @return String representation of the Purchase map
   * @throws com.fasterxml.jackson.core.JsonProcessingException if Json processing fails
   */
  @GetMapping(value = "/games/{sessionId}/playerBank")
  public ResponseEntity<String> getPlayerBankInfo(@PathVariable long sessionId,
                                                  @RequestParam String username)
      throws JsonProcessingException {
    return inventoryService.getPlayerBankInfo(sessionId, username);
  }

  /**
   * Allows client to buy card, given that they send a valid way to buy that card.
   *
   * @param sessionId   sessionID.
   * @param cardMd5     Card we want to purchase's md5.
   * @param accessToken token of the player trying to buy the card.
   * @param purchaseMap PurchaseMap denoting player's offer.
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *      HTTP BAD_REQUEST otherwise.</p>
   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
   */
  @PutMapping(value = "/games/{sessionId}/cards/{cardMd5}")
  public ResponseEntity<String> buyCard(@PathVariable long sessionId, @PathVariable String cardMd5,
                                        @RequestParam String accessToken,
                                        @RequestBody OrientPurchaseMap purchaseMap)
      throws JsonProcessingException {
    return inventoryService.buyCard(sessionId, cardMd5, accessToken, purchaseMap);
  }


  /**
   * Gets the discounted price of the card with hash cardMd5  if it was to be bought by
   * player with accessToken inside game with sessionId.
   *
   * @param sessionId session identifier.
   * @param cardMd5 hash of card.
   * @param accessToken access token of player.
   * @return PriceMap of the discounted price.
   * @throws JsonProcessingException if the fails to write as a string.
   */
  @GetMapping(value = "/games/{sessionId}/cards/{cardMd5}/discountedPrice")
  public ResponseEntity<String> getDiscountedPrice(@PathVariable long sessionId,
                                                   @PathVariable String cardMd5,
                                                   @RequestParam String accessToken)
      throws JsonProcessingException {
    return inventoryService.getDiscountedPrice(sessionId, cardMd5, accessToken);
  }

  /**
   * Let the player reserve a face up card.
   *
   * @param sessionId   game session id.
   * @param cardMd5     card hash.
   * @param accessToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  @PutMapping(value = "/games/{sessionId}/cards/{cardMd5}/reservation")
  public ResponseEntity<String> reserveCard(@PathVariable long sessionId,
                                            @PathVariable String cardMd5,
                                            @RequestParam String accessToken)
      throws JsonProcessingException {
    return inventoryService.reserveCard(sessionId, cardMd5, accessToken);
  }

  /**
   * Let the player reserve a face down card.
   *
   * @param sessionId   game session id.
   * @param level       deck level.
   * @param accessToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  @PutMapping(value = "/games/{sessionId}/deck/reservation")
  public ResponseEntity<String> reserveFaceDownCard(@PathVariable long sessionId,
                                                    @RequestParam String level,
                                                    @RequestParam String accessToken)
      throws JsonProcessingException {

    return inventoryService.reserveFaceDownCard(sessionId, level, accessToken);
  }

  /**
   * Let the player claim a noble.
   *
   * @param sessionId    the session id
   * @param nobleMd5     the noble hash
   * @param accessToken  player's authentication token
   * @return HttpStatus.ok if the request completed, an error response otherwise.
   * @throws JsonProcessingException the json processing exception
   */
  @PutMapping(value = "/games/{sessionId}/nobles/{nobleMd5}")
  public ResponseEntity<String> claimNoble(@PathVariable long sessionId,
                                           @PathVariable String nobleMd5,
                                           @RequestParam String accessToken)
      throws JsonProcessingException {
    return inventoryService.acquireNoble(sessionId, nobleMd5, accessToken);
  }

  /**
   * Lets the player reserve a noble.
   *
   * @param sessionId session identifier.
   * @param nobleMd5 noble hash.
   * @param accessToken access token of requesting player.
   * @return next action that needs to be performed.
   * @throws JsonProcessingException if json processing fails.
   */
  @PostMapping(value = "/games/{sessionId}/nobles/{nobleMd5}")
  public ResponseEntity<String> reserveNoble(@PathVariable long sessionId,
                                             @PathVariable String nobleMd5,
                                             @RequestParam String accessToken)
      throws JsonProcessingException {
    return inventoryService.reserveNoble(sessionId, nobleMd5, accessToken);
  }

  /**
   * Takes a level two card.
   *
   * @param sessionId session Id.
   * @param accessToken auth token.
   * @param chosenCard chosen card's md5.
   * @return information on next action or invalid request message.
   * @throws JsonProcessingException exception if json processing fails.
   */
  @PutMapping(value = {"/games/{sessionId}/board/cards/levelTwo"})
  public ResponseEntity<String> takeLevelTwoCard(@PathVariable long sessionId,
                                                 @RequestParam String accessToken,
                                                 @RequestParam String chosenCard)
      throws JsonProcessingException {
    return inventoryService.takeLevelTwoCard(sessionId, accessToken, chosenCard);
  }

  /**
   * Takes a level one card.
   *
   * @param sessionId session Id.
   * @param accessToken auth token.
   * @param chosenCard chosen card's md5.
   * @return information on next action or invalid request message.
   * @throws JsonProcessingException exception if json processing fails.
   */
  @PutMapping(value = {"/games/{sessionId}/board/cards/levelOne"})
  public ResponseEntity<String> takeLevelOneCard(@PathVariable long sessionId,
                                                 @RequestParam String accessToken,
                                                 @RequestParam String chosenCard)
      throws JsonProcessingException {
    return inventoryService.takeLevelOneCard(sessionId, accessToken, chosenCard);
  }

  /**
   * Associates a bag card to a token type.
   *
   * @param sessionId session id.
   * @param accessToken access token.
   * @param tokenType chosen token type.
   * @return information on next action or invalid request message
   */
  @PutMapping(value = "/games/{sessionId}/cards/bagcards")
  public ResponseEntity<String> associateBagCard(@PathVariable long sessionId,
                                                 @RequestParam String accessToken,
                                                 @RequestParam String tokenType) {
    return inventoryService.associateBagCard(sessionId, accessToken, tokenType);
  }


}
