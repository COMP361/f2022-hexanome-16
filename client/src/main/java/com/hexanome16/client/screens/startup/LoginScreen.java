package com.hexanome16.client.screens.startup;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.ui.FXGLButton;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginScreen implements EntityFactory {
  @Spawns("loginscreen")
  public Entity loginscreen(SpawnData data) {
    Rectangle login_screen = createLogin();
    return FXGL.entityBuilder(data)
        .view(login_screen)
        .type(EntityType.LOGIN)
        .build();
  }

  @Spawns("login")
  public Entity text(SpawnData data) {
    Text message = createMessage("Login", 96, "#FCD828");
    return FXGL.entityBuilder(data)
        .view(message)
        .type(EntityType.LOGIN)
        .build();
  }

  @Spawns("usertext")
  public Entity usertext(SpawnData data) {
    Text user = createMessage("Username", 45, "#000000");
    user.setStrokeWidth(1);
    return FXGL.entityBuilder(data)
        .view(user)
        .type(EntityType.LOGIN)
        .build();
  }

  @Spawns("passtext")
  public Entity passtext(SpawnData data) {
    Text password = createMessage("Password", 45, "#000000");
    password.setStrokeWidth(1);
    return FXGL.entityBuilder(data)
        .view(password)
        .type(EntityType.LOGIN)
        .build();
  }

  @Spawns("username")
  public Entity username(SpawnData data) {
    TextField username = new TextField();
    return FXGL.entityBuilder(data)
        .view(username)
        .type(EntityType.LOGIN)
        .build();
  }

  @Spawns("password")
  public Entity password(SpawnData data) {
    PasswordField password = new PasswordField();
    return FXGL.entityBuilder(data)
        .view(password)
        .type(EntityType.LOGIN)
        .build();
  }

  @Spawns("loginbutton")
  public Entity loginbutton(SpawnData data) {
    FXGLButton button = createButton("Login");
    return FXGL.entityBuilder(data)
        .view(button)
        .type(EntityType.LOGIN)
        .build();
  }

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

  private Rectangle createLogin() {
    Rectangle login_screen = new Rectangle();
    login_screen.setWidth(720);
    login_screen.setHeight(420);
    login_screen.setArcHeight(50.0);
    login_screen.setArcWidth(50.0);
    login_screen.setFill(Paint.valueOf("#936D35"));
    login_screen.setOpacity(0.5);
    return login_screen;
  }

  private Text createMessage(String text, double size, String color) {
    Text message = new Text(text);
    message.setFont(Font.font("Brush Script MT", FontWeight.BOLD, size));
    message.setFill(Paint.valueOf(color));
    message.setStrokeWidth(2.);
    message.setStroke(Paint.valueOf("#936D35"));
    message.setStyle("-fx-background-color: ffffff00; ");
    return message;
  }

  private FXGLButton createButton(String message) {
    FXGLButton button = new FXGLButton(message);
    button.setOnMouseClicked(e -> {
      MainMenuScreen.initUI();
    });
    button.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 30));
    button.setPrefSize(130, 50);
    button.setStyle("-fx-background-color: #603232;" +
        "-fx-background-radius: 25px;" +
        "-fx-text-fill: #fff;");
    button.setOpacity(0.95);
    return button;
  }
}
