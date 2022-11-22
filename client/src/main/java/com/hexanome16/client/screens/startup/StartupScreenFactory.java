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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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
   */
  @Spawns("message")
  public Entity text(SpawnData data) {
    String text =
        (String) data.getData().getOrDefault("message", "Click diamond to enter the game!");
    Text message = createMessage(text, 115, "#FCD828");
    return FXGL.entityBuilder(data)
        .view(message)
        .type(EntityType.STARTUP)
        .type(EntityType.MESSAGE)
        .build();
  }

  /**
   * Returns the login screen rectangle.
   */
  @Spawns("loginscreen")
  public Entity loginscreen(SpawnData data) {
    Rectangle loginScreen = createLogin();
    return FXGL.entityBuilder(data)
        .view(loginScreen)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the login title at the top of the login screen.
   */
  @Spawns("login")
  public Entity login(SpawnData data) {
    Text message = createMessage("Login", 96, "#FCD828");
    return FXGL.entityBuilder(data)
        .view(message)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the username text.
   */
  @Spawns("usertext")
  public Entity usertext(SpawnData data) {
    Text user = createMessage("Username", 45, "#000000");
    user.setStrokeWidth(1);
    return FXGL.entityBuilder(data)
        .view(user)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the password text.
   */
  @Spawns("passtext")
  public Entity passtext(SpawnData data) {
    Text password = createMessage("Password", 45, "#000000");
    password.setStrokeWidth(1);
    return FXGL.entityBuilder(data)
        .view(password)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the username TextField.
   */
  @Spawns("username")
  public Entity username(SpawnData data) {
    TextField usernameField = new TextField();
    usernameField.setOnKeyTyped(e -> {
      username = usernameField.getText();
    });
    return FXGL.entityBuilder(data)
        .view(usernameField)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the password PasswordField.
   */
  @Spawns("password")
  public Entity password(SpawnData data) {
    PasswordField passwordField = new PasswordField();
    passwordField.setOnKeyTyped(e -> {
      password = passwordField.getText();
    });
    return FXGL.entityBuilder(data)
        .view(passwordField)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the login button.
   */
  @Spawns("loginbutton")
  public Entity loginbutton(SpawnData data) {
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
    return FXGL.entityBuilder(data)
        .view(button)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns the cancel button.
   */
  @Spawns("cancelbutton")
  public Entity cancelbutton(SpawnData data) {
    FXGLButton button = createButton("Cancel");
    button.setOpacity(0.85);
    button.setOnMouseClicked(e -> {
      StartupScreen.backToMainScreen();
    });
    return FXGL.entityBuilder(data)
        .view(button)
        .type(EntityType.LOGIN)
        .build();
  }

  /**
   * Returns a transparent button similar to the cancel button that allows the user to go back to
   * the startup screen by clicking on a blank area outside the login window.
   */
  @Spawns("blankspace")
  public Entity blackspace(SpawnData data) {
    FXGLButton button = createButton();
    button.setPrefSize(getAppWidth(), getAppHeight());
    button.setOnMouseClicked(e -> {
      StartupScreen.backToMainScreen();
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
    loginScreen.setOpacity(0.5);
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
    spawn("blankspace");
    spawn("loginscreen", 630, 320);
    spawn("login", 880, 420);
    spawn("usertext", 830, 525);
    spawn("username", 1000, 505);
    spawn("passtext", 830, 575);
    spawn("password", 1000, 550);
    spawn("loginbutton", 1020, 635);
    spawn("cancelbutton", 820, 635);
  }
}
