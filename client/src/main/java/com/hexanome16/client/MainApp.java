package com.hexanome16.client;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.hexanome16.client.Config.APP_HEIGHT;
import static com.hexanome16.client.Config.APP_TITLE;
import static com.hexanome16.client.Config.APP_VERSION;
import static com.hexanome16.client.Config.APP_WIDTH;
import static com.hexanome16.client.Config.CURSOR_HOTSPOT;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.hexanome16.client.screens.game.GameFactory;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.players.DeckFactory;
import com.hexanome16.client.screens.game.prompts.PromptFactory;
import com.hexanome16.client.screens.lobby.LobbyFactory;
import com.hexanome16.client.screens.startup.StartupScreen;
import com.hexanome16.client.screens.startup.StartupScreenFactory;
import java.util.Map;

/**
 * FXGL Game Application, Game's entry point.
 */
public class MainApp extends GameApplication {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  protected void initSettings(GameSettings gameSettings) {
    gameSettings.setWidth(APP_WIDTH);
    gameSettings.setHeight(APP_HEIGHT);
    gameSettings.setTitle(APP_TITLE);
    gameSettings.setVersion(APP_VERSION);
  }

  @Override
  protected void initGame() {
    getGameWorld().addEntityFactory(new GameFactory());
    getGameWorld().addEntityFactory(new PromptFactory());
    getGameWorld().addEntityFactory(new DeckFactory());
    getGameWorld().addEntityFactory(new StartupScreenFactory());
    getGameWorld().addEntityFactory(new LobbyFactory());
    getGameScene().setCursor(FXGL.getAssetLoader().loadCursorImage("cursor.png"), CURSOR_HOTSPOT);
    StartupScreen.backToStartupScreen();
  }

  @Override
  protected void onUpdate(double tpf) {
    GameScreen.onUpdate();
  }

  @Override
  protected void initGameVars(Map<String, Object> vars) {
    GameScreen.initGameVars(vars);
  }
}
