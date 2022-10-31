package com.hexanome16.screens.game;

import static com.almasb.fxgl.dsl.FXGLForKtKt.addUINode;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getip;

import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.screens.game.prompts.actualyUI.PromptPartFactory;
import java.util.Map;
import java.util.Stack;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameScreen {
  private static Stack<Integer> level_one = new Stack<>();
  private static Stack<Integer> level_two = new Stack<>();
  private static Stack<Integer> level_three = new Stack<>();

  private static Stack<Integer> nobles = new Stack<>();

  //for demo use only: lists of cards returned by the server
  private static int[] level_one_list =
      {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
          26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39};
  private static int[] level_two_list =
      {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
          26, 27, 28, 29};
  private static int[] level_three_list =
      {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
  private static int[] noble_list = {0, 1, 2, 3, 4};

  public static void initGame() {
    FXGL.getGameWorld().addEntityFactory(new GameFactory());
    FXGL.getGameWorld().addEntityFactory(new PromptPartFactory());
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
    for (int i = 0; i < 4; i++) {
      addLevelOneCard();
      addLevelTwoCard();
      addLevelThreeCard();
    }
    for (int i = 0; i < 5; i++) {
      //FXGL.getGameWorld().addEntity(gameFactory.newNoble(nobles.pop()));
      FXGL.spawn("Noble", new SpawnData().put("nobleIndex", nobles.pop()));
    }
  }

  public static void initUI() {
    //displayQuantity(Level.ONE);
    //displayQuantity(Level.TWO);
    //displayQuantity(Level.THREE);
  }

  public static void initGameVars(Map<String, Object> vars) {
    vars.put("level_one_quantity", 40);
    vars.put("level_two_quantity", 30);
    vars.put("level_three_quantity", 20);
  }

  public static void onUpdate() {
    PropertyMap state = FXGL.getWorldProperties();
    state.setValue("level_three_quantity", level_three.size());
    state.setValue("level_two_quantity", level_two.size());
    state.setValue("level_one_quantity", level_one.size());
  }

  public static void addLevelThreeCard() {
    if (!level_three.empty()) {
      FXGL.spawn("LevelThreeCard", new SpawnData().put("cardIndex", level_three.pop()));
    }
  }

  public static void addLevelTwoCard() {
    if (!level_two.empty()) {
      FXGL.spawn("LevelTwoCard", new SpawnData().put("cardIndex", level_two.pop()));
    }
  }

  public static void addLevelOneCard() {
    if (!level_one.empty()) {
      FXGL.spawn("LevelOneCard", new SpawnData().put("cardIndex", level_one.pop()));
    }
  }

  private static void displayQuantity(Level level) {
    Text quantity = new Text();
    quantity.setFill(Color.WHITE);
    quantity.setFont(Font.font(50));
    quantity.setTranslateX(450);
    switch (level) {
      case ONE:
        quantity.setTranslateY(getAppHeight() - 320);
        quantity.textProperty().bind(getip("level_one_quantity").asString());
        break;
      case TWO:
        quantity.setTranslateY(getAppHeight() - 525);
        quantity.textProperty().bind(getip("level_two_quantity").asString());
        break;
      case THREE:
        quantity.setTranslateY(getAppHeight() - 730);
        quantity.textProperty().bind(getip("level_three_quantity").asString());
        break;
    }
    addUINode(quantity);
  }
}
