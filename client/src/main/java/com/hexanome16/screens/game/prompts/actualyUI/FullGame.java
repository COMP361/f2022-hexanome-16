package com.hexanome16.screens.game.prompts.actualyUI;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.ComponentListener;
import java.awt.Color;

public class FullGame extends GameApplication {
  @Override
  protected void initSettings(GameSettings gameSettings) {
      gameSettings.setVersion("1");
      gameSettings.setTitle("Am struggling real hard rn");
//      gameSettings.setWidth(1920);
//      gameSettings.setHeight(1080);
  }



  @Override
  protected void initGame() {
    FXGL.getGameWorld().addEntityFactory(new FullGameFactory());
    Entity myBG = FXGL.spawn("Background");
    Entity myCard = FXGL.spawn("Card", new SpawnData().put("Color", GemEnum.RUBY));

  }

  public static void main(String[] args) {
    launch(args);
  }
}
