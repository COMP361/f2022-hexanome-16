package com.hexanome16.screens.game;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getip;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.screens.game.components.CardComponent;
import com.hexanome16.screens.game.components.NobleComponent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameFactory implements EntityFactory
{
  private String levelOne = "level_one";
  private String levelTwo = "level_two";
  private String levelThree = "level_three";

  public static final int matCoordsX = 400;

  public static final int matCoordsY = 150;

  @Spawns("LevelOneCard")
  public Entity newLevelOneCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 565)
        .view(levelOne + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.ONE, levelOne + data.getData().get("cardIndex") + ".png"))
        .build();
  }

  @Spawns("LevelTwoCard")
  public Entity newLevelTwoCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 360)
        .view(levelTwo + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.TWO, levelTwo + data.getData().get("cardIndex") + ".png"))
        .build();
  }

  @Spawns("LevelThreeCard")
  public Entity newLevelThreeCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 155)
        .view(levelThree + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.THREE, levelThree + data.getData().get("cardIndex") + ".png"))
        .build();
  }

  @Spawns("LevelThreeDeck")
  public Entity levelThree(SpawnData data) {
    StackPane myStackPane = new StackPane();
    Texture level3deck = FXGL.texture("level_three.png");
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    myNumber.textProperty().bind(getip("level_three_quantity").asString());
    myStackPane.getChildren().addAll(level3deck,myNumber);

    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 155)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }

  @Spawns("LevelTwoDeck")
  public Entity levelTwo(SpawnData data) {
    StackPane myStackPane = new StackPane();
    Texture level2deck = FXGL.texture("level_two.png");
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    myNumber.textProperty().bind(getip("level_two_quantity").asString());

    myStackPane.getChildren().addAll(level2deck,myNumber);

    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 360)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }

  @Spawns("LevelOneDeck")
  public Entity levelOne(SpawnData data) {
    StackPane myStackPane = new StackPane();
    Texture level1deck = FXGL.texture("level_one.png");
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    myNumber.textProperty().bind(getip("level_one_quantity").asString());
    myStackPane.getChildren().addAll(level1deck,myNumber);
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 565)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }

  @Spawns("Noble")
  public Entity newNoble(SpawnData data) {
    return FXGL.entityBuilder()
        .view("noble" + data.getData().get("nobleIndex") + ".png")
        .with(new NobleComponent())
        .scale(0.15, 0.15)
        .build();
  }

  @Spawns("Mat")
  public Entity buildMat(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX, matCoordsY)
        .view("mat.png")
        .scale(0.6, 0.6)
        .build();
  }

  @Spawns("Background")
  public Entity buildBackground(SpawnData data) {
    return FXGL.entityBuilder()
        .at(0, 0)
        .view("background.png")
        .scale(1, 1)
        .build();
  }

}
