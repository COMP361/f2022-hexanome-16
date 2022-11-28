package com.hexanome16.client.screens.game;

import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.google.gson.Gson;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.components.NobleComponent;
import com.hexanome16.client.screens.game.players.PlayerDecks;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * GameScreen class spawns all the entities for game board.
 */
public class GameScreen {
  private static final Stack<Integer> level_one = new Stack<>();
  private static final Stack<Integer> level_two = new Stack<>();
  private static final Stack<Integer> level_three = new Stack<>();
  private static final Stack<Integer> nobles = new Stack<>();
  //for demo use only: lists of cards returned by the server
  private static long sessionId = -1;

  /**
   * Adds background, mat, cards, nobles, game bank,
   * player inventory and settings button to the game screen.
   */
  public static void initGame(long id) {
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
    PlayerDecks.generateAll();
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
  public static void addLevelThreeCard() {
    String cardJson = GameRequest.newCard(sessionId, Level.THREE);
    Gson gson = new Gson();

    // convert JSON file to map
    Map<String, Object> map = gson.fromJson(cardJson, Map.class);
    PriceMap pm = getPriceMap(map);

    FXGL.spawn("LevelThreeCard",
        new SpawnData().put("id", map.get("id")).put("texture", map.get("texturePath")));
  }

  /**
   * Adds a new level-two card to the game board.
   */
  public static void addLevelTwoCard() {
    String cardJson = GameRequest.newCard(sessionId, Level.TWO);
    Gson gson = new Gson();

    // convert JSON file to map
    Map<String, Object> map = gson.fromJson(cardJson, Map.class);
    PriceMap pm = getPriceMap(map);

    FXGL.spawn("LevelTwoCard",
        new SpawnData().put("id", map.get("id")).put("texture", map.get("texturePath")));
  }

  /**
   * Adds a new level-one card to the game board.
   */
  public static void addLevelOneCard() {
    String cardJson = GameRequest.newCard(sessionId, Level.ONE);
    Gson gson = new Gson();

    // convert JSON file to map
    Map<String, Object> map = gson.fromJson(cardJson, Map.class);
    PriceMap pm = getPriceMap(map);
    FXGL.spawn("LevelOneCard",
        new SpawnData().put("id", map.get("id")).put("texture", map.get("texturePath"))
            .put("price", pm));
  }

  public static void initLevelOneDeck() {
    String cardJson = GameRequest.initDeck(sessionId, Level.ONE);
    Gson gson = new Gson();
    Map<String, Object> cardHashList = gson.fromJson(cardJson, Map.class);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>)entry.getValue();
      PriceMap pm = getPriceMap(card);
      FXGL.spawn("LevelOneCard",
          new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
              .put("price", pm).put("MD5", hash));
    }
  }

  public static void initLevelTwoDeck() {
    String cardJson = GameRequest.initDeck(sessionId, Level.TWO);
    Gson gson = new Gson();
    Map<String, Object> cardHashList = gson.fromJson(cardJson, Map.class);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>)entry.getValue();
        PriceMap pm = getPriceMap(card);
        FXGL.spawn("LevelTwoCard",
            new SpawnData().put("id", card.get("id")).put("texture", card.get("texturePath"))
                .put("price", pm).put("MD5", hash));
    }
  }

  public static void initLevelThreeDeck() {
    String cardJson = GameRequest.initDeck(sessionId, Level.THREE);
    Gson gson = new Gson();
    Map<String, Object> cardHashList = gson.fromJson(cardJson, Map.class);
    for (Map.Entry<String, Object> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      Map<String, Object> card = (Map<String, Object>)entry.getValue();
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
