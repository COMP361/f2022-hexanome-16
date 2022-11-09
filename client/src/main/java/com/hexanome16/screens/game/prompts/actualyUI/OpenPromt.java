package com.hexanome16.screens.game.prompts.actualyUI;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;

public class OpenPromt {

  public static void openPrompt(PromptTypeInterface.PromptType pPromptType) {
    FXGL.spawn("PromptBox", new SpawnData().put("promptType", pPromptType));
  }

  public static void openPrompt(Entity entity) {
    FXGL.spawn("PromptBox", new SpawnData().put("entity", entity));
  }
}
