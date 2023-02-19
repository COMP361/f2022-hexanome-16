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
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import models.Level;
import models.price.PriceMap;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * GameScreen class spawns all the entities for game board.
 */
public class GameScreen {
  private static final Map<String, Map> level_one = new HashMap<String, Map>();
  private static final Map<String, Map> level_two = new HashMap<String, Map>();
  private static final Map<String, Map> level_three = new HashMap<String, Map>();

  private static final Map<String, Map> red_level_one = new HashMap<String, Map>();
  private static final Map<String, Map> red_level_two = new HashMap<String, Map>();
  private static final Map<String, Map> red_level_three = new HashMap<String, Map>();

  private static final Map<String, Map> nobles = new HashMap<String, Map>();

  private static String levelOneDeckJson = "";
  private static String levelTwoDeckJson = "";
  private static String levelThreeDeckJson = "";

  private static String redLevelOneDeckJson = "";
  private static String redLevelTwoDeckJson = "";
  private static String redLevelThreeDeckJson = "";

  private static String nobleJson = "";

  private static String currentPlayerJson = "";

  private static long sessionId = -1;

  private static Thread updateLevelOneDeck;
  private static Thread updateLevelTwoDeck;
  private static Thread updateLevelThreeDeck;

  private static Thread updateRedLevelOneDeck;
  private static Thread updateRedLevelTwoDeck;
  private static Thread updateRedLevelThreeDeck;

  private static Thread updateNobles;

  private static Thread updateCurrentPlayer;

  private static String[] usernames;

  private static Gson myGS = new Gson();
  private static Map<String, Object> myNames;
  private static String currentPlayer;

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

  private static void fetchRedLevelOneDeckThread() {
    Task<Void> updateDeckTask = new Task<>() {
      @Override
      protected Void call() throws Exception {
        redLevelOneDeckJson =
            GameRequest.updateDeck(
                sessionId, Level.REDONE, DigestUtils.md5Hex(redLevelOneDeckJson)
            );
        return null;
      }
    };
    updateDeckTask.setOnSucceeded(e -> {
      updateRedLevelOneDeck = null;
      Platform.runLater(GameScreen::updateRedLevelOneDeck);
      fetchRedLevelOneDeckThread();
    });
    updateRedLevelOneDeck = new Thread(updateDeckTask);
    updateRedLevelOneDeck.setDaemon(true);
    updateRedLevelOneDeck.start();
  }

  private static void fetchRedLevelTwoDeckThread() {
    Task<Void> updateDeckTask = new Task<>() {
      @Override
      protected Void call() throws Exception {
        redLevelTwoDeckJson =
            GameRequest.updateDeck(
                sessionId, Level.REDTWO, DigestUtils.md5Hex(redLevelTwoDeckJson)
            );
        return null;
      }
    };
    updateDeckTask.setOnSucceeded(e -> {
      updateRedLevelTwoDeck = null;
      Platform.runLater(GameScreen::updateRedLevelTwoDeck);
      fetchRedLevelTwoDeckThread();
    });
    updateRedLevelTwoDeck = new Thread(updateDeckTask);
    updateRedLevelTwoDeck.setDaemon(true);
    updateRedLevelTwoDeck.start();
  }

