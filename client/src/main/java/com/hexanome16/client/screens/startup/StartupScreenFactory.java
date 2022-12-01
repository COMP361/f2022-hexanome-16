package com.hexanome16.client.screens.startup;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.ui.FXGLButton;
import com.almasb.fxgl.ui.FontFactory;
import com.hexanome16.client.requests.lobbyservice.oauth.AuthRequest;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;
import com.hexanome16.client.utils.AuthUtils;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * The StartupScreenFactory contains all entities and methods related to the
 * startup screen and the login screen.
 */
public class StartupScreenFactory implements EntityFactory {
  private static final FontFactory CURSIVE_FONT_FACTORY = FXGL.getAssetLoader()
      .loadFont("BrushScriptMT.ttf");

  private static String username = "";
  private static String password = "";

  /**
   * Returns the image of the full startup screen.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("mainScreen")
  public Entity mainScreen(SpawnData data) {
    var mainScreen = FXGL.texture("splendor_main_screen.jpg");
    return FXGL.entityBuilder(data)
        .view(mainScreen)
        .type(EntityType.STARTUP)
        .build();
  }

  /**
   * Returns the button over the diamond.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("diamond")
  public Entity diamond(SpawnData data) {
    FXGLButton button = createButton();
    return FXGL.entityBuilder(data)
        .view(button)
        .type(EntityType.STARTUP)
        .build();
  }

  /**
   * Returns the message telling the user to click on the diamond.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("message")
  public Entity text(SpawnData data) {
    String text =
        (String) data.getData().getOrDefault("message", "Click diamond to enter the game!");
    Text message = createMessage(text, 115, "#FCD828");
    animateMessage(message, 1500);
    return FXGL.entityBuilder(data)
        .view(message)
        .type(EntityType.STARTUP)
        .type(EntityType.MESSAGE)
        .build();
  }

  /**
   * Returns the login screen rectangle.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("loginScreen")
  public Entity loginScreen(SpawnData data) {
    Rectangle loginScreen = createLogin();
    animateLoginBox(loginScreen, 1000);
    return FXGL.entityBuilder(data)
        .view(loginScreen)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the login title at the top of the login screen.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("login")
  public Entity login(SpawnData data) {
    Text message = createMessage("Login", 96, "#FCD828");
    animateLoginElement(message, 1000);
    return FXGL.entityBuilder(data)
        .view(message)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the username text.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("userText")
  public Entity userText(SpawnData data) {
    Text user = createMessage("Username", 45, "#000000");
    user.setStrokeWidth(1);
    animateLoginElement(user, 1000);
    return FXGL.entityBuilder(data)
        .view(user)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the password text.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("passwordText")
  public Entity passwordText(SpawnData data) {
    Text password = createMessage("Password", 45, "#000000");
    password.setStrokeWidth(1);
    animateLoginElement(password, 1000);
    return FXGL.entityBuilder(data)
        .view(password)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the username TextField.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("username")
  public Entity username(SpawnData data) {
    TextField usernameField = new TextField();
    usernameField.setOnKeyTyped(e -> {
      username = usernameField.getText();
    });
    animateLoginElement(usernameField, 1000);
    return FXGL.entityBuilder(data)
        .view(usernameField)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the password PasswordField.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("password")
  public Entity password(SpawnData data) {
    PasswordField passwordField = new PasswordField();
    passwordField.setOnKeyTyped(e -> {
      password = passwordField.getText();
    });
    animateLoginElement(passwordField, 1000);
    return FXGL.entityBuilder(data)
        .view(passwordField)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the login button.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("loginButton")
  public Entity loginButton(SpawnData data) {
    FXGLButton button = createButton("Login");
    button.setOnMouseClicked(e -> {
      getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.MESSAGE));
      AuthRequest.execute(username, password);
      if (AuthUtils.getAuth() == null) {
        spawn("message",
            new SpawnData(getAppWidth() / 3.0 - 200, getAppHeight() - 200)
                .put("message", "Invalid username or password"));
      } else {
        MainMenuScreen.initUi();
      }
    });
    animateLoginElement(button, 1000);
    return FXGL.entityBuilder(data)
        .view(button)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the cancel button.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("cancelButton")
  public Entity cancelButton(SpawnData data) {
    FXGLButton button = createButton("Cancel");
    button.setOpacity(0.85);
    button.setOnMouseClicked(e -> {
      StartupScreen.backToStartupScreen();
    });
    animateLoginElement(button, 1000);
    return FXGL.entityBuilder(data)
        .view(button)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns a transparent button similar to the cancel button that allows the user to go back to
   * the startup screen by clicking on a blank area outside the login window.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("blankSpace")
  public Entity blackSpace(SpawnData data) {
    FXGLButton button = createButton();
    button.setPrefSize(getAppWidth(), getAppHeight());
    button.setOnMouseClicked(e -> {
      StartupScreen.backToStartupScreen();
    });
    return FXGL.entityBuilder(data)
        .view(button)
        .type(EntityType.LOGIN)
        .build();
  }

  // TODO: replace all magic numbers
  private Rectangle createLogin() {
    Rectangle loginScreen = new Rectangle();
    loginScreen.setWidth(720);
    loginScreen.setHeight(420);
    loginScreen.setArcHeight(50.0);
    loginScreen.setArcWidth(50.0);
    loginScreen.setFill(Paint.valueOf("#936D35"));
    loginScreen.setOpacity(0.2);
    return loginScreen;
  }

  private Text createMessage(String text, double size, String color) {
    Text message = new Text(text);
    message.setFont(CURSIVE_FONT_FACTORY.newFont(size));
    message.setFill(Paint.valueOf(color));
    message.setStrokeWidth(2.);
    message.setStroke(Paint.valueOf("#936D35"));
    message.setStyle("-fx-background-color: ffffff00; ");
    return message;
  }

  private FXGLButton createButton(String message) {
    FXGLButton button = new FXGLButton(message);
    button.setOnMouseClicked(e -> {
      MainMenuScreen.initUi();
    });
    // TODO: set background colour to a better looking one
    button.setOnMouseEntered(e -> {
      button.setStyle("-fx-background-color: #000000;"
          + "-fx-background-radius: 25px;"
          + "-fx-text-fill: #fff;");
    });
    button.setOnMouseExited(e -> {
      button.setStyle("-fx-background-color: #603232;"
          + "-fx-background-radius: 25px;"
          + "-fx-text-fill: #fff;");
    });
    button.setFont(CURSIVE_FONT_FACTORY.newFont(30));
    button.setPrefSize(130, 50);
    button.setStyle("-fx-background-color: #603232;"
        + "-fx-background-radius: 25px;"
        + "-fx-text-fill: #fff;");
    button.setOpacity(0.95);
    return button;
  }

  private FXGLButton createButton() {
    FXGLButton button = new FXGLButton();
    button.setOnMouseClicked(e -> {
      spawnLoginScreen();
    });
    button.setPrefSize(500, 260);
    button.setStyle("-fx-background-color: ffffff00;");
    return button;
  }

  private void spawnLoginScreen() {
    getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.MESSAGE));
    spawn("blankSpace");
    spawn("loginScreen", 630, 320);
    spawn("login", 880, 420);
    spawn("userText", 830, 525);
    spawn("username", 1000, 505);
    spawn("passwordText", 830, 575);
    spawn("password", 1000, 550);
    spawn("loginButton", 1020, 635);
    spawn("cancelButton", 820, 635);
  }

  private void animateMessage(Text message, int duration) {
    ScaleTransition animation = new ScaleTransition(Duration.millis(duration), message);
    animation.setAutoReverse(true);
    animation.setCycleCount(500);
    animation.setByX(1.05);
    animation.setByY(1.05);
    animation.setToX(1.3);
    animation.setToY(1.3);
    animation.play();

    FadeTransition ft = new FadeTransition(Duration.millis(duration), message);
    ft.setFromValue(1.0);
    ft.setToValue(0.3);
    ft.setCycleCount(500);
    ft.setAutoReverse(true);
    ft.setDelay(Duration.millis(duration));
    ft.play();
  }

  private void animateLoginBox(Rectangle loginBox, int duration) {
    ScaleTransition st = new ScaleTransition(Duration.millis(duration), loginBox);
    st.setAutoReverse(false);
    st.setCycleCount(1);
    st.setByX(1.05);
    st.setByY(1.05);
    st.setToX(1.0);
    st.setToY(1.0);
    st.setFromX(0.1);
    st.setFromY(0.1);
    st.play();

    FadeTransition ft = new FadeTransition(Duration.millis(duration), loginBox);
    ft.setFromValue(0.2);
    ft.setToValue(0.5);
    ft.setCycleCount(1);
    ft.setAutoReverse(false);
    ft.setDelay(Duration.millis(duration / 2));
    ft.play();
  }

  private void animateLoginElement(Node node, int duration) {
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
