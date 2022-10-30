package com.hexanome16.screens.game;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.hexanome16.screens.game.components.CardComponent;
import com.hexanome16.screens.game.components.NobleComponent;

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
        .with(new CardComponent(Level.ONE))
        .build();
  }

  @Spawns("LevelTwoCard")
  public Entity newLevelTwoCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 360)
        .view(levelTwo + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.TWO))
        .build();
  }

  @Spawns("LevelThreeCard")
  public Entity newLevelThreeCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 155)
        .view(levelThree + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.THREE))
        .build();
  }

  @Spawns("LevelThreeDeck")
  public Entity levelThree(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 155)
        .view("level_three.png")
        .scale(0.15, 0.15)
        .build();
  }

  @Spawns("LevelTwoDeck")
  public Entity levelTwo(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 360)
        .view("level_two.png")
        .scale(0.15, 0.15)
        .build();
  }

  @Spawns("LevelOneDeck")
  public Entity levelOne(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 565)
        .view("level_one.png")
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
