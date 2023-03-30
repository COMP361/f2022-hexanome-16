package com.hexanome16.client.utils;

import static com.hexanome16.client.Config.CURSIVE_FONT_FACTORY;

import com.almasb.fxgl.ui.FXGLButton;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * This class has some utility function for creating UI elements.
 */
public class UiUtils {
  private UiUtils() {
    super();
  }

  /**
   * Create a text message.
   *
   * @param text the text to display
   * @param size the size of the text
   * @param color the color of the text
   * @return the text message
   */
  public static Text createMessage(String text, double size, String color) {
    Text message = new Text(text);
    message.setFont(CURSIVE_FONT_FACTORY.newFont(size));
    message.setFill(Paint.valueOf(color));
    message.setStrokeWidth(2.);
    message.setStroke(Paint.valueOf("#936D35"));
    message.setStyle("-fx-background-color: ffffff00; ");
    return message;
  }

  /**
   * Create a button.
   *
   * @param message the message to display on the button
   * @return the button
   */
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

  /**
   * Create a login box.
   *
   * @return the rectangle
   */
  public static Rectangle createLogin() {
    Rectangle loginScreen = new Rectangle();
    loginScreen.setWidth(720);
    loginScreen.setHeight(420);
    loginScreen.setArcHeight(50.0);
    loginScreen.setArcWidth(50.0);
    loginScreen.setFill(Paint.valueOf("#936D35"));
    loginScreen.setOpacity(0.2);
    return loginScreen;
  }

  /**
   * Animate the login box.
   *
   * @param loginBox the login box
   * @param duration the duration of the animation
   */
  public static void animateLoginBox(Rectangle loginBox, int duration) {
    animateLoginElement(loginBox, duration);

    FadeTransition ft = new FadeTransition(Duration.millis(duration), loginBox);
    ft.setFromValue(0.2);
    ft.setToValue(0.5);
    ft.setCycleCount(1);
    ft.setAutoReverse(false);
    ft.setDelay(Duration.millis(duration / 2.0));
    ft.play();
  }

  /**
   * Animate a login element.
   *
   * @param node the node to animate
   * @param duration the duration of the animation
   */
  public static void animateLoginElement(Node node, int duration) {
    ScaleTransition st = new ScaleTransition(Duration.millis(duration), node);
    st.setAutoReverse(false);
    st.setCycleCount(1);
    st.setByX(1.05);
    st.setByY(1.05);
    st.setToX(1.0);
    st.setToY(1.0);
    st.setFromX(0.1);
    st.setFromY(0.1);
    st.play();
  }
}
