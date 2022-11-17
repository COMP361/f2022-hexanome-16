package com.hexanome16.client.screens.game;

import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.components.NobleComponent;
import com.hexanome16.client.screens.game.players.PlayerDecks;
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
  private static final int[] level_one_list = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
      16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
      39};
  private static final int[] level_two_list = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
      16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};
  private static final int[] level_three_list = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
      15, 16, 17, 18, 19};
  private static final int[] noble_list = {0, 1, 2, 3, 4};

  /**
   * Adds background, mat, cards, nobles, game bank,
   * player inventory and settings button to the game screen.
   */
  public static void initGame() {
    for (int i = 0; i < level_one_list.length; i++) {
      level_one.push(level_one_list[i]);
    }
    for (int i = 0; i < level_two_list.length; i++) {
      level_two.push(level_two_list[i]);
    }
    for (int i = 0; i < level_three_list.length; i++) {
      level_three.push(level_three_list[i]);
    }
    for (int i = 0; i < noble_list.length; i++) {
      nobles.push(noble_list[i]);
    }
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
    for (int i = 0; i < 4; i++) {
      addLevelOneCard();
      addLevelTwoCard();
      addLevelThreeCard();
    }
    for (int i = 0; i < 5; i++) {
      FXGL.spawn("Noble", new SpawnData().put("nobleIndex", nobles.pop()));
    }
    // spawn the player's hands
    PlayerDecks.generateAll();
  }

  /**
   * Initializes the number of cards in each deck.

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
    if (!level_three.empty()) {
      FXGL.spawn("LevelThreeCard", new SpawnData().put("cardIndex", level_three.pop()));
    }
  }

  /**
   * Adds a new level-two card to the game board.
   */
  public static void addLevelTwoCard() {
    if (!level_two.empty()) {
      FXGL.spawn("LevelTwoCard", new SpawnData().put("cardIndex", level_two.pop()));
    }
  }

  /**
   * Adds a new level-one card to the game board.
   */
  public static void addLevelOneCard() {
    if (!level_one.empty()) {
      FXGL.spawn("LevelOneCard", new SpawnData().put("cardIndex", level_one.pop()));
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

}
