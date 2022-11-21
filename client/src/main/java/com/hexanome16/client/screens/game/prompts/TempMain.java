package com.hexanome16.client.screens.game.prompts;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.hexanome16.client.Config.APP_HEIGHT;
import static com.hexanome16.client.Config.APP_TITLE;
import static com.hexanome16.client.Config.APP_VERSION;
import static com.hexanome16.client.Config.APP_WIDTH;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.client.screens.game.GameFactory;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.players.DeckFactory;
import com.hexanome16.client.screens.lobby.LobbyFactory;
import com.hexanome16.client.screens.startup.LoginScreen;
import com.hexanome16.client.screens.startup.StartupScreen;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class TempMain extends GameApplication {
  @Override
  protected void initSettings(GameSettings gameSettings) {
    gameSettings.setWidth(APP_WIDTH);
    gameSettings.setHeight(APP_HEIGHT);
    gameSettings.setTitle(APP_TITLE);
    gameSettings.setVersion(APP_VERSION);
  }

  @Override
  protected void initGame() {
    getGameWorld().addEntityFactory(new PromptFactory());
    getGameWorld().addEntityFactory(new TempPromptFactory());
    Entity myBG = FXGL.spawn("Background");
    Entity myNobleChoice = FXGL.spawn("NobleChoice");
    Entity aHandofCards = FXGL.spawn("ViewHandColor");
    Entity myOwnReservedCards = FXGL.spawn("ViewReservedSelf");
    Entity OthersReservedCards = FXGL.spawn("ViewOtherSelf");
    Entity AssociateBuyCard = FXGL.spawn("BuyBagCard");
    Entity TakingTokens = FXGL.spawn("TakingTokens");
    Entity BuyCardByCard = FXGL.spawn("BuyCardByCard");
    Entity BuyCard = FXGL.spawn("BuyCard");
    Entity BuyNobleReserve = FXGL.spawn("BuyNobleReserve");
    Entity Pause = FXGL.spawn("Pause");
    Entity PeiniCard = FXGL.spawn("PeiniCard");

  }

  public static void main(String[] args) {
    launch(args);
  }
}
