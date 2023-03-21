package com.hexanome16.client.utils;

import static com.hexanome16.client.Config.CURSIVE_FONT_FACTORY;

import com.almasb.fxgl.ui.FXGLButton;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class UiUtils {
  private UiUtils() {
    super();
  }

  public static Text createMessage(String text, double size, String color) {
    Text message = new Text(text);
    message.setFont(CURSIVE_FONT_FACTORY.newFont(size));
    message.setFill(Paint.valueOf(color));
    message.setStrokeWidth(2.);
    message.setStroke(Paint.valueOf("#936D35"));
    message.setStyle("-fx-background-color: ffffff00; ");
    return message;
  }

  public static FXGLButton createButton(String message) {
    FXGLButton button = new FXGLButton(message);
    button.setOnMouseClicked(e -> MainMenuScreen.initUi());
    // TODO: set background colour to a better looking one
    button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #000000;"
        + "-fx-background-radius: 25px;"
        + "-fx-text-fill: #fff;"));
    button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #603232;"
        + "-fx-background-radius: 25px;"
        + "-fx-text-fill: #fff;"));
    button.setFont(CURSIVE_FONT_FACTORY.newFont(30));
    button.setPrefSize(130, 50);
    button.setStyle("-fx-background-color: #603232;"
        + "-fx-background-radius: 25px;"
        + "-fx-text-fill: #fff;");
    button.setOpacity(0.95);
    return button;
  }
}
