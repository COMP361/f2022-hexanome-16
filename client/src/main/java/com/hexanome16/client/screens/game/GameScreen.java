package com.hexanome16.client.screens.game;

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
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * GameScreen class spawns all the entities for game board.
 */
public class GameScreen {
  private static final Map<String, Map> level_one = new HashMap<String, Map>();
  private static final Map<String, Map> level_two = new HashMap<String, Map>();
  private static final Map<String, Map> level_three = new HashMap<String, Map>();

  private static final Map<String, Map> nobles = new HashMap<String, Map>();

  private static String levelOneDeckJson = "";
  private static String levelTwoDeckJson = "";
  private static String levelThreeDeckJson = "";

  private static String nobleJson = "";
  private static long sessionId = -1;

  private static Thread updateLevelOneDeck;
  private static Thread updateLevelTwoDeck;
  private static Thread updateLevelThreeDeck;

  private static Thread updateNobles;

  private static void fetchLevelOneDeckThread() {
    Task<Void> updateDeckTask = new Task<>() {
      @Override
      protected Void call() throws Exception {
        levelOneDeckJson =
            GameRequest.updateDeck(sessionId, Level.ONE, DigestUtils.md5Hex(levelOneDeckJson));
        return null;
      }
    };
    updateDeckTask.setOnSucceeded(e -> {
      updateLevelOneDeck = null;
      Platform.runLater(GameScreen::updateLevelOneDeck);
      fetchLevelOneDeckThread();
    });
    updateLevelOneDeck = new Thread(updateDeckTask);
    updateLevelOneDeck.setDaemon(true);
    updateLevelOneDeck.start();
  }

  private static void fetchLevelTwoDeckThread() {
    Task<Void> updateDeckTask = new Task<>() {
      @Override
      protected Void call() throws Exception {
        levelTwoDeckJson =
            GameRequest.updateDeck(sessionId, Level.TWO, DigestUtils.md5Hex(levelTwoDeckJson));
        return null;
      }
    };
    updateDeckTask.setOnSucceeded(e -> {
      updateLevelTwoDeck = null;
      Platform.runLater(GameScreen::updateLevelTwoDeck);
      fetchLevelTwoDeckThread();
    });
    updateLevelTwoDeck = new Thread(updateDeckTask);
    updateLevelTwoDeck.setDaemon(true);
    updateLevelTwoDeck.start();
  }

  private static void fetchLevelThreeDeckThread() {
    Task<Void> updateDeckTask = new Task<>() {
      @Override
      protected Void call() throws Exception {
        levelThreeDeckJson =
            GameRequest.updateDeck(sessionId, Level.THREE, DigestUtils.md5Hex(levelThreeDeckJson));
        return null;
      }
    };
    updateDeckTask.setOnSucceeded(e -> {
      updateLevelThreeDeck = null;
      Platform.runLater(GameScreen::updateLevelThreeDeck);
      fetchLevelThreeDeckThread();
    });
    updateLevelThreeDeck = new Thread(updateDeckTask);
    updateLevelThreeDeck.setDaemon(true);
    updateLevelThreeDeck.start();
  }

  private static void fetchNoblesThread() {
    Task<Void> updateDeckTask = new Task<>() {
      @Override
      protected Void call() throws Exception {
        nobleJson = GameRequest.updateNoble(sessionId, DigestUtils.md5Hex(nobleJson));
        return null;
      }
    };
    updateDeckTask.setOnSucceeded(e -> {
      updateNobles = null;
      Platform.runLater(GameScreen::updateNobles);
      fetchNoblesThread();
    });
    updateNobles = new Thread(updateDeckTask);
    updateNobles.setDaemon(true);
    updateNobles.start();
  }

