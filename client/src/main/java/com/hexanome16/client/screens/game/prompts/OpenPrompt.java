package com.hexanome16.client.screens.game.prompts;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.common.models.Level;

/**
 * Class containing 2 methods to open prompts.
 */
public class OpenPrompt {

  /**
   * Opens prompt of Type promptType.
   *
   * @param promptType Type of prompt to open.
   */
  public static void openPrompt(PromptTypeInterface.PromptType promptType) {
    FXGL.spawn("PromptBox", new SpawnData().put("promptType", promptType));
  }

  /**
   * Opens Buy prompt with the entity as the view of the card to be bought.
   * To be reworked.
   *
   * @param entity The card entity to be displayed.
   */
  public static void openPrompt(Entity entity) {
    FXGL.spawn("PromptBox", new SpawnData().put("entity", entity));
  }

  /**
   * Opens Deck Reserve prompt.
   *
   * @param level The deck level.
   */
  public static void openPrompt(Level level) {
    FXGL.spawn("PromptBox", new SpawnData().put("level", level));
  }
}
