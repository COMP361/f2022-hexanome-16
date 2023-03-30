package com.hexanome16.client.screens.register;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
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
import com.hexanome16.client.Config;
import com.hexanome16.client.MainApp;
import com.hexanome16.client.requests.lobbyservice.oauth.AuthRequest;
import com.hexanome16.client.requests.lobbyservice.user.RegisterUserRequest;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UiUtils;
import com.hexanome16.common.models.sessions.Role;
import java.util.Arrays;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * This class provides methods to create UI elements for the register screen.
 */
public class RegisterFactory implements EntityFactory {
  private static String username = "";
  private static String password = "";
  private static Role role = Role.ROLE_PLAYER;

  /**
   * Returns the register screen background.
   *
   * @param data The spawn data.
   * @return The background entity.
   */
  @Spawns("regBackground")
  public Entity background(SpawnData data) {
    return entityBuilder(data)
        .type(EntityType.BACKGROUND)
        .viewWithBBox(new Rectangle(1920, 1080, Config.PRIMARY_COLOR))
        .at(0, 0)
        .build();
  }

  /**
   * Returns the register screen close button.
   *
   * @param data The spawn data.
   * @return The close button entity.
   */
  @Spawns("regClose")
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

  /**
   * Returns the register screen message.
   *
   * @param data The spawn data.
   * @return The message entity.
   */
  @Spawns("regMessage")
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
  @Spawns("regForm")
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
  @Spawns("regTitle")
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
  @Spawns("regUserText")
  public Entity userText(SpawnData data) {
    Text user = UiUtils.createMessage("Username", 45, "#000000");
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
  @Spawns("regPasswordText")
  public Entity passwordText(SpawnData data) {
    Text password = UiUtils.createMessage("Password", 45, "#000000");
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
  @Spawns("regUsername")
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
  @Spawns("regPassword")
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
   * Returns the role dropdown label.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("regRoleText")
  public Entity roleText(SpawnData data) {
    Text role = UiUtils.createMessage("Role", 45, "#000000");
    UiUtils.animateLoginElement(role, 1000);
    return FXGL.entityBuilder(data)
        .view(role)
        .type(EntityType.ROLE_TEXT)
        .build();
  }

  /**
   * Returns the role dropdown.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("regRole")
  public Entity role(SpawnData data) {
    ComboBox<String> roleField = new ComboBox<>();
    roleField.getItems().addAll("Player", "Service", "Admin");
    roleField.setOnAction(e -> role = Role.fromString(roleField.getValue()));
    roleField.getSelectionModel().select(0);
    UiUtils.animateLoginElement(roleField, 1000);
    return FXGL.entityBuilder(data)
        .view(roleField)
        .type(EntityType.ROLE)
        .build();
  }

  /**
   * Returns the submit button.
   *
   * @param data the data
   * @return the entity
   */
  @Spawns("regSubmit")
  public Entity loginButton(SpawnData data) {
    FXGLButton button = UiUtils.createButton("Register");
    button.setOnMouseClicked(e -> {
      getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.MESSAGE));
      MainApp.errorMessage = "";
      RegisterUserRequest.execute(username, password, role.name(), "#000000");
      if (MainApp.errorMessage.isBlank()) {
        spawn("message", new SpawnData(getAppWidth() / 3.0, getAppHeight() - 200)
            .put("message", "Registration successful!"));
      }
    });
    UiUtils.animateLoginElement(button, 1000);
    return FXGL.entityBuilder(data)
        .view(button)
        .type(EntityType.SUBMIT)
        .build();
  }
}
