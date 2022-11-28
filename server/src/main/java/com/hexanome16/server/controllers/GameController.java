package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.DevelopmentCard;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.PriceMap;
import com.hexanome16.server.models.PurchaseMap;
import com.hexanome16.server.models.TokenPrice;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Not implemented.
 */
@RestController
public class GameController {
  private static final Map<String, DevelopmentCard> cardHashMap =
      new HashMap<String, DevelopmentCard>();

  /**
   * Create a new game as client requested.
   *
   * @param sessionId sessionId
   * @return error if present
   */
  @PutMapping(value = {"/games/{sessionId}", "/games/{sessionId}/"})
  public String createGame(@PathVariable long sessionId) {
    try {
      Game game = new Game(sessionId);
      Game.getGameMap().put(sessionId, game);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "success";
  }

  /**
   * Return next card in a deck to client.
   *
   * @param sessionId sessionId
   * @param level     deck level
   * @return next card on board
   * @throws JsonProcessingException json exception
   */
  @GetMapping(value = {"/game/nextCard/{sessionId}", "/game/nextCard/{sessionId}/"})
  public String nextCard(@PathVariable long sessionId, @RequestParam String level)
      throws JsonProcessingException {
    Level alevel = null;
    switch (level) {
      default:
      case "ONE":
        alevel = Level.ONE;
        break;
      case "TWO":
        alevel = Level.TWO;
        break;
      case "THREE":
        alevel = Level.THREE;
        break;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(
        Game.getGameMap().get(sessionId).getDeck(alevel).nextCard());
  }

  /**
   * Return initial deck to client at the start of the game.
   *
   * @param sessionId sessionId
   * @param level     deck level
   * @return next card on board
   * @throws JsonProcessingException json exception
   */
  @GetMapping(value = {"/game/getCards/{sessionId}", "/game/getCards/{sessionId}/"})
  public String getCards(@PathVariable long sessionId, @RequestParam String level)
      throws JsonProcessingException {
    Level alevel = null;
    switch (level) {
      default:
      case "ONE":
        alevel = Level.ONE;
        break;
      case "TWO":
        alevel = Level.TWO;
        break;
      case "THREE":
        alevel = Level.THREE;
        break;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    //All hash is MD5 checksum, checkstyle won't let me use the word MD5!!!
    Map<String, DevelopmentCard> cardHash = new HashMap<String, DevelopmentCard>();
    for (DevelopmentCard card : Game.getGameMap().get(sessionId).getOnBoardDeck(alevel)
        .getCardList()) {
      //store in the class
      cardHashMap.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
      //store in the list we are going to send to the client
      cardHash.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
      System.out.println(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)));
    }
    System.out.println(objectMapper.writeValueAsString(cardHash));
    return objectMapper.writeValueAsString(cardHash);
  }

  @GetMapping(value = {"/game/{sessionId}/getNobles", "/game/{sessionId}/getNobles/"})
  public String getNobles(@PathVariable long sessionId)
      throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(Game.getGameMap().get(sessionId).getOnBoardNobles());
  }


  // Buy Prompt Controllers ////////////////////////////////////////////////////////////////////////

  //TODO : Find a way to send game bank to client
  @GetMapping(value = {"/game/{sessionId}/playerBank", "/game/{sessionId}/playerBank/"})
  public String getPlayerBankInfo(@PathVariable long sessionId, @RequestParam String username) {

    return "haiiii :3";
  }





