package com.hexanome16.screens.game.prompts.actualyUI.PromptTypes;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptComponent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ChooseNobleAlt implements PromptTypeInterface{
  double aWidth = getAppWidth()/2;
  double aHeight = getAppHeight()/2;
  double aNobleWidth = aWidth/4;
  double aNobleHeight = aNobleWidth;
  double topleftX = (getAppWidth()/2)-(aWidth/2);
  double topleftY = (getAppHeight()/2) - (aHeight/2);


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
    double ConfirmButtonRadii = aHeight/10;

    //initiate BorderPane
    BorderPane myPrompt = new BorderPane();
    myPrompt.setTranslateX(topleftX);
    myPrompt.setTranslateY(topleftY);
    myPrompt.setPrefWidth(aWidth);
    myPrompt.setMaxWidth(aWidth);
    myPrompt.setPrefHeight(aHeight);
    myPrompt.setMaxHeight(aHeight);



    //initiate elements
    Text promptMessage = new Text();            //Top
    HBox nobles = new HBox();                   //Nobles
    Circle confirmationButton = new Circle();   //Button
    Font RobotoBold = FXGL.getAssetLoader().loadFont("Roboto-Bold.ttf").newFont(60);
    Texture noble1 = FXGL.texture("noble1.png");
    Texture noble2 = FXGL.texture("noble2.png");


    // customize elements //////////////////////////////////////////////////////////////////////////
    // Resizing Nobles//
    noble1.setFitWidth(aNobleWidth);
    noble1.setFitHeight(aNobleHeight);
    noble2.setFitWidth(aNobleWidth);
    noble2.setFitHeight(aNobleHeight);
    ////////////////////

    //To visualize Pane//
    //.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));//
    ////////////////////

    // prompt message//
    promptMessage.setText("Choose One");
    promptMessage.setFont(RobotoBold);
    promptMessage.setTextAlignment(TextAlignment.CENTER);
    promptMessage.setWrappingWidth(aWidth);
    myPrompt.setTop(promptMessage);

    // Confirm button//
    confirmationButton.setRadius(ConfirmButtonRadii);
    confirmationButton.setFill(Color.rgb(249,161,89));
    confirmationButton.setOpacity(0.5);
    confirmationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      if(confirmationButton.getOpacity() == 1) {
        PromptComponent.closePrompt();
        ////////Handle new Noble///////////
      }
    });

    /*Centering circle*/
    HBox inter1=new HBox(confirmationButton);
    inter1.setAlignment(Pos.CENTER);
    inter1.setPrefSize(ConfirmButtonRadii*4,aHeight);
    /*Circle in inter1*/
    myPrompt.setRight(inter1);


    // Nobles /////////
    /*initiate Rectangles*/
    Node firstNobleRelated = new Rectangle(300,300,Color.RED);
    Node secondNobleRelated = new Rectangle(300,300,Color.RED);
//    firstNobleRelated.addEventHandler(MouseEvent.MOUSE_ENTERED);






    StackPane firstnoble= new StackPane(firstNobleRelated,noble1);
    StackPane secondnoble = new StackPane(secondNobleRelated,noble2);
    nobles.getChildren().addAll(firstnoble,secondnoble);
    nobles.setAlignment(Pos.CENTER);
    nobles.setPrefSize(3*aHeight/4,3*aWidth/4);
    nobles.setSpacing(aNobleWidth/4);
    myPrompt.setCenter(nobles);


    ////////////////////////////////////////////////////////////////////////////////////////////////

    //add elements to view
    entity.getViewComponent().addChild(myPrompt);
  }
}
