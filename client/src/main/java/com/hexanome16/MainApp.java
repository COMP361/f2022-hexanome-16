package com.hexanome16;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.hexanome16.screens.game.GameScreen;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes.BuyCard;
import com.hexanome16.screens.game.prompts.actualyUI.OpenPromt;
import com.hexanome16.screens.game.prompts.actualyUI.PromptPartFactory;
import java.util.Map;

public class MainApp extends GameApplication {
  @Override
  protected void initSettings(GameSettings gameSettings) {
      gameSettings.setWidth(1920);
      gameSettings.setHeight(1080);
  }



  // To Spawn Prompt, OpenPromt.openPrompt(PromptTypeInterface.PromptType.<AN_ENUM_ELEMENT_FROM_PROMPT_TYPE_INTERFACE>);
  // see all possible prompt types in the PromptTypeInterface, inside the inner enum in src/main/java/com/hexanome16/screens/game/prompts/actualyUI/Components/PromptTypes
  // Or look for PromptTypeInterface
  @Override
  protected void initGame() {
    GameScreen.initGame();
  }

  @Override
  protected void initUI() {
    GameScreen.initUI();
  }

  @Override
  protected void initGameVars(Map<String, Object> vars) {
    GameScreen.initGameVars(vars);
  }

  @Override
  protected void onUpdate(double tpf) {
    GameScreen.onUpdate();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
