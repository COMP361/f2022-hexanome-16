package com.hexanome16.client;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.hexanome16.client.Config.APP_HEIGHT;
import static com.hexanome16.client.Config.APP_TITLE;
import static com.hexanome16.client.Config.APP_VERSION;
import static com.hexanome16.client.Config.APP_WIDTH;
import static com.hexanome16.client.Config.CURSOR_HOTSPOT;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.client.screens.game.GameFactory;
import com.hexanome16.client.screens.game.players.DeckFactory;
import com.hexanome16.client.screens.game.prompts.PromptFactory;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.lobby.LobbyFactory;
import com.hexanome16.client.screens.register.RegisterFactory;
import com.hexanome16.client.screens.startup.StartupScreen;
import com.hexanome16.client.screens.startup.StartupScreenFactory;

/**
 * FXGL Game Application, Game's entry point.
 */
public class MainApp extends GameApplication {
  /**
   * The error message.
   */
  public static String errorMessage;

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    Thread.setDefaultUncaughtExceptionHandler(MainApp::showError);
    launch(args);
  }

  @Override
  protected void initSettings(GameSettings gameSettings) {
    gameSettings.setWidth(APP_WIDTH);
    gameSettings.setHeight(APP_HEIGHT);
    gameSettings.setTitle(APP_TITLE);
    gameSettings.setVersion(APP_VERSION);
    gameSettings.setMainMenuEnabled(false);
    gameSettings.setGameMenuEnabled(false);
    gameSettings.setDeveloperMenuEnabled(false);
    gameSettings.setManualResizeEnabled(true);
    gameSettings.setPreserveResizeRatio(true);
    gameSettings.setApplicationMode(ApplicationMode.RELEASE);
  }

  @Override
  protected void initGame() {
    getGameWorld().addEntityFactory(new GameFactory());
    getGameWorld().addEntityFactory(new PromptFactory());
    getGameWorld().addEntityFactory(new DeckFactory());
    getGameWorld().addEntityFactory(new StartupScreenFactory());
    getGameWorld().addEntityFactory(new LobbyFactory());
    getGameWorld().addEntityFactory(new RegisterFactory());
    getGameScene().setCursor(FXGL.getAssetLoader().loadCursorImage("cursor.png"), CURSOR_HOTSPOT);
    StartupScreen.backToStartupScreen();
  }

  private static void showError(Thread t, Throwable e) {
    errorMessage = e.getMessage();
    spawn("PromptBox", new SpawnData().put("promptType", PromptTypeInterface.PromptType.ERROR));
  }
}
