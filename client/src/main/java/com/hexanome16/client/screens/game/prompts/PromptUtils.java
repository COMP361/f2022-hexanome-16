package com.hexanome16.client.screens.game.prompts;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.util.CustomHttpResponses;
import javafx.util.Pair;
import kong.unirest.core.Headers;

/**
 * Class containing 2 methods to open prompts.
 */
public class PromptUtils {

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


  // TODO : ADD ALL OTHER ACTION TYPES AND THEIR ACCORDING PROMPTS.
  /**
   * Takes in a response from the server and spawns appropriate prompts.
   *
   * @param serverResponse response from server.
   */
  public static void actionResponseSpawner(Pair<Headers, String> serverResponse) {
    Headers headers = serverResponse.getKey();

    // TODO :: FIND A FIX FOR THIS, once card is replaced, the card that comes
    // after doesnt seem to have a card bonus type. (check for null shouldnt be
    // necessary)
    if (headers == null) {
      System.out.println("WOW this is no good, response headers was null :(");
      return;
    }

    if (headers.get(CustomHttpResponses.ActionType.ACTION_TYPE).get(0)
        .equals(CustomHttpResponses.ActionType.LEVEL_TWO.getMessage())) {
      FXGL.spawn("PromptBox",
          new SpawnData().put("promptType", PromptTypeInterface.PromptType.CHOOSE_LEVEL_TWO));
    }
  }
}