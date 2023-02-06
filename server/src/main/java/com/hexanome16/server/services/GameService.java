package com.hexanome16.server.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.dto.PlayerJson;
import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.dto.WinJson;
import com.hexanome16.server.models.CardInfo;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.InventoryAddable;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.PriceMap;
import com.hexanome16.server.models.PurchaseMap;
import com.hexanome16.server.models.Reservable;
import com.hexanome16.server.models.TokenPrice;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.BroadcastMap;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Service is responsible for managing game state requests from
 * the {@link com.hexanome16.server.controllers.GameController}.
 */
@Service
public class GameService implements GameServiceInterface {
  private final BroadcastMap broadcastContentManagerMap;
  /**
   * A mapping from ID's to their associated games.
   */
  //store all the games here
  private final Map<Long, Game> gameMap = new HashMap<>();
  private final Map<String, CardInfo> cardHashMap = new HashMap<>();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AuthServiceInterface authService;

  /**
   * Instantiates the game service.
   *
   * @param authService the authentication service used to validate requests
   * @param broadcastContentManagerMap the broadcast content manager map used to manage
   */
  public GameService(@Autowired AuthServiceInterface authService, @Autowired BroadcastMap
      broadcastContentManagerMap) {
    this.authService = authService;
    this.broadcastContentManagerMap = broadcastContentManagerMap;
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  }

  @Override
  public Map<Long, Game> getGameMap() {
    return gameMap;
  }

  //TODO: probably need to make a better test for this.

