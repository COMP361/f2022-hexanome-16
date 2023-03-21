package com.hexanome16.client.screens.register;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.hexanome16.client.Config;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;
import com.hexanome16.client.utils.UiUtils;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class RegisterFactory {
  @Spawns("background")
  public Entity background(SpawnData data) {
    return entityBuilder(data)
        .type(EntityType.BACKGROUND)
        .viewWithBBox(new Rectangle(1920, 1080, Config.PRIMARY_COLOR))
        .at(0, 0)
        .build();
  }

  @Spawns("closeButton")
  public Entity closeButton(SpawnData data) {
    Button button = new Button("X");
    button.setStyle(
        "-fx-background-color: transparent; -fx-text-fill: #CFFBE7;"
            + "-fx-border-color: #CFFBE7; -fx-font-size: 24px; -fx-border-width: 1px;"
            + "-fx-border-radius: 100%; -fx-background-radius: 100%; -fx-padding: 4px;"
            + "-fx-font-weight: bold; -fx-min-width: 48px; -fx-min-height: 48px;");
    button.setOnAction(event -> {
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
}
