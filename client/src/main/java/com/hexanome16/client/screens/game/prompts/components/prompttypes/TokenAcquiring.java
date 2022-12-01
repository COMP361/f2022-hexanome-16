package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.client.Config;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Class for Acquiring Tokens Choice: 2 tokens of the same color or 3 differently colored tokens.
 */
public class TokenAcquiring implements PromptTypeInterface {
  /**
   * The width.
   */
  double atWidth = getAppWidth() / 2.;
  /**
   * The height.
   */
  double atHeight = getAppHeight() / 2.;
  /**
   * The button width.
   */
  double atButtonWidth = atWidth / 3;
  /**
   * The button height.
   */
  double atButtonHeight = atButtonWidth / 4;
  /**
   * The top left x.
   */
  double atTopLeftX = (getAppWidth() / 2.) - (atWidth / 2);
  /**
   * The top left y.
   */
  double atTopLeftY = (getAppHeight() / 2.) - (atHeight / 2);

  @Override
  public double getWidth() {
    return atWidth;
  }

  @Override
  public double getHeight() {
    return atHeight;
  }


  @Override
  public void populatePrompt(Entity entity) {

    // initiate and set up prompt layout
    BorderPane myPrompt = new BorderPane();
    myPrompt.setTranslateX(atTopLeftX);
    myPrompt.setTranslateY(atTopLeftY);

    // prompt message
    Text myPromptMessage = new Text("Taking Tokens");
    myPromptMessage.setTextAlignment(TextAlignment.CENTER);
    myPromptMessage.setWrappingWidth(atWidth);
    myPromptMessage.setFont(Font.font(atHeight / 10.));

    // set up choose 2 choice
    StackPane chooseTwo = makeButton("TAKE 2 TOKENS SAME COLOR", 0.25,
        PromptType.TOKEN_ACQUIRING_TWO);

    // set up OR text
    Text orText = new Text("OR");
    orText.setTextAlignment(TextAlignment.CENTER);
    orText.setFont(Font.font(atButtonHeight));

    // set up choose 3 choice
    StackPane chooseThree = makeButton("TAKE 3 TOKENS DIFFERENT COLOR", 0.20,
        PromptType.TOKEN_ACQUIRING_THREE);

    // set up choices' layout (Choose 2) (OR) (Choose 3)
    HBox myChoices = new HBox();
    myChoices.getChildren().addAll(chooseTwo, orText, chooseThree);
    myChoices.setAlignment(Pos.CENTER);
    myChoices.setPrefSize(atWidth, atHeight * 0.9);
    myChoices.setSpacing(atButtonWidth / 6.);

    // add built elements to prompt layout and add to entity
    myPrompt.setTop(myPromptMessage);
    myPrompt.setCenter(myChoices);
    entity.getViewComponent().addChild(myPrompt);
  }

  // for the second parameter, the longer the text the smaller that fraction needs to be
  private StackPane makeButton(String buttonText,
                               double fontHeightFraction, PromptType promptToOpen) {
    // initiate and set up layout of button
    StackPane stackPane = new StackPane();
    stackPane.setPrefSize(atButtonWidth, atButtonHeight);
    stackPane.setMaxSize(atButtonWidth, atButtonHeight);

    // set up button rectangle
    Rectangle buttonRectangle =
        new Rectangle(atButtonWidth, atButtonHeight, Config.SECONDARY_COLOR);
    buttonRectangle.setStrokeWidth(atHeight / 100);
    buttonRectangle.setStroke(Color.BLACK);
    buttonRectangle.setOpacity(0.5);

    // set up button message
    Text buttonMessage = new Text(buttonText);
    buttonMessage.setFont(Font.font(atButtonHeight * fontHeightFraction));
    buttonMessage.setTextAlignment(TextAlignment.CENTER);

    // adds onHoverEffect
    PromptTypeInterface.setOnHoverEffectOpacity(stackPane, buttonRectangle, 0.5, 1.0);

    // add onClick (Spawns Prompt of type promptToOpen)
    stackPane.setOnMouseClicked(e -> {
      FXGL.spawn("PromptBox", new SpawnData().put("promptType", promptToOpen));
    });

    //
    stackPane.getChildren().addAll(buttonRectangle, buttonMessage);
    return stackPane;
  }

}
