package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.action.generator.PurchaseActionGenerator;
import com.hexanome16.server.models.DevelopmentCard;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Not implemented.
 */
@RestController
public class GameController {
  private final PurchaseActionGenerator purchaseActionGenerator = new PurchaseActionGenerator();

  private final Map<String, DevelopmentCard> cardHashMap = new HashMap<String, DevelopmentCard>();

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

  /**
   * Client requests a list of possible buy actions for a certain card.
   *
   * @param sessionId game id
   * @param username player username
   * @param cardHash MD5 card
   * @return a hash string of actions
   * @throws JsonProcessingException exception
   */
  @GetMapping(value = {"/game/{sessionId}/{username}/actions/purchase"})
  public String getPurchaseActions(@PathVariable long sessionId, @PathVariable String username,
                                   @RequestParam String cardHash)
      throws JsonProcessingException {
    //player auth goes here I guess
    Game game = Game.getGameMap().get(sessionId);
    //to be implemented
    //Player player= game.getPlayerByUserame(username);
    Player player = new Player("");
    DevelopmentCard card = cardHashMap.get(cardHash);
    ObjectMapper objectMapper = new ObjectMapper();
    String serializedActions = objectMapper.writeValueAsString(
        purchaseActionGenerator.generateActions(game, player, (LevelCard) card));
    return serializedActions;
  }

  @PatchMapping(value = {"/game/{sessionId}/{username}/action/purchase"})
  public void selectPurchaseAction(@PathVariable long sessionId, @PathVariable String username,
                                   @RequestParam String actionHash) {

  }
}


