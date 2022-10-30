package com.hexanome16.screens.game.prompts.actualyUI;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;

public class OpenPromt {

  public static void openPrompt(PromptTypeInterface.PromptType pPromptType){
    FXGL.spawn("PromptBox",new SpawnData().put("promptType", pPromptType));
  }
}
