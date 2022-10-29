package com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class SeeOtherReserved implements PromptTypeInterface {

  double aWidth = getAppWidth() / 2;
  double aHeight = getAppHeight() / 2;
  double aCardWidth = aWidth/4;
  double aCardHeight = aCardWidth * 1.39;
  double topleftX = (getAppWidth() / 2) - (aWidth / 2);
  double topleftY = (getAppHeight() / 2) - (aHeight / 2);

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
    BorderPane myBorderPane = new BorderPane();
    myBorderPane.setTranslateX(topleftX);
    myBorderPane.setTranslateY(topleftY);
    HBox ReservedCards = new HBox();
    ReservedCards.setAlignment(Pos.CENTER);
    ReservedCards.setSpacing((aWidth-3*aCardWidth)/4);
    Text myPromptMessage = new Text("Opponent Reserved Cards");
    myPromptMessage.setFont(Font.font(aHeight/20));
    myPromptMessage.setTextAlignment(TextAlignment.CENTER);
    myPromptMessage.setWrappingWidth(aWidth);
    myBorderPane.setTop(myPromptMessage);
    myBorderPane.setCenter(ReservedCards);
    //    myScrollPane.setTranslateX(topleftX);
//    myScrollPane.setTranslateY(topleftY);




    Texture myCard = FXGL.texture("card1.png");
    myCard.setFitWidth(aCardWidth);
    myCard.setFitHeight(aCardHeight);
    ReservedCards.getChildren().add(myCard);

    StackPane myanonymousCard = new StackPane();
    Rectangle myOtherCard = new Rectangle(aCardWidth,aCardHeight, Color.BLACK);
    Text myintoregation = new Text("?");
    myintoregation.setFont(Font.font(aCardHeight*0.9));
    myintoregation.setFill(Color.WHITE);
    myintoregation.setTextAlignment(TextAlignment.CENTER);
    myanonymousCard.getChildren().addAll(myOtherCard,myintoregation);
    ReservedCards.getChildren().add(myanonymousCard);

    ReservedCards.setPrefSize(aWidth,aHeight*0.8);


    entity.getViewComponent().addChild(myBorderPane);
  }
}
