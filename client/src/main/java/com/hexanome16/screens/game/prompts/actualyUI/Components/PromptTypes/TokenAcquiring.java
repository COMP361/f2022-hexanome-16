package com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
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

public class TokenAcquiring implements PromptTypeInterface {
  double aWidth = getAppWidth() / 2;
  double aHeight = getAppHeight() / 2;
  double aBottonWidth = aWidth / 3;
  double aBottonHeight = aBottonWidth / 4;
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
    BorderPane myPrompt = new BorderPane();
    Text myPromptMessage = new Text("Taking Tokens");
    myPromptMessage.setTextAlignment(TextAlignment.CENTER);
    myPromptMessage.setWrappingWidth(aWidth);
    myPromptMessage.setFont(Font.font(aHeight / 10));
    myPrompt.setTop(myPromptMessage);
    myPrompt.setTranslateX(topleftX);
    myPrompt.setTranslateY(topleftY);

    HBox myChoices = new HBox();
    myPrompt.setCenter(myChoices);
    myChoices.setAlignment(Pos.CENTER);
    myChoices.setPrefSize(aWidth, aHeight * 0.9);
    myChoices.setSpacing(aBottonWidth / 6);

    StackPane ChooseTwo = new StackPane();
    ChooseTwo.setPrefSize(aBottonWidth, aBottonHeight);
    ChooseTwo.setMaxSize(aBottonWidth, aBottonHeight);
    Rectangle ChooseTwoButton = new Rectangle(aBottonWidth, aBottonHeight, Color.rgb(249, 161, 89));
    ChooseTwoButton.setStrokeWidth(aHeight / 100);
    ChooseTwoButton.setStroke(Color.BLACK);
    ChooseTwoButton.setOpacity(0.5);
    Text ChooseTwoMessage = new Text("TAKE 2 TOKENS SAME COLOR");
    ChooseTwoMessage.setFont(Font.font(aBottonHeight * 0.25));
    ChooseTwoMessage.setTextAlignment(TextAlignment.CENTER);
    ChooseTwo.setOnMouseEntered(e -> {
      ChooseTwoButton.setOpacity(1);
    });
    ChooseTwo.setOnMouseExited(e -> {
      ChooseTwoButton.setOpacity(0.5);
    });
    ChooseTwo.setOnMouseClicked(e -> {
      FXGL.spawn("PromptBox", new SpawnData().put("promptType", PromptType.TOKEN_ACQUIRING_TWO));
    });

    ChooseTwo.getChildren().addAll(ChooseTwoButton, ChooseTwoMessage);

    Text OR = new Text("OR");
    OR.setTextAlignment(TextAlignment.CENTER);
    OR.setFont(Font.font(aBottonHeight));

    StackPane ChooseThree = new StackPane();
    ChooseThree.setPrefSize(aBottonWidth, aBottonHeight);
    ChooseThree.setMaxSize(aBottonWidth, aBottonHeight);
    Rectangle ChooseThreeButton =
        new Rectangle(aBottonWidth, aBottonHeight, Color.rgb(249, 161, 89));
    ChooseThreeButton.setStrokeWidth(aHeight / 100);
    ChooseThreeButton.setStroke(Color.BLACK);
    ChooseThreeButton.setOpacity(0.5);
    Text ChooseThreeMessage = new Text("TAKE 3 TOKENS DIFFERENT COLOR");
    ChooseThreeMessage.setFont(Font.font(aBottonHeight * 0.20));
    ChooseThreeMessage.setTextAlignment(TextAlignment.CENTER);
    ChooseThree.setOnMouseEntered(e -> {
      ChooseThreeButton.setOpacity(1);
    });
    ChooseThree.setOnMouseExited(e -> {
      ChooseThreeButton.setOpacity(0.5);
    });
    ChooseThree.setOnMouseClicked(e -> {
      FXGL.spawn("PromptBox", new SpawnData().put("promptType", PromptType.TOKEN_ACQUIRING_THREE));
    });

    ChooseThree.getChildren().addAll(ChooseThreeButton, ChooseThreeMessage);

    myChoices.getChildren().addAll(ChooseTwo, OR, ChooseThree);

    entity.getViewComponent().addChild(myPrompt);
  }
}
