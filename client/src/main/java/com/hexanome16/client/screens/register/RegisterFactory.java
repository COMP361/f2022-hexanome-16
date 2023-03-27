package com.hexanome16.client.screens.register;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.ui.FXGLButton;
import com.hexanome16.client.Config;
import com.hexanome16.client.requests.lobbyservice.oauth.AuthRequest;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UiUtils;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class RegisterFactory {
  private static String username = "";
  private static String password = "";
  private static String role = "";

  @Spawns("background")
  public Entity background(SpawnData data) {
    return entityBuilder(data)
        .type(EntityType.BACKGROUND)
        .viewWithBBox(new Rectangle(1920, 1080, Config.PRIMARY_COLOR))
        .at(0, 0)
        .build();
  }

  @Spawns("close")
  public Entity closeButton(SpawnData data) {
    Button button = new Button("X");
    button.setStyle(
        "-fx-background-color: transparent; -fx-text-fill: #CFFBE7;"
            + "-fx-border-color: #CFFBE7; -fx-font-size: 24px; -fx-border-width: 1px;"
            + "-fx-border-radius: 100%; -fx-background-radius: 100%; -fx-padding: 4px;"
            + "-fx-font-weight: bold; -fx-min-width: 48px; -fx-min-height: 48px;");
    button.setOnAction(event -> {
      RegisterScreen.exitRegisterScreen();
      MainMenuScreen.initUi();
    });
    return entityBuilder(data)
        .type(EntityType.CLOSE)
        .viewWithBBox(button)
        .at(1700, 100)
        .build();
  }

  @Spawns("message")
  public Entity message(SpawnData data) {
    String text =
        (String) data.getData().getOrDefault("message", "");
    Text message = UiUtils.createMessage(text, 115, "#FCD828");
    return FXGL.entityBuilder(data)
        .view(message)
        .type(EntityType.MESSAGE)
        .build();
  }

  /**
   * Returns the login screen rectangle.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("form")
  public Entity loginScreen(SpawnData data) {
    Rectangle loginScreen = UiUtils.createLogin();
    UiUtils.animateLoginBox(loginScreen, 1000);
    return FXGL.entityBuilder(data)
        .view(loginScreen)
        .type(EntityType.FORM)
        .build();
  }

  /**
   * Returns the login title at the top of the login screen.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("title")
  public Entity register(SpawnData data) {
    Text message = UiUtils.createMessage("Register", 96, "#FCD828");
    UiUtils.animateLoginElement(message, 1000);
    return FXGL.entityBuilder(data)
        .view(message)
        .type(com.hexanome16.client.screens.startup.EntityType.LOGIN)
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
    Text user = UiUtils.createMessage("Username", 45, "#000000");
    user.setStrokeWidth(1);
    UiUtils.animateLoginElement(user, 1000);
    return FXGL.entityBuilder(data)
        .view(user)
        .type(EntityType.USERNAME)
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
    Text password = UiUtils.createMessage("Password", 45, "#000000");
    password.setStrokeWidth(1);
    UiUtils.animateLoginElement(password, 1000);
    return FXGL.entityBuilder(data)
        .view(password)
        .type(EntityType.PASSWORD)
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
    usernameField.setOnKeyTyped(e -> username = usernameField.getText());
    UiUtils.animateLoginElement(usernameField, 1000);
    return FXGL.entityBuilder(data)
        .view(usernameField)
        .type(EntityType.USERNAME)
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
    passwordField.setOnKeyTyped(e -> password = passwordField.getText());
    UiUtils.animateLoginElement(passwordField, 1000);
    return FXGL.entityBuilder(data)
        .view(passwordField)
        .type(EntityType.PASSWORD)
        .build();
  }

  /**
   * Returns the login button.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("submit")
  public Entity loginButton(SpawnData data) {
    FXGLButton button = UiUtils.createButton("Login");
    button.setOnMouseClicked(e -> {
      getGameWorld().removeEntities(getGameWorld().getEntitiesByType(
          com.hexanome16.client.screens.startup.EntityType.MESSAGE));
      AuthRequest.execute(username, password);
      if (AuthUtils.getAuth() == null) {
        spawn("message",
            new SpawnData(getAppWidth() / 3.0 - 200, getAppHeight() - 200)
                .put("message", "Invalid username or password"));
      } else {
        MainMenuScreen.initUi();
      }
    });
    UiUtils.animateLoginElement(button, 1000);
    return FXGL.entityBuilder(data)
        .view(button)
        .type(com.hexanome16.client.screens.startup.EntityType.LOGIN)
        .build();
  }
}
