package org.actualyUI;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.actualyUI.Components.CostComponent;
import org.actualyUI.Components.GemComponent;
import org.actualyUI.Components.PrestigePointComponent;
import org.actualyUI.Components.PromptComponent;
import org.actualyUI.PromptTypes.PromptTypeInterface;

public class FullGameFactory implements EntityFactory {

  @Spawns("Card")
  public Entity newCard(SpawnData data){

    Object myColor = data.get("Color");
    return FXGL.entityBuilder(data)
        .with(new GemComponent(myColor,80,110))
        .with(new PrestigePointComponent())
        .with(new CostComponent())
        .scale(new Point2D(2,2))
        .onClick(new Consumer<Entity>() {
          @Override
          public void accept(Entity entity) {
            FXGL.spawn("PromptBox",new SpawnData().put("promptType", PromptTypeInterface.PromptType.CHOOSE_NOBLES));
          }
        })
        .at(400,300)
        .build();

  }

  @Spawns("Background")
  public Entity newBackGround(SpawnData data){
    var bg = new Rectangle(getAppWidth(),getAppHeight(),Color.BLUE.brighter().brighter().brighter());
    return entityBuilder()
        .view(bg)
        .build();
  }


  @Spawns("Scrollbar")
  public Entity myScrollbar(SpawnData data){
    ScrollPane myPane = new ScrollPane(new Rectangle(100,500, Color.BLUE));
    myPane.setHmax(100);
    myPane.setHmin(100);
    return entityBuilder().view(myPane).build();
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