  /**
   * Allows client to buy card, given that they send a valid way to buy that card.
   *
   * @param sessionId sessionID.
   * @param cardMd5 Card we want to purchase's md5.
   * @param username username of the player trying to buy the card.
   * @param rubyAmount amount of ruby gems proposed.
   * @param emeraldAmount amount of emerald gems proposed.
   * @param sapphireAmount amount of sapphire gems proposed.
   * @param diamondAmount amount of diamond gems proposed.
   * @param onyxAmount amount of onyx gems proposed.
   * @param goldAmount amount of gold gems proposed.
   * @return HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *         HTTP BAD_REQUEST otherwise.
   */
  @PutMapping(value = {"/game/{sessionId}/{cardMd5}", "/game/{sessionId}/{cardMd5}/"})
  public ResponseEntity<String> buyCard(@PathVariable long sessionId,
                                        @PathVariable String cardMd5,
                                        @RequestParam String username,
                                        @RequestParam int rubyAmount,
                                        @RequestParam int emeraldAmount,
                                        @RequestParam int sapphireAmount,
                                        @RequestParam int diamondAmount,
                                        @RequestParam int onyxAmount,
                                        @RequestParam int goldAmount) {


    if (!Game.getGameMap().containsKey(sessionId) || !cardHashMap.containsKey(cardMd5)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    ////////////////////////////////////////////////
    System.out.println("First Check Done");
    ////////////////////////////////////////////////






    // makes sure player is in game && proposed deal is acceptable && player has enough tokens
    ///////////////////////////////////////
    System.out.println("Inputed username : ");
    System.out.println(username);
    System.out.println("Available usernames : ");
    // fetch the card in question
    DevelopmentCard cardToBuy = cardHashMap.get(cardMd5);

    // get game in question
    Game game = Game.getGameMap().get(sessionId);
    for (Player e : game.getParticipants()) {
      System.out.println(e.getUsername());
    }
    System.out.println("Is offer acceptable : ");
    // get proposed Deal as a purchase map
    PurchaseMap proposedDeal = new PurchaseMap(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount);

    // get card price as a priceMap
    PriceMap cardPriceMap = ((TokenPrice) cardToBuy.getPrice()).getPriceMap();

    // get player using found index
    Player clientPlayer = findPlayer(game, username);
    System.out.println(proposedDeal.canBeUsedToBuy(PurchaseMap.toPurchaseMap(cardPriceMap)));
    ///////////////////////////////////////
    if (clientPlayer == null
        || !proposedDeal.canBeUsedToBuy(PurchaseMap.toPurchaseMap(cardPriceMap))) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    ////////////////////////////////////////
    System.out.println("Second Check Done");
    ////////////////////////////////////////

    // last layer of sanity check, making sure player has enough funds to do the purchase.
    // and is player's turn
    // TODO modify player turn stuff with what Costa did
    if (!clientPlayer.hasAtLeast(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount)
        || !game.isPlayersTurn(clientPlayer)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //////////////////////////////////////
    System.out.println("Last Check Done");
    //////////////////////////////////////


    // TODO: increase Game Bank (not for M5)

    // TODO: removes the tokens from the player's funds
    clientPlayer.incPlayerBank(-rubyAmount, -emeraldAmount,
        -sapphireAmount, -diamondAmount, -onyxAmount, -goldAmount);

    // TODO: add that card to the player's Inventory
    clientPlayer.addCardToInventory(cardToBuy);

    game.removeOnBoardCard((LevelCard) cardToBuy);

    // TODO: endPlayer's turn, Use Costa's work
    game.endCurrentPlayersTurn();


    ///////////////////////////////////////////////////////////////////////////////////////
    System.out.println("Buy Request Received, param. : ");
    System.out.println(sessionId);
    System.out.println(cardMd5);
    System.out.println(username);
    System.out.println(rubyAmount);
    System.out.println(emeraldAmount);
    System.out.println(sapphireAmount);
    System.out.println(diamondAmount);
    System.out.println(onyxAmount);
    System.out.println(goldAmount);
    /////////////////////////////////////////////////////////////////////////////////////////


    return new ResponseEntity<>(HttpStatus.OK);
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////



  // HELPERS ///////////////////////////////////////////////////////////////////////////////////////
  // finds player with username "username" in the game, returns null if no such player in game
  private Player findPlayer(Game game, String username) {
    for (Player e : game.getParticipants()) {
      if (e.getUsername().equals(username)) {
        return e;
      }
    }
    return null;
  }



}


