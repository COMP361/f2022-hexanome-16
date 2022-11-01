package com.hexanome16;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.hexanome16.screens.game.GameFactory;
import com.hexanome16.screens.game.GameScreen;
import com.hexanome16.screens.game.players.DeckFactory;
import com.hexanome16.screens.game.prompts.actualyUI.PromptPartFactory;
import com.hexanome16.screens.startup.LoginScreen;
import com.hexanome16.screens.startup.StartupScreen;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class MainApp extends GameApplication {
  @Override
  protected void initSettings(GameSettings gameSettings) {
      gameSettings.setWidth(1920);
      gameSettings.setHeight(1080);
      gameSettings.setTitle("Splendor");
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
