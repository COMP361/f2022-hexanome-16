package com.hexanome16.client.screens.game;

import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.google.gson.Gson;
import com.hexanome16.client.requests.backend.cards.GameRequest;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.components.NobleComponent;
import com.hexanome16.client.screens.game.players.PlayerDecks;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BuyCardPrompt;
import com.hexanome16.client.screens.lobby.LobbyFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * GameScreen class spawns all the entities for game board.
 */
public class GameScreen {
  private String levelOneDeckJson;
  private static final Map<String, Map> level_one = new HashMap<String, Map>();
  private static final Map<String, Map> level_two = new HashMap<String, Map>();
  private static final Map<String, Map> level_three = new HashMap<String, Map>();

  private static long sessionId = -1;

  private static Thread updateDeck;

  private static void fetchLevelOneDeckThread() {
    Task<Void> updateDeckTask = new Task<>() {
      @Override
      protected Void call() throws Exception {
        deckJson = GameRequest.updateDeck(sessionId, Level.ONE);
        return null;
      }
    };
    updateDeckTask.setOnSucceeded(e -> {
      updateDeck = null;
      Platform.runLater(GameScreen::updateLevelOneDeck);
      fetchLevelOneDeckThread();
    });
    updateDeck = new Thread(updateDeckTask);
    updateDeck.setDaemon(true);
    updateDeck.start();
  }
  /**
   * Adds background, mat, cards, nobles, game bank,
   * player inventory and settings button to the game screen.
   */
  public static void initGame(long id) {
    initializeBankGameVars(id);

    sessionId = id;

    FXGL.spawn("Background");
    FXGL.spawn("Mat");
    FXGL.spawn("LevelOneDeck");
    FXGL.spawn("LevelTwoDeck");
    FXGL.spawn("LevelThreeDeck");
    FXGL.spawn("SacrificeCard");
    FXGL.spawn("NobleReserveCard");
    FXGL.spawn("BagCard");
    FXGL.spawn("TokenBank");
    FXGL.spawn("Setting");
    initLevelOneDeck();
    initLevelTwoDeck();
    initLevelThreeDeck();
    // spawn the player's hands
    PlayerDecks.generateAll(4);
  }

  // puts values necessary for game bank in the world properties
  private static void initializeBankGameVars(long id) {
    String gameBankString = PromptsRequests.getNewGameBankInfo(id);
    Map<CurrencyType, Integer> gameBankMap = BuyCardPrompt.toGemAmountMap(gameBankString);
    for (CurrencyType e : gameBankMap.keySet()) {
      FXGL.getWorldProperties().setValue(id + e.toString(), gameBankMap.get(e));
    }

  }

  /**
   * Initializes the number of cards in each deck.
   *
   * @param vars game variables
   */
  public static void initGameVars(Map<String, Object> vars) {
    vars.put("level_one_quantity", 40);
    vars.put("level_two_quantity", 30);
    vars.put("level_three_quantity", 20);
  }

  /**
   * Updates the number of cards in each deck.
   */
  public static void onUpdate() {
    PropertyMap state = FXGL.getWorldProperties();
    state.setValue("level_three_quantity", level_three.size());
    state.setValue("level_two_quantity", level_two.size());
    state.setValue("level_one_quantity", level_one.size());
  }

  /**
   * Adds a new level-three card to the game board.
   */
  private static void updateLevelThreeDeck() {
    String deckJson = GameRequest.updateDeck(sessionId, Level.THREE);
  }

  /**
   * Adds a new level-two card to the game board.
   */
  private static void updateLevelTwoDeck() {
    String deckJson = GameRequest.updateDeck(sessionId, Level.TWO);
  }

  /**
   * Adds a new level-one card to the game board.
   */
  private static void updateLevelOneDeck() {
    String deckJson = GameRequest.updateDeck(sessionId, Level.ONE);
    System.out.println("level one is updating");
    Gson gson = new Gson();
    Map<String, Object> deckHash = gson.fromJson(deckJson, Map.class);
    Map<String, Object> cardHashList = (Map<String, Object>)deckHash.get("cards");

    String hashToRemove = "";
    //check if remove
    for (Map.Entry<String, Map> entry : level_one.entrySet()) {
      String hash = entry.getKey();
      if(!cardHashList.containsKey(hash)) {
        hashToRemove = hash;
        for(int i = 0; i < 4; i++) {
          if (hash.equals(CardComponent.level_one_grid[i].getCardHash())) {
            CardComponent.level_one_grid[i].removeFromMat();
          }
        }
      }
    }
    level_one.remove(hashToRemove);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>)entry.getValue();
      if(!level_one.containsKey(hash)) {
        level_one.put(hash, card);
        PriceMap pm = getPriceMap(card);
        FXGL.spawn("LevelOneCard",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
      }
    }
    updateLevelOneDeck();
  }

  public static void initLevelOneDeck() {
    String deckJson = GameRequest.initDeck(sessionId, Level.ONE);
    Gson gson = new Gson();
    Map<String, Object> cardHashList = gson.fromJson(deckJson, Map.class);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>)entry.getValue();
      level_one.put(hash, card);
      PriceMap pm = getPriceMap(card);
      FXGL.spawn("LevelOneCard",
          new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
              .put("price", pm).put("MD5", hash));
    }
  }

  public static void initLevelTwoDeck() {
    String deckJson = GameRequest.initDeck(sessionId, Level.TWO);
    Gson gson = new Gson();
    Map<String, Object> cardHashList = gson.fromJson(deckJson, Map.class);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>)entry.getValue();
      level_two.put(hash,card);
        PriceMap pm = getPriceMap(card);
        FXGL.spawn("LevelTwoCard",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
    }
  }

  public static void initLevelThreeDeck() {
    String deckJson = GameRequest.initDeck(sessionId, Level.THREE);
    Gson gson = new Gson();
    Map<String, Object> cardHashList = gson.fromJson(deckJson, Map.class);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>)entry.getValue();
      level_three.put(hash, card);
        PriceMap pm = getPriceMap(card);
        FXGL.spawn("LevelThreeCard",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
    }
  }

  /**
   * Resets every component and clears the game board when exit the game.
   */
  public static void exitGame() {
    level_one.clear();
    level_two.clear();
    level_three.clear();
    CardComponent.reset();
    NobleComponent.reset();
    FXGL.getGameWorld()
        .removeEntities(FXGL.getGameWorld().getEntitiesByComponent(CardComponent.class));
    FXGL.getGameWorld()
        .removeEntities(FXGL.getGameWorld().getEntitiesByComponent(ViewComponent.class));
  }

  private static PriceMap getPriceMap(Map<String, Object> card) {
    Map<String, Object> priceMap = (Map) ((Map) card.get("price")).get("priceMap");
    PriceMap pm = new PriceMap(((Double) priceMap.get("rubyAmount")).intValue(),
        ((Double) priceMap.get("emeraldAmount")).intValue(),
        ((Double) priceMap.get("sapphireAmount")).intValue(),
        ((Double) priceMap.get("diamondAmount")).intValue(),
        ((Double) priceMap.get("onyxAmount")).intValue());
    return pm;
  }

  /**
   * Returns session Id.
   *
   * @return returns session Id.
   */
  public static long getSessionId() {
    return sessionId;
  }
}
