package com.hexanome16.screens.game.prompts.actualyUI;


import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptComponent;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;

public class PromptPartFactory implements EntityFactory {
//          FXGL.spawn("PromptBox",new SpawnData().put("promptType", PromptTypeInterface.PromptType.PAUSE));


  @Spawns("PromptBox")
  public Entity newPromptbox(SpawnData data){
    PromptTypeInterface.PromptType promptType = PromptTypeInterface.PromptType.NULL;

    if (data.getData().containsKey("promptType"))  promptType= data.get("promptType");

    PromptTypeInterface myPromptType = promptType.getAssociatedClass();

    return entityBuilder(data)
        .with(new PromptComponent(myPromptType))
        .build();
  }

}
