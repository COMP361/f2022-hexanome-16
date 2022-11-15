package com.hexanome16.client;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.hexanome16.client.Config.APP_HEIGHT;
import static com.hexanome16.client.Config.APP_TITLE;
import static com.hexanome16.client.Config.APP_VERSION;
import static com.hexanome16.client.Config.APP_WIDTH;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.hexanome16.client.screens.game.GameFactory;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.players.DeckFactory;
import com.hexanome16.client.screens.game.prompts.actualyUI.PromptPartFactory;
import com.hexanome16.client.screens.lobby.LobbyFactory;
import com.hexanome16.client.screens.startup.LoginScreen;
import com.hexanome16.client.screens.startup.StartupScreen;
import java.util.Map;

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

  // To Spawn Prompt, OpenPromt.openPrompt(PromptTypeInterface.PromptType.<AN_ENUM_ELEMENT_FROM_PROMPT_TYPE_INTERFACE>);
  // see all possible prompt types in the PromptTypeInterface, inside the inner enum in src/main/java/com/hexanome16/screens/game/prompts/actualyUI/Components/PromptTypes
  // Or look for PromptTypeInterface
  @Override
  protected void initGame() {
    FXGL.getGameWorld().addEntityFactory(new GameFactory());
    FXGL.getGameWorld().addEntityFactory(new PromptPartFactory());
    FXGL.getGameWorld().addEntityFactory(new DeckFactory());
    getGameWorld().addEntityFactory(new StartupScreen());
    getGameWorld().addEntityFactory(new LoginScreen());
    getGameWorld().addEntityFactory(new LobbyFactory());

    StartupScreen.backToMainScreen();
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
