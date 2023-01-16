package com.hexanome16.server.controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.dto.PlayerJson;
import com.hexanome16.server.models.DevelopmentCard;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.PriceMap;
import com.hexanome16.server.models.PurchaseMap;
import com.hexanome16.server.models.TokenPrice;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.BroadcastMap;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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

  private final BroadcastMap broadcastContentManagerMap = new BroadcastMap();

  /**
   * A mapping from ID's to their associated games.
   */
  //store all the games here
  private final Map<Long, Game> gameMap = new HashMap<>();
  private final Map<String, DevelopmentCard> cardHashMap = new HashMap<>();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AuthServiceInterface authService;

  /**
   * Instantiates a new Game controller.
   *
   * @param authService the authentication service
   */
  public GameController(@Autowired AuthServiceInterface authService) {
    this.authService = authService;
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  }

  /**
   * Gets game map.
   *
   * @return the game map
   */
  public Map<Long, Game> getGameMap() {
    return gameMap;
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
    try {
      Player[] players = objectMapper.convertValue(payload.get("players"), Player[].class);
      String creator = objectMapper.convertValue(payload.get("creator"), String.class);
      String savegame = objectMapper.convertValue(payload.get("savegame"), String.class);
      Game game = new Game(sessionId, players, creator, savegame);
      gameMap.put(sessionId, game);
      BroadcastContentManager<DeckHash> broadcastContentManagerOne =
          new BroadcastContentManager<>(new DeckHash(gameMap.get(sessionId), Level.ONE));
      BroadcastContentManager<DeckHash> broadcastContentManagerTwo =
          new BroadcastContentManager<>(new DeckHash(gameMap.get(sessionId), Level.TWO));
      BroadcastContentManager<DeckHash> broadcastContentManagerThree =
          new BroadcastContentManager<>(new DeckHash(gameMap.get(sessionId), Level.THREE));
      BroadcastContentManager<PlayerJson> broadcastContentManagerPlayer =
          new BroadcastContentManager<>(
              new PlayerJson(gameMap.get(sessionId).getCurrentPlayer().getName()));
      BroadcastContentManager<NoblesHash> broadcastContentManagerNoble =
          new BroadcastContentManager<>(new NoblesHash(gameMap.get(sessionId)));
      broadcastContentManagerMap.put("ONE", broadcastContentManagerOne);
      broadcastContentManagerMap.put("TWO", broadcastContentManagerTwo);
      broadcastContentManagerMap.put("THREE", broadcastContentManagerThree);
      broadcastContentManagerMap.put("player", broadcastContentManagerPlayer);
      broadcastContentManagerMap.put("noble", broadcastContentManagerNoble);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "success";
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
    if (authService.verifyPlayer(sessionId, accessToken, gameMap)) {
      DeferredResult<ResponseEntity<String>> result;
      result =
          ResponseGenerator.getHashBasedUpdate(10000, broadcastContentManagerMap.get(level), hash);
      return result;
    }
    return null;
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
    if (authService.verifyPlayer(sessionId, accessToken, gameMap)) {
      DeferredResult<ResponseEntity<String>> result;
      result = ResponseGenerator.getHashBasedUpdate(10000,
          broadcastContentManagerMap.get("noble"), hash);
      return result;
    }
    return null;

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
    if (authService.verifyPlayer(sessionId, accessToken, gameMap)) {
      DeferredResult<ResponseEntity<String>> result;
      result = ResponseGenerator.getHashBasedUpdate(10000, broadcastContentManagerMap.get("player"),
          hash);
      return result;
    }
    return null;

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
    // session not found
    if (!gameMap.containsKey(sessionId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    // get player with username
    Player concernedPlayer = findPlayerByName(gameMap.get(sessionId), username);

    // Player not in game
    if (concernedPlayer == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    PurchaseMap playerBankMap = concernedPlayer.getBank().toPurchaseMap();

    return new ResponseEntity<>(objectMapper.writeValueAsString(playerBankMap), HttpStatus.OK);
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
    // session not found
    if (!gameMap.containsKey(sessionId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    PurchaseMap gameBankMap = gameMap.get(sessionId).getGameBank().toPurchaseMap();


    return new ResponseEntity<>(objectMapper.writeValueAsString(gameBankMap), HttpStatus.OK);
  }

  /**
   * Ends current player's turn and starts next player's turn.
   */
  public void endCurrentPlayersTurn(Game game) {
    game.setCurrentPlayerIndex((game.getCurrentPlayerIndex() + 1) % game.getPlayers().length);
    BroadcastContentManager<PlayerJson> broadcastContentManagerPlayer =
        (BroadcastContentManager<PlayerJson>) broadcastContentManagerMap.get("player");
    broadcastContentManagerPlayer.updateBroadcastContent(
        new PlayerJson(game.getCurrentPlayer().getName())
    );
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
   *      HTTP BAD_REQUEST otherwise.
   *      </p>
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

    //
    if (!gameMap.containsKey(sessionId) || !DeckHash.allCards.containsKey(cardMd5)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Verify player is who they claim to be
    if (!authService.verifyPlayer(sessionId, authenticationToken, gameMap)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Fetch the card in question
    DevelopmentCard cardToBuy = DeckHash.allCards.get(cardMd5);

    // Get game in question
    Game game = gameMap.get(sessionId);

    // Get proposed Deal as a purchase map
    PurchaseMap proposedDeal = new PurchaseMap(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount);

    // Get card price as a priceMap
    PriceMap cardPriceMap = ((TokenPrice) cardToBuy.getPrice()).getPriceMap();

    // Get player using found index
    Player clientPlayer = findPlayerByToken(game, authenticationToken);

    // Makes sure player is in game && proposed deal is acceptable && player has enough tokens
    if (clientPlayer == null
        || !proposedDeal.canBeUsedToBuy(PurchaseMap.toPurchaseMap(cardPriceMap))) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    System.out.println("PLAYER FOUND");
    System.out.println(clientPlayer.getName());


    // Last layer of sanity check, making sure player has enough funds to do the purchase.
    // and is player's turn
    if (!clientPlayer.hasAtLeast(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount)
        || !game.isPlayersTurn(clientPlayer)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // Increase Game Bank and decrease player funds
    game.incGameBankFromPlayer(clientPlayer, rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount);


    // Add that card to the player's Inventory
    clientPlayer.addCardToInventory(cardToBuy);

    // Remove card from the board
    game.removeOnBoardCard((LevelCard) cardToBuy);

    Level level = ((LevelCard) cardToBuy).getLevel();
    // Add new card to the deck
    game.addOnBoardCard(level);


    // Update long polling
    ((BroadcastContentManager<DeckHash>)
        (broadcastContentManagerMap.get(((LevelCard) cardToBuy).getLevel().name())))
        .updateBroadcastContent(new DeckHash(gameMap.get(sessionId), level));

    // Ends players turn, which is current player
    endCurrentPlayersTurn(game);

    return new ResponseEntity<>(HttpStatus.OK);
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////


  // HELPERS ///////////////////////////////////////////////////////////////////////////////////////

  /**
   * Finds a player in a game given their username.
   *
   * @param game     game where player is supposed to be.
   * @param username name of player.
   * @return Player with that username in that game, null if no such player.
   */
  public Player findPlayerByName(Game game, String username) {

    if (game == null) {
      return null;
    }
    for (Player e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }


  /**
   * Finds player with that authentication token in the game.
   *
   * @param game                game to search.
   * @param authenticationToken token associated to player
   * @return player with that token, null if no such player
   */
  public Player findPlayerByToken(Game game, String authenticationToken) {

    if (game == null) {
      return null;
    }
    ResponseEntity<String> usernameEntity = authService.getPlayer(authenticationToken);

    String username = usernameEntity.getBody();


    for (Player e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }

}


