package com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypeInterface;
import com.hexanome16.client.screens.lobby.LobbyScreen;
import com.hexanome16.client.screens.settings.SettingsScreen;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Pause implements PromptTypeInterface {

  double aWidth = FXGL.getAppWidth() / 6;
  double aHeight = FXGL.getAppHeight() / 2;
  double topleftX = FXGL.getAppWidth() / 2 - aWidth / 2;
  double topleftY = FXGL.getAppHeight() / 2 - aHeight / 2;
  double aButtonWidth = aWidth * 0.75;
  double aButtonHeight = aButtonWidth * 0.25;

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
    VBox ButtonHolder = new VBox();
    ButtonHolder.setAlignment(Pos.CENTER);
    ButtonHolder.setSpacing(aButtonHeight / 2);
    ButtonHolder.setTranslateX(topleftX);
    ButtonHolder.setTranslateY(topleftY);
    ButtonHolder.setPrefSize(aWidth, aHeight);


    addButton(ButtonHolder, ButtonType.SETTINGS);
    addButton(ButtonHolder, ButtonType.EXIT);

    entity.getViewComponent().addChild(ButtonHolder);
  }

  private void addButton(VBox ButtonHolder, ButtonType ButtonLabel) {

    StackPane Button = new StackPane();
    Button.setPrefSize(aButtonWidth, aButtonHeight * 0.2);
    Button.setMaxSize(aButtonWidth, aButtonHeight * 0.2);

    Text ButtonText = new Text(ButtonLabel.toString());
    ButtonText.setFont(Font.font(aButtonHeight * 0.7));
    ButtonText.setWrappingWidth(aButtonWidth * 0.9);
    ButtonText.setTextAlignment(TextAlignment.CENTER);

    Rectangle ButtonShape = new Rectangle(aButtonWidth, aButtonHeight, Color.rgb(249, 161, 89));
    ButtonShape.setStrokeWidth(aButtonHeight * 0.1);
    ButtonShape.setStroke(Color.BLACK);

    Button.setOnMouseEntered(e -> {
      ButtonShape.setStrokeWidth(aButtonHeight * 0.2);
    });
    Button.setOnMouseExited(e -> {
      ButtonShape.setStrokeWidth(aButtonHeight * 0.1);
    });
    Button.setOnMouseClicked(e -> {
      ButtonLabel.handleClick();
    });
    Button.getChildren().addAll(ButtonShape, ButtonText);
    ButtonHolder.getChildren().add(Button);
  }

  public enum ButtonType {
    SETTINGS,
    EXIT;

    public void handleClick() {
      if (this == EXIT) {
        GameScreen.exitGame();
        LobbyScreen.initLobby();
      } else {
        SettingsScreen.initUi(true);
      }
      /////////////////// TO MODIFY TO ADD OPENING SETTING AND EXITING ///////////////////////////
    }

  }


}