  private static void fetchRedLevelThreeDeckThread() {
    Task<Void> updateDeckTask = new Task<>() {
      @Override
      protected Void call() throws Exception {
        redLevelThreeDeckJson =
            GameRequest.updateDeck(
                sessionId, Level.REDTHREE, DigestUtils.md5Hex(redLevelThreeDeckJson)
            );
        return null;
      }
    };
    updateDeckTask.setOnSucceeded(e -> {
      updateRedLevelThreeDeck = null;
      Platform.runLater(GameScreen::updateRedLevelThreeDeck);
      fetchRedLevelThreeDeckThread();
    });
    updateRedLevelThreeDeck = new Thread(updateDeckTask);
    updateRedLevelThreeDeck.setDaemon(true);
    updateRedLevelThreeDeck.start();
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


  private static void fetchCurrentPlayerThread() {
    Task<Void> updateCurrentPlayerTask = new Task<>() {
      @Override
      protected Void call() throws Exception {
        currentPlayerJson =
            GameRequest.updateCurrentPlayer(sessionId, DigestUtils.md5Hex(currentPlayerJson));
        return null;
      }
    };

    updateCurrentPlayerTask.setOnSucceeded(e -> {
      updateCurrentPlayer = null;

      Platform.runLater(() -> {
        myNames = myGS.fromJson(currentPlayerJson, Map.class);
        currentPlayer = (String) myNames.get("username");
        UpdateGameInfo.fetchGameBank(getSessionId());
        UpdateGameInfo.fetchAllPlayer(getSessionId(), usernames);
        UpdateGameInfo.setCurrentPlayer(getSessionId(), currentPlayer);
      });

      fetchCurrentPlayerThread();
    });
    updateCurrentPlayer = new Thread(updateCurrentPlayerTask);
    updateCurrentPlayer.setDaemon(true);
    updateCurrentPlayer.start();
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
    FXGL.spawn("RedLevelOneDeck");
    FXGL.spawn("RedLevelTwoDeck");
    FXGL.spawn("RedLevelThreeDeck");
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
    if (updateRedLevelOneDeck == null) {
      fetchRedLevelOneDeckThread();
    }
    if (updateRedLevelTwoDeck == null) {
      fetchRedLevelTwoDeckThread();
    }
    if (updateRedLevelThreeDeck == null) {
      fetchRedLevelThreeDeckThread();
    }
    if (updateNobles == null) {
      fetchNoblesThread();
    }
    if (updateCurrentPlayer == null) {
      fetchCurrentPlayerThread();
    }
    UpdateGameInfo.initPlayerTurn();
    usernames = FXGL.getWorldProperties().getValue("players");
    UpdateGameInfo.fetchAllPlayer(getSessionId(), usernames);
    // spawn the player's hands
    PlayerDecks.generateAll(usernames);
  }

  // puts values necessary for game bank in the world properties
  private static void initializeBankGameVars(long id) {
    String gameBankString = PromptsRequests.getGameBankInfo(id);
    Map<CurrencyType, Integer> gameBankMap = UpdateGameInfo.toGemAmountMap(gameBankString);
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
   * Updates red level one deck.
   */
  private static void updateRedLevelOneDeck() {
    Gson gson = new Gson();
    Map<String, Object> deckHash = gson.fromJson(redLevelOneDeckJson, Map.class);
    Map<String, Object> cardHashList = (Map<String, Object>) deckHash.get("cards");

    String hashToRemove = "";
    //check if remove
    for (Map.Entry<String, Map> entry : red_level_one.entrySet()) {
      String hash = entry.getKey();
      if (!cardHashList.containsKey(hash)) {
        hashToRemove = hash;
        for (int i = 0; i < 4; i++) {
          if (hash.equals(CardComponent.red_level_one_grid[i].getCardHash())) {
            CardComponent.red_level_one_grid[i].removeFromMat();
          }
        }
      }
    }
    red_level_one.remove(hashToRemove);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>) entry.getValue();
      if (!red_level_one.containsKey(hash)) {
        red_level_one.put(hash, card);
        PriceMap pm = getPriceMap(card);
        FXGL.spawn("RedLevelOneCard",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
      }
    }
  }

  /**
   * Updates red level two deck.
   */
  private static void updateRedLevelTwoDeck() {
    Gson gson = new Gson();
    Map<String, Object> deckHash = gson.fromJson(redLevelTwoDeckJson, Map.class);
    Map<String, Object> cardHashList = (Map<String, Object>) deckHash.get("cards");

    String hashToRemove = "";
    //check if remove
    for (Map.Entry<String, Map> entry : red_level_two.entrySet()) {
      String hash = entry.getKey();
      if (!cardHashList.containsKey(hash)) {
        hashToRemove = hash;
        for (int i = 0; i < 4; i++) {
          if (hash.equals(CardComponent.red_level_two_grid[i].getCardHash())) {
            CardComponent.red_level_two_grid[i].removeFromMat();
          }
        }
      }
    }
    red_level_two.remove(hashToRemove);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>) entry.getValue();
      if (!red_level_two.containsKey(hash)) {
        red_level_two.put(hash, card);
        PriceMap pm = getPriceMap(card);
        FXGL.spawn("RedLevelTwoCard",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
      }
    }
  }

  /**
   * Updates red level three deck.
   */
  private static void updateRedLevelThreeDeck() {
    Gson gson = new Gson();
    Map<String, Object> deckHash = gson.fromJson(redLevelThreeDeckJson, Map.class);
    Map<String, Object> cardHashList = (Map<String, Object>) deckHash.get("cards");

    String hashToRemove = "";
    //check if remove
    for (Map.Entry<String, Map> entry : red_level_three.entrySet()) {
      String hash = entry.getKey();
      if (!cardHashList.containsKey(hash)) {
        hashToRemove = hash;
        for (int i = 0; i < 4; i++) {
          if (hash.equals(CardComponent.red_level_three_grid[i].getCardHash())) {
            CardComponent.red_level_three_grid[i].removeFromMat();
          }
        }
      }
    }
    red_level_three.remove(hashToRemove);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>) entry.getValue();
      if (!red_level_three.containsKey(hash)) {
        red_level_three.put(hash, card);
        PriceMap pm = new PriceMap(); // need to be dealt with later
        FXGL.spawn("RedLevelThreeCard",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
      }
    }
  }

  /**
   * Resets every component and clears the game board when exit the game.
   */
  public static void exitGame() {
    levelOneDeckJson = "";
    levelTwoDeckJson = "";
    levelThreeDeckJson = "";
    redLevelOneDeckJson = "";
    redLevelTwoDeckJson = "";
    redLevelThreeDeckJson = "";
    nobleJson = "";
    currentPlayerJson = "";
    updateLevelOneDeck = null;
    updateLevelTwoDeck = null;
    updateLevelThreeDeck = null;
    updateRedLevelOneDeck = null;
    updateRedLevelTwoDeck = null;
    updateRedLevelThreeDeck = null;
    updateNobles = null;
    updateCurrentPlayer = null;
    level_one.clear();
    level_two.clear();
    level_three.clear();
    red_level_one.clear();
    red_level_two.clear();
    red_level_three.clear();
    nobles.clear();
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