  @Override
  public String createGame(long sessionId, SessionJson payload) {
    try {
      Game game = new Game(sessionId, payload);
      gameMap.put(sessionId, game);
      for (Level level : Level.values()) {
        BroadcastContentManager<DeckHash> broadcastContentManager =
            new BroadcastContentManager<>(new DeckHash(gameMap.get(sessionId), level));
        broadcastContentManagerMap.put(level.name(), broadcastContentManager);
      }
      BroadcastContentManager<PlayerJson> broadcastContentManagerPlayer =
          new BroadcastContentManager<>(
              new PlayerJson(gameMap.get(sessionId).getCurrentPlayer().getName()));
      BroadcastContentManager<WinJson> broadcastContentManagerWinners =
          new BroadcastContentManager<>(new WinJson());
      BroadcastContentManager<NoblesHash> broadcastContentManagerNoble =
          new BroadcastContentManager<>(new NoblesHash(gameMap.get(sessionId)));
      broadcastContentManagerMap.put("player", broadcastContentManagerPlayer);
      broadcastContentManagerMap.put("winners", broadcastContentManagerWinners);
      broadcastContentManagerMap.put("noble", broadcastContentManagerNoble);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "success";
  }


  @Override
  public DeferredResult<ResponseEntity<String>> getDeck(long sessionId, String level,
                                                        String accessToken, String hash) {
    if (authService.verifyPlayer(sessionId, accessToken, gameMap)) {
      return ResponseGenerator.getHashBasedUpdate(10000, broadcastContentManagerMap.get(level),
          hash);
    }
    return null;
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getNobles(long sessionId, String accessToken,
                                                          String hash) {
    if (authService.verifyPlayer(sessionId, accessToken, getGameMap())) {
      DeferredResult<ResponseEntity<String>> result;
      result = ResponseGenerator.getHashBasedUpdate(10000, broadcastContentManagerMap.get("noble"),
          hash);
      return result;
    }
    return null;

  }

  @Override
  public DeferredResult<ResponseEntity<String>> getCurrentPlayer(long sessionId, String accessToken,
                                                                 String hash) {
    if (authService.verifyPlayer(sessionId, accessToken, gameMap)) {
      DeferredResult<ResponseEntity<String>> result;
      result = ResponseGenerator.getHashBasedUpdate(10000, broadcastContentManagerMap.get("player"),
          hash);
      return result;
    }
    return null;

  }

  @Override
  public DeferredResult<ResponseEntity<String>> getWinners(long sessionId, String accessToken,
                                                                 String hash) {
    if (authService.verifyPlayer(sessionId, accessToken, gameMap)) {
      DeferredResult<ResponseEntity<String>> result;
      result = ResponseGenerator.getHashBasedUpdate(
          10000, broadcastContentManagerMap.get("winners"), hash
      );
      return result;
    }
    return null;

  }

  @Override
  public ResponseEntity<String> getPlayerBankInfo(long sessionId, String username)
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

  @Override
  public ResponseEntity<String> getGameBankInfo(long sessionId) throws JsonProcessingException {
    // session not found
    if (!gameMap.containsKey(sessionId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    PurchaseMap gameBankMap = gameMap.get(sessionId).getGameBank().toPurchaseMap();


    return new ResponseEntity<>(objectMapper.writeValueAsString(gameBankMap), HttpStatus.OK);
  }

  @Override
  public void endCurrentPlayersTurn(Game game) {
    int nextPlayerIndex = (game.getCurrentPlayerIndex() + 1) % game.getPlayers().length;
    game.setCurrentPlayerIndex(nextPlayerIndex);
    if (nextPlayerIndex == 0) {
      Player[] winners = game.getWinCondition().isGameWon(game);
      if (winners.length > 0) {
        BroadcastContentManager<WinJson> broadcastContentManagerWinners =
            (BroadcastContentManager<WinJson>) broadcastContentManagerMap.get("winners");
        broadcastContentManagerWinners.updateBroadcastContent(new WinJson(
            Arrays.stream(winners).map(Player::getName).toArray(String[]::new)
        ));
      }
    } else {
      BroadcastContentManager<PlayerJson> broadcastContentManagerPlayer =
          (BroadcastContentManager<PlayerJson>) broadcastContentManagerMap.get("player");
      broadcastContentManagerPlayer.updateBroadcastContent(
          new PlayerJson(game.getCurrentPlayer().getName()));
    }
  }

  @Override
  public ResponseEntity<String> buyCard(long sessionId, String cardMd5, String authenticationToken,
                                        int rubyAmount, int emeraldAmount, int sapphireAmount,
                                        int diamondAmount, int onyxAmount, int goldAmount)
      throws JsonProcessingException {


    // Fetch the card in question
    InventoryAddable cardToBuy = DeckHash.getCardFromAllCards(cardMd5);

    //
    if (!gameMap.containsKey(sessionId) || cardToBuy == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Verify player is who they claim to be
    if (!authService.verifyPlayer(sessionId, authenticationToken, gameMap)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get game in question
    Game game = gameMap.get(sessionId);

    // Get proposed Deal as a purchase map
    PurchaseMap proposedDeal =
        new PurchaseMap(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount, onyxAmount,
            goldAmount);

    // Get card price as a priceMap
    PriceMap cardPriceMap = ((TokenPrice) cardToBuy.getCardInfo().price()).getPriceMap();

    // Get player using found index
    Player clientPlayer = findPlayerByToken(game, authenticationToken);

    // Makes sure player is in game && proposed deal is acceptable && player has enough tokens
    if (clientPlayer == null
        || !proposedDeal.canBeUsedToBuy(PurchaseMap.toPurchaseMap(cardPriceMap))) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



    // Last layer of sanity check, making sure player has enough funds to do the purchase.
    // and is player's turn
    if (!clientPlayer.hasAtLeast(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount,
        onyxAmount, goldAmount) || game.isNotPlayersTurn(clientPlayer)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // Increase Game Bank and decrease player funds
    game.incGameBankFromPlayer(clientPlayer, rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount);


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

  /**
   * Let the player reserve a face up card.
   *
   * @param sessionId game session id.
   * @param cardMd5 card hash.
   * @param authenticationToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws JsonProcessingException exception
   */
  public ResponseEntity<String> reserveCard(@PathVariable long sessionId,
                                            @PathVariable String cardMd5,
                                            @RequestParam String authenticationToken)
      throws JsonProcessingException {

    InventoryAddable card = DeckHash.getCardFromAllCards(cardMd5);

    //verify game and player
    if (!gameMap.containsKey(sessionId) || card == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    if (!authService.verifyPlayer(sessionId, authenticationToken, gameMap)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Game game = gameMap.get(sessionId);


    Player player = findPlayerByToken(game, authenticationToken);

    if (game.isNotPlayersTurn(player)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    if (card instanceof Reservable && !player.reserveCard((Reservable) card)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // give player a gold token
    game.incGameBankFromPlayer(player, 0, 0, 0, 0, 0, -1);

    // replace this card with a new one on board
    game.removeOnBoardCard((LevelCard) card);
    Level level = ((LevelCard) card).getLevel();
    game.addOnBoardCard(level);

    // Notify long polling
    ((BroadcastContentManager<DeckHash>)
        (broadcastContentManagerMap.get(((LevelCard) card).getLevel().name())))
        .updateBroadcastContent(new DeckHash(gameMap.get(sessionId), level));

    endCurrentPlayersTurn(game);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Let the player reserve a face down card.
   *
   * @param sessionId game session id.
   * @param level deck level.
   * @param authenticationToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws JsonProcessingException exception
   */
  public ResponseEntity<String> reserveFaceDownCard(@PathVariable long sessionId,
                                                    @RequestParam String level,
                                                    @RequestParam String authenticationToken)
      throws JsonProcessingException {
    //verify game and player
    if (!gameMap.containsKey(sessionId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    if (!authService.verifyPlayer(sessionId, authenticationToken, gameMap)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Game game = gameMap.get(sessionId);

    Level atLevel;

    switch (level) {
      case "THREE": atLevel = Level.THREE;
        break;
      case "TWO": atLevel = Level.TWO;
        break;
      default: atLevel = Level.ONE;
    }

    InventoryAddable card = game.getLevelDeck(atLevel).nextCard();

    Player player = findPlayerByToken(game, authenticationToken);

    if (game.isNotPlayersTurn(player)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    if (card instanceof Reservable && !player.reserveCard((Reservable) card)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // give player a gold token
    game.incGameBankFromPlayer(player, 0, 0, 0, 0, 0, -1);

    endCurrentPlayersTurn(game);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public Player findPlayerByName(@NonNull Game game, String username) {
    for (Player e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }

  @Override
  public Player findPlayerByToken(@NonNull Game game, String accessToken) {
    ResponseEntity<String> usernameEntity = authService.getPlayer(accessToken);

    String username = usernameEntity.getBody();


    for (Player e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }
}
