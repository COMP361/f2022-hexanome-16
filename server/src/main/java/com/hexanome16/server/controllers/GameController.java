package com.hexanome16.server.controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.lobbyservice.AuthController;
import com.hexanome16.server.models.Deck;
import com.hexanome16.server.models.DevelopmentCard;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.PriceMap;
import com.hexanome16.server.models.PurchaseMap;
import com.hexanome16.server.models.TokenPrice;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
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

  //store all the games here
  private static final Map<Long, Game> gameMap = new HashMap<>();
  private final Map<String, DevelopmentCard> cardHashMap = new HashMap<>();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AuthController authController;

  private final Map<String, BroadcastContentManager> broadcastContentManagerMap =
      new HashMap<String, BroadcastContentManager>();

  public GameController(AuthController authController) {
    this.authController = authController;
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  }

  public static Map<Long, Game> getGameMap() {
    return gameMap;
  }

  private Level getLevel(String level) {
    return switch (level) {
      case "ONE" -> Level.ONE;
      case "TWO" -> Level.TWO;
      case "THREE" -> Level.THREE;
      default -> throw new IllegalArgumentException("Invalid level");
    };
  }

  private boolean verifyPlayer(long sessionId, String accessToken) {
    Game game = gameMap.get(sessionId);
    if (game == null) {
      return false;
    }
    ResponseEntity<String> username = authController.getPlayer(accessToken);
    if (username.getStatusCode().is2xxSuccessful()) {
      return Arrays.stream(game.getPlayers())
          .anyMatch(player -> player.getName().equals(username.getBody()));
    }
    return false;
  }

  /**
   * Create a new game as client requested.
   *
   * @param sessionId sessionId
   * @return error if present
   */
  @PutMapping(value = {"/games/{sessionId}", "/games/{sessionId}/"})
  public String createGame(@PathVariable long sessionId, @RequestBody Map<String, Object> payload) {
    try {
      Player[] players = objectMapper.convertValue(payload.get("players"), Player[].class);
      String creator = objectMapper.convertValue(payload.get("creator"), String.class);
      String savegame = objectMapper.convertValue(payload.get("savegame"), String.class);
      Game game = new Game(sessionId, players, creator, savegame);
      gameMap.put(sessionId, game);
      BroadcastContentManager<Deck> broadcastContentManagerOne =
          new BroadcastContentManager<Deck>(game.getOnBoardDeck(Level.ONE));
      BroadcastContentManager<Deck> broadcastContentManagerTwo =
          new BroadcastContentManager<Deck>(game.getOnBoardDeck(Level.TWO));
      BroadcastContentManager<Deck> broadcastContentManagerThree =
          new BroadcastContentManager<Deck>(game.getOnBoardDeck(Level.THREE));
      broadcastContentManagerMap.put("ONE", broadcastContentManagerOne);
      broadcastContentManagerMap.put("TWO", broadcastContentManagerTwo);
      broadcastContentManagerMap.put("THREE", broadcastContentManagerThree);
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
  public String nextCard(@PathVariable long sessionId, @RequestParam String level,
                         @RequestParam String accessToken)
      throws JsonProcessingException {
    return verifyPlayer(sessionId, accessToken) ? objectMapper.writeValueAsString(
        gameMap.get(sessionId).getDeck(getLevel(level)).nextCard()) : null;
  }

  /**
   * Return initial deck to client at the start of the game.
   *
   * @param sessionId sessionId
   * @param level     deck level
   * @return next card on board
   * @throws JsonProcessingException json exception
   */
  @GetMapping(value = {"/game/{sessionId}/deck/init", "/game/{sessionId}/deck/init/"})
  public String getDeckInit(@PathVariable long sessionId, @RequestParam String level,
                            @RequestParam String accessToken)
      throws JsonProcessingException {
    if (verifyPlayer(sessionId, accessToken)) {
      //All hash is MD5 checksum, checkstyle won't let me use the word MD5!!!
      Map<String, DevelopmentCard> cardHash = new HashMap<>();
      for (DevelopmentCard card : gameMap.get(sessionId).getOnBoardDeck(getLevel(level))
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
    return null;
  }

  /**
   * Long polling on update on onboard deck.
   *
   * @param sessionId   game session id
   * @param level       deck level
   * @param accessToken account access token
   * @return updated game deck
   * @throws JsonProcessingException exception
   */
  @GetMapping(value = "/game/{sessionId}/deck", produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getDeck(@PathVariable long sessionId,
                                                        @RequestParam String level,
                                                        @RequestParam String accessToken)
      throws JsonProcessingException {
    if (verifyPlayer(sessionId, accessToken)) {

      Map<String, DevelopmentCard> cardHash = new HashMap<>();
      DeferredResult<ResponseEntity<String>> result;
      for (DevelopmentCard card : gameMap.get(sessionId).getOnBoardDeck(getLevel(level))
          .getCardList()) {
        //store in the class
        cardHashMap.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
        //store in the list we are going to send to the client
        cardHash.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
      }
      result = ResponseGenerator.getAsyncUpdate(10000, broadcastContentManagerMap.get(level));
      return result;
    }
    return null;
  }

  /**
   * Returns nobles present on the game board.
   *
   * @param sessionId   session id
   * @param accessToken access token
   * @return nobles present on the game board
   * @throws JsonProcessingException if json processing fails
   */
  @GetMapping(value = {"/game/{sessionId}/getNobles", "/game/{sessionId}/getNobles/"})
  public String getNobles(@PathVariable long sessionId, @RequestParam String accessToken)
      throws JsonProcessingException {
    return verifyPlayer(sessionId, accessToken)
        ? objectMapper.writeValueAsString(gameMap.get(sessionId).getOnBoardNobles())
        : null;
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
  @GetMapping(value = {"/game/{sessionId}/playerBank", "/game/{sessionId}/playerBank/"})
  public String getPlayerBankInfo(@PathVariable long sessionId,
                                  @RequestParam String username)
      throws JsonProcessingException {
    // session not found
    if (!gameMap.containsKey(sessionId)) {
      return null;
    }
    // get player with username
    Player concernedPlayer = findPlayer(gameMap.get(sessionId), username);

    // Player not in game
    if (concernedPlayer == null) {
      return null;
    }

    PurchaseMap playerBankMap = concernedPlayer.getBank().toPurchaseMap();

    return objectMapper.writeValueAsString(playerBankMap);
  }


  /**
   * Allows client to buy card, given that they send a valid way to buy that card.
   *
   * @param sessionId      sessionID.
   * @param cardMd5        Card we want to purchase's md5.
   * @param username       username of the player trying to buy the card.
   * @param rubyAmount     amount of ruby gems proposed.
   * @param emeraldAmount  amount of emerald gems proposed.
   * @param sapphireAmount amount of sapphire gems proposed.
   * @param diamondAmount  amount of diamond gems proposed.
   * @param onyxAmount     amount of onyx gems proposed.
   * @param goldAmount     amount of gold gems proposed.
   * @return HTTP OK if it's the player's turn and the proposed offer is acceptable,
   * HTTP BAD_REQUEST otherwise.
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


    if (!gameMap.containsKey(sessionId) || !cardHashMap.containsKey(cardMd5)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // fetch the card in question
    DevelopmentCard cardToBuy = cardHashMap.get(cardMd5);

    // get game in question
    Game game = gameMap.get(sessionId);

    // get proposed Deal as a purchase map
    PurchaseMap proposedDeal = new PurchaseMap(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount);

    // get card price as a priceMap
    PriceMap cardPriceMap = ((TokenPrice) cardToBuy.getPrice()).getPriceMap();

    // get player using found index
    Player clientPlayer = findPlayer(game, username);

    // makes sure player is in game && proposed deal is acceptable && player has enough tokens
    if (clientPlayer == null
        || !proposedDeal.canBeUsedToBuy(PurchaseMap.toPurchaseMap(cardPriceMap))) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // last layer of sanity check, making sure player has enough funds to do the purchase.
    // and is player's turn
    // kimi's part for first expression
    if (!clientPlayer.hasAtLeast(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount)
        || !game.isPlayersTurn(clientPlayer)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // TODO: increase Game Bank (not for M5)


    // kimi's part
    clientPlayer.incPlayerBank(-rubyAmount, -emeraldAmount,
        -sapphireAmount, -diamondAmount, -onyxAmount, -goldAmount);

    // TODO: add that card to the player's Inventory
    clientPlayer.addCardToInventory(cardToBuy);

    // remove card from the board
    game.removeOnBoardCard((LevelCard) cardToBuy);

    Level level = ((LevelCard) cardToBuy).getLevel();
    //add new card to the deck
    game.addOnBoardCard(level);

    //update long polling
    broadcastContentManagerMap.get(((LevelCard) cardToBuy).getLevel().name())
        .updateBroadcastContent(game.getOnBoardDeck(level));

    // ends players turn, which is current player
    game.endCurrentPlayersTurn();

    return new ResponseEntity<>(HttpStatus.OK);
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////


  // HELPERS ///////////////////////////////////////////////////////////////////////////////////////
  // finds player with username "username" in the game, returns null if no such player in game
  private Player findPlayer(Game game, String username) {
    for (Player e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }


}