  /**
   * Adds background, mat, cards, nobles, game bank,
   * player inventory and settings button to the game screen.
   *
   * @param id game id
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

    if (updateLevelOneDeck == null) {
      fetchLevelOneDeckThread();
    }
    if (updateLevelTwoDeck == null) {
      fetchLevelTwoDeckThread();
    }
    if (updateLevelThreeDeck == null) {
      fetchLevelThreeDeckThread();
    }
    if (updateNobles == null) {
      fetchNoblesThread();
    }
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
   * Updates on board nobles.
   */
  private static void updateNobles() {
    Gson gson = new Gson();
    Map<String, Object> deckHash = gson.fromJson(nobleJson, Map.class);
    Map<String, Object> nobleHashList = (Map<String, Object>) deckHash.get("nobles");
    for (Map.Entry<String, Object> entry : nobleHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>) entry.getValue();
      if (!nobles.containsKey(hash)) {
        nobles.put(hash, card);
        PriceMap pm = getPriceMap(card);
        FXGL.spawn("Noble",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
      }
    }
  }

  /**
   * Updates level three deck.
   */
  private static void updateLevelThreeDeck() {
    Gson gson = new Gson();
    Map<String, Object> deckHash = gson.fromJson(levelThreeDeckJson, Map.class);
    Map<String, Object> cardHashList = (Map<String, Object>) deckHash.get("cards");

    String hashToRemove = "";
    for (Map.Entry<String, Map> entry : level_three.entrySet()) {
      String hash = entry.getKey();
      if (!cardHashList.containsKey(hash)) {
        hashToRemove = hash;
        for (int i = 0; i < 4; i++) {
          if (hash.equals(CardComponent.level_three_grid[i].getCardHash())) {
            CardComponent.level_three_grid[i].removeFromMat();
          }
        }
      }
    }
    level_three.remove(hashToRemove);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>) entry.getValue();
      if (!level_three.containsKey(hash)) {
        level_three.put(hash, card);
        PriceMap pm = getPriceMap(card);
        FXGL.spawn("LevelThreeCard",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
      }
    }
  }

  /**
   * Updates level two deck.
   */
  private static void updateLevelTwoDeck() {
    Gson gson = new Gson();
    Map<String, Object> deckHash = gson.fromJson(levelTwoDeckJson, Map.class);
    Map<String, Object> cardHashList = (Map<String, Object>) deckHash.get("cards");

    String hashToRemove = "";
    for (Map.Entry<String, Map> entry : level_two.entrySet()) {
      String hash = entry.getKey();
      if (!cardHashList.containsKey(hash)) {
        hashToRemove = hash;
        for (int i = 0; i < 4; i++) {
          if (hash.equals(CardComponent.level_two_grid[i].getCardHash())) {
            CardComponent.level_two_grid[i].removeFromMat();
          }
        }
      }
    }
    level_two.remove(hashToRemove);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>) entry.getValue();
      if (!level_two.containsKey(hash)) {
        level_two.put(hash, card);
        PriceMap pm = getPriceMap(card);
        FXGL.spawn("LevelTwoCard",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
      }
    }
  }

  /**
   * Updates level one deck.
   */
  private static void updateLevelOneDeck() {
    Gson gson = new Gson();
    Map<String, Object> deckHash = gson.fromJson(levelOneDeckJson, Map.class);
    Map<String, Object> cardHashList = (Map<String, Object>) deckHash.get("cards");

    String hashToRemove = "";
    //check if remove
    for (Map.Entry<String, Map> entry : level_one.entrySet()) {
      String hash = entry.getKey();
      if (!cardHashList.containsKey(hash)) {
        hashToRemove = hash;
        for (int i = 0; i < 4; i++) {
          if (hash.equals(CardComponent.level_one_grid[i].getCardHash())) {
            CardComponent.level_one_grid[i].removeFromMat();
          }
        }
      }
    }
    level_one.remove(hashToRemove);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>) entry.getValue();
      if (!level_one.containsKey(hash)) {
        level_one.put(hash, card);
        PriceMap pm = getPriceMap(card);
        FXGL.spawn("LevelOneCard",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
      }
    }
  }

  /**
   * Resets every component and clears the game board when exit the game.
   */
  public static void exitGame() {
    level_one.clear();
    level_three.clear();
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
