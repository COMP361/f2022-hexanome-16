package com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptComponent;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ChooseNoblev1 implements PromptTypeInterface {
  double aWidth = 400;
  double aHeight = 300;
  double nobleWidth;
  double nobleHeight;
  double topleftX;
  double topleftY;



  @Override
  public double width() {
    return aWidth;
  }

  @Override
  public double height() {
    return aHeight;
  }

  @Override
  public void populatePrompt(Entity entity) {
    //initiale key values
    nobleWidth = aWidth/5;
    nobleHeight= nobleWidth;
    topleftX = getAppWidth()/2 - (aWidth/2);
    topleftY = getAppHeight()/2- (aHeight/2);


    //initiate elements
    Node noble1 = new Rectangle(nobleWidth, nobleHeight ,Color.WHITE);
    Node noble2 = new Rectangle(nobleWidth, nobleHeight ,Color.WHITE);
    Circle myConfirmButton = new Circle(topleftX+ 8*(aWidth)/10 ,topleftY+5*(aHeight)/6,height()/10,Color.YELLOW);
    Text PromptText = new Text(topleftX,topleftY+(aHeight)/5,"~Choose a Noble~");
    Texture myNode = FXGL.texture("noble1.png");

    myNode.fitHeightProperty().set(100);
    myNode.fitWidthProperty().set(100);

    // customize elements //////////////////////////////////////////////////////////////////////////
    PromptText.setFont(Font.font("monospace", FontWeight.EXTRA_BOLD, 20));
    PromptText.setTextAlignment(TextAlignment.CENTER);
    PromptText.setWrappingWidth(aWidth);
    //////////////////
    noble1.setTranslateX(topleftX + nobleWidth);
    noble1.setTranslateY(topleftY + nobleHeight);
    noble1.setOpacity(0.5);
    //////////////////
    noble1.addEventHandler(MouseEvent.MOUSE_ENTERED,
        mouseEvent -> {
      if (noble1.getOpacity()!=1) noble1.setOpacity(0.7);
    }
    );
    noble1.addEventHandler(MouseEvent.MOUSE_EXITED,
        mouseEvent -> {
      if (noble1.getOpacity()!=1) noble1.setOpacity(0.5);
    }
    );
    noble1.addEventHandler(MouseEvent.MOUSE_CLICKED,
        mouseEvent -> {
          noble1.setOpacity(1);
          noble2.setOpacity(0.5);
          myConfirmButton.setOpacity(1);
        }
    );
    ///////////////////
    noble2.setTranslateX(topleftX + 3*nobleWidth);
    noble2.setTranslateY(topleftY + nobleHeight);
    noble2.setOpacity(0.5);
    noble2.addEventHandler(MouseEvent.MOUSE_ENTERED,
        mouseEvent -> {
          if (noble2.getOpacity() != 1) noble2.setOpacity(0.7);
        }
    );

    noble2.addEventHandler(MouseEvent.MOUSE_EXITED,
        mouseEvent -> {
          if (noble2.getOpacity() != 1) noble2.setOpacity(0.5);
        }
    );
    noble2.addEventHandler(MouseEvent.MOUSE_CLICKED,
        mouseEvent -> {
      noble2.setOpacity(1);
      noble1.setOpacity(0.5);
      myConfirmButton.setOpacity(1);
    }
    );
    ////////////////////
    myConfirmButton.setOpacity(0.5);
    myConfirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
        mouseEvent -> {
      if (myConfirmButton.getOpacity() == 1.0){
        FXGL.getGameWorld().removeEntities(FXGL.getGameWorld().getEntitiesByComponent(
            PromptComponent.class));

        ///// ADD CHOSEN NOBLE TO HAND /////
      }
    }
    );
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //add elements to view
    entity.getViewComponent().addChild(PromptText);
    entity.getViewComponent().addChild(noble1);
    entity.getViewComponent().addChild(noble2);
    entity.getViewComponent().addChild(myConfirmButton);
    entity.getViewComponent().addChild(myNode);

  }
}
