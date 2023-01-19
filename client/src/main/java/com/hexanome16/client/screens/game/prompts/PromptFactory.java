package com.hexanome16.client.screens.game.prompts;


import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.hexanome16.client.screens.game.Level;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;

/**
 * FXGL factory for Prompts.
 */
public class PromptFactory implements EntityFactory {


  /**
   * Method to spawn prompt box.
   *
   * @param data <p>
   *             Contains Type of Prompt that will be spawned under the key "promptType" ,
   *             the possible prompt types can be found in PromptTypeInterface.PromptType.
   *             Another possible key in data is "entity", to use exclusively for the buy card
   *             prompt, this was a quick fix to allow a card's view to be shown in the buy screen.
   *             </p>
   * @return The entity that will be spawned.
   */
  @Spawns("PromptBox")
  public Entity newPromptBox(SpawnData data) {

    // to be reworked.
    if (data.getData().containsKey("entity")) {
      PromptTypeInterface.PromptType promptType = PromptTypeInterface.PromptType.BUY_CARD;
      PromptTypeInterface myPromptType = promptType.getAssociatedClass();
      Entity entity = data.get("entity");

      return entityBuilder(data)
          .with(new PromptComponent(myPromptType, entity))
          .build();
    } else if (data.getData().containsKey("level")) {
      PromptTypeInterface.PromptType promptType = PromptTypeInterface.PromptType.RESERVE_CARD;
      PromptTypeInterface myPromptType = promptType.getAssociatedClass();
      Level level = (Level)data.getData().getOrDefault("level", Level.ONE);

      return entityBuilder(data)
          .with(new PromptComponent(myPromptType))
          .build();
    } else {
      PromptTypeInterface.PromptType promptType = PromptTypeInterface.PromptType.NULL;
      if (data.getData().containsKey("promptType")) {
        promptType = data.get("promptType");
      }
      PromptTypeInterface myPromptType = promptType.getAssociatedClass();

      return entityBuilder(data)
          .with(new PromptComponent(myPromptType))
          .build();
    }
  }
}
