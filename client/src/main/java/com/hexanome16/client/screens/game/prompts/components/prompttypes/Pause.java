package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.hexanome16.client.Config;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.lobby.LobbyScreen;
import com.hexanome16.client.screens.settings.SettingsScreen;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Class for getting access to the in-game quick menu.
 */
public class Pause implements PromptTypeInterface {

  double atWidth = FXGL.getAppWidth() / 6.;
  double atHeight = FXGL.getAppHeight() / 2.;
  double atTopLeftX = FXGL.getAppWidth() / 2. - atWidth / 2;
  double atTopLeftY = FXGL.getAppHeight() / 2. - atHeight / 2;
  double atButtonWidth = atWidth * 0.75;
  double atButtonHeight = atButtonWidth * 0.25;

  @Override
  public double width() {
    return atWidth;
  }

  @Override
  public double height() {
    return atHeight;
  }

  @Override
  public void populatePrompt(Entity entity) {

    // initiate and set up the buttons' layout
    VBox buttonsHolder = new VBox();
    buttonsHolder.setAlignment(Pos.CENTER);
    buttonsHolder.setSpacing(atButtonHeight / 2);
    buttonsHolder.setTranslateX(atTopLeftX);
    buttonsHolder.setTranslateY(atTopLeftY);
    buttonsHolder.setPrefSize(atWidth, atHeight);

    // add buttons to the layout
    for (ButtonType t : ButtonType.values()) {
      Node newButton = addButton(t);
      buttonsHolder.getChildren().add(newButton);
    }

    // add layout to entity
    entity.getViewComponent().addChild(buttonsHolder);
  }


  private Node addButton(ButtonType buttonType) {

    // initialize and set up button layout
    StackPane button = new StackPane();
    button.setPrefSize(atButtonWidth, atButtonHeight * 0.2);
    button.setMaxSize(atButtonWidth, atButtonHeight * 0.2);

    // initialize and set up button Text
    Text buttonText = new Text(buttonType.toString());
    buttonText.setFont(Font.font(atButtonHeight * 0.7));
    buttonText.setWrappingWidth(atButtonWidth * 0.9);
    buttonText.setTextAlignment(TextAlignment.CENTER);

    // initialize and set up button body
    Rectangle buttonBody = new Rectangle(atButtonWidth, atButtonHeight, Config.SECONDARY_COLOR);
    buttonBody.setStrokeWidth(atButtonHeight * 0.1);
    buttonBody.setStroke(Color.BLACK);
    buttonBody.setOpacity(0.7);

    // add elements of button to layout
    button.getChildren().addAll(buttonBody, buttonText);

    // add effects on Hover to button
    PromptTypeInterface.setOnHoverEffectOpacity(button, buttonBody, 0.7, 1.0);

    // add on click effect to button
    button.setOnMouseClicked(e -> {
      buttonType.handleClick();
    });


    return button;
  }

  /**
   * An enum for the possible buttons in the quick menu.
   */
  public enum ButtonType {
    SETTINGS,
    EXIT;

    /**
     * Method responsible for handling what happens when someone clicks a button with
     * the type of the implicit argument.
     */
    public void handleClick() {
      if (this == EXIT) {
        GameScreen.exitGame();
        LobbyScreen.initLobby();
      } else if (this == SETTINGS) {
        SettingsScreen.initUi(true);
      }
    }
  }


}