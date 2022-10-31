package com.hexanome16;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.hexanome16.screens.game.GameFactory;
import com.hexanome16.screens.game.GameScreen;
import com.hexanome16.screens.game.prompts.actualyUI.PromptPartFactory;
import com.hexanome16.screens.lobby.LobbyFactory;
import com.hexanome16.screens.startup.LoginScreen;
import com.hexanome16.screens.startup.StartupScreen;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.hexanome16.Config.*;

public class MainApp extends GameApplication {
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
    getGameWorld().addEntityFactory(new GameFactory());
    getGameWorld().addEntityFactory(new PromptPartFactory());
    getGameWorld().addEntityFactory(new StartupScreen());
    getGameWorld().addEntityFactory(new LoginScreen());
    getGameWorld().addEntityFactory(new LobbyFactory());

    StartupScreen.backToMainScreen();
  }

  @Override
  protected void onUpdate(double tpf) {
    GameScreen.onUpdate();
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  protected void initGameVars(Map<String, Object> vars) {
    GameScreen.initGameVars(vars);
  }

}
