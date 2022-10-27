package com.hexanome16.screens.game.prompts.actualyUI.PromptTypes;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BuyCard implements PromptTypeInterface {

  double aWidth = getAppWidth()/2;
  double aHeight = getAppHeight()/2;
  double aCardHeight = aHeight*0.7;
  double aCardWidth = aCardHeight*0.72;
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
    //initializing Hbox
    double boxesWidth = aWidth/5;
    double SurplusWidth = (aWidth-3*boxesWidth-aCardWidth)/3;
    HBox myPrompt = new HBox();
    initiatePane(myPrompt);
    myPrompt.setAlignment(Pos.CENTER);
    myPrompt.setSpacing(SurplusWidth);

    // initiate elements
    VBox playerBank = new VBox();
    playerBank.setAlignment(Pos.CENTER);
    playerBank.setPrefSize(boxesWidth,aHeight/5);
    playerBank.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));

    Texture myCard = FXGL.texture("card1.png");
    myCard.setFitWidth(aCardWidth);
    myCard.setFitHeight(aCardHeight);

    VBox GameBank = new VBox();
    GameBank.setPrefSize(boxesWidth,aHeight/5);
    GameBank.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));

    VBox ReserveBuy= new VBox();
    ReserveBuy.setPrefSize(boxesWidth,aHeight/5);
    ReserveBuy.setBackground(new Background(new BackgroundFill(Color.YELLOW,null,null)));

    // initiate Player Bank elements




    // adding to view
    myPrompt.getChildren().addAll(playerBank,myCard,GameBank,ReserveBuy);
    entity.getViewComponent().addChild(myPrompt);
  }

  private void initiatePane(Pane myPrompt) {
    myPrompt.setTranslateX(topleftX);
    myPrompt.setTranslateY(topleftY);
    myPrompt.setPrefWidth(aWidth);
    myPrompt.setMaxWidth(aWidth);
    myPrompt.setPrefHeight(aHeight);
    myPrompt.setMaxHeight(aHeight);
  }
}
