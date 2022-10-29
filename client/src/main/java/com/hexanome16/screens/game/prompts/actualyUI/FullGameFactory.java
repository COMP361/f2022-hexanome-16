package com.hexanome16.screens.game.prompts.actualyUI;


import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.hexanome16.screens.game.prompts.actualyUI.Components.CostComponent;
import com.hexanome16.screens.game.prompts.actualyUI.Components.GemComponent;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PrestigePointComponent;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptComponent;
import com.hexanome16.screens.game.prompts.actualyUI.PromptTypes.PromptTypeInterface;
import java.util.function.Consumer;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FullGameFactory implements EntityFactory {

  @Spawns("Card")
  public Entity newCard(SpawnData data){

    Object myColor = data.get("Color");
    return FXGL.entityBuilder(data)
        .with(new GemComponent(myColor,80,110))
        .with(new PrestigePointComponent())
        .with(new CostComponent())
        .onClick(new Consumer<Entity>() {
          @Override
          public void accept(Entity entity) {
            FXGL.spawn("PromptBox",new SpawnData().put("promptType", PromptTypeInterface.PromptType.BUY_CARDS));
          }
        })
        .build();

  }

  @Spawns("NobleChoice")
  public Entity newNobleChoice(SpawnData data){
    Text myText = new Text("Noble choice Prompt");
    myText.setFont(Font.font(30));

    return entityBuilder(data)
        .view(new StackPane((new Rectangle(300,100,Color.GREENYELLOW)),(myText)))
        .at(100,100)
        .onClick( e -> {
          FXGL.spawn("PromptBox",new SpawnData().put("promptType", PromptTypeInterface.PromptType.CHOOSE_NOBLES));
        })
        .build();
  }

  @Spawns("ViewHandColor")
  public Entity newViewHandColor(SpawnData data){
    Text myText = new Text("Cards in Hand");
    myText.setFont(Font.font(30));

    return entityBuilder(data)
        .view(new StackPane((new Rectangle(300,100,Color.BLUE)),(myText)))
        .at(500,100)
        .onClick( e -> {
          FXGL.spawn("PromptBox",new SpawnData().put("promptType", PromptTypeInterface.PromptType.SEE_CARDS));
        })
        .build();
  }


  @Spawns("ViewReservedSelf")
  public Entity newViewReservedSelf(SpawnData data){
    Text myText = new Text("Own Reserved Cards \n+\nBuy Reserved Card");
    myText.setFont(Font.font(30));

    return entityBuilder(data)
        .view(new StackPane((new Rectangle(300,100,Color.RED)),(myText)))
        .at(900,100)
        .onClick( e -> {
          FXGL.spawn("PromptBox",new SpawnData().put("promptType", PromptTypeInterface.PromptType.SEE_OWN_RESERVED));
        })
        .build();
  }


  @Spawns("ViewOtherSelf")
  public Entity newViewOtherSelf(SpawnData data){
    Text myText = new Text("Other Reserved Cards");
    myText.setFont(Font.font(30));

    return entityBuilder(data)
        .view(new StackPane((new Rectangle(300,100,Color.GOLD)),(myText)))
        .at(1300,100)
        .onClick( e -> {
          FXGL.spawn("PromptBox",new SpawnData().put("promptType", PromptTypeInterface.PromptType.SEE_OTHER_RESERVED));
        })
        .build();
  }



  @Spawns("BuyingReserved")
  public Entity newBuyingReserved(SpawnData data){
    Text myText = new Text("Buying Reserved Card");
    myText.setFont(Font.font(30));

    return entityBuilder(data)
        .view(new StackPane((new Rectangle(300,100,Color.GOLD)),(myText)))
        .at(1300,100)
        .onClick( e -> {
          FXGL.spawn("PromptBox",new SpawnData().put("promptType", PromptTypeInterface.PromptType.SEE_OTHER_RESERVED));
        })
        .build();
  }

  @Spawns("BuyBagCard")
  public Entity newAssociateBagCard(SpawnData data){
    Text myText = new Text("Buy Bag Card\n+\nAdding to Bonus");
    myText.setFont(Font.font(30));
    return entityBuilder(data)
        .view(new StackPane((new Rectangle(300,100,Color.GREENYELLOW)),(myText)))
        .at(100,300)
        .onClick( e -> {
          FXGL.spawn("PromptBox",new SpawnData().put("promptType", PromptTypeInterface.PromptType.BUY_BAG_CARD));
        })
        .build();
  }

  @Spawns("TakingTokens")
  public Entity newTakingTokens(SpawnData data){
    Text myText = new Text("Taking Tokens");
    myText.setFont(Font.font(30));
    return entityBuilder(data)
        .view(new StackPane((new Rectangle(300,100,Color.YELLOW)),(myText)))
        .at(500,300)
        .onClick( e -> {
          FXGL.spawn("PromptBox",new SpawnData().put("promptType", PromptTypeInterface.PromptType.TOKEN_ACQUIRING));
        })
        .build();
  }




  @Spawns("Background")
  public Entity newBackGround(SpawnData data){
    var bg = new Rectangle(getAppWidth(),getAppHeight(),Color.GREY.darker());
    return entityBuilder()
        .view(bg)
        .build();
  }





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
