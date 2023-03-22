package com.hexanome16.client.screens.lobby;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.ui.FontFactory;
import com.hexanome16.client.Config;
import com.hexanome16.client.MainApp;
import com.hexanome16.client.requests.lobbyservice.sessions.CreateSessionRequest;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;
import com.hexanome16.client.screens.settings.SettingsScreen;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.BackgroundService;
import com.hexanome16.common.dto.GameServiceJson;
import com.hexanome16.common.models.sessions.SaveGameJson;
import com.hexanome16.common.models.sessions.Session;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * This class is used to create the entities of the lobby screen.
 */
public class LobbyFactory implements EntityFactory {
  static final AtomicBoolean shouldFetch = new AtomicBoolean(true);
  static final AtomicReference<Map<String, Session>> sessions =
      new AtomicReference<>(new HashMap<>());
  static final AtomicReference<String> hashCode = new AtomicReference<>("");
  static final AtomicReference<BackgroundService> fetchSessionsService =
      new AtomicReference<>(null);
  static TableView<Map.Entry<String, Session>> activeSessionList;
  static TableView<Map.Entry<String, Session>> otherSessionList;
  static final AtomicReference<GameServiceJson[]> gameServices =
      new AtomicReference<>(new GameServiceJson[0]);
  static final AtomicReference<BackgroundService> fetchGameServersService
      = new AtomicReference<>(null);
  static final AtomicReference<GameServiceJson> selectedGameService = new AtomicReference<>(null);
  static ComboBox<GameServiceJson> gameServiceDropdown;
  static CheckBox saveGameCheckbox;
  static final AtomicReference<SaveGameJson[]> saveGames =
      new AtomicReference<>(new SaveGameJson[0]);
  static TableView<SaveGameJson> saveGameList;

  /**
   * This method adds the table with sessions where the user is playing to the lobby screen.
   *
   * @param data The data to spawn the entity.
   * @return Session table.
   */
  @Spawns("ownSessionList")
  public Entity ownSessionList(SpawnData data) {
    return LobbyHelpers.sessionList(data, true);
  }

  /**
   * This method adds the table with sessions that the user hasn't joined to the lobby screen.
   *
   * @param data The data to spawn the entity.
   * @return Session table.
   */
  @Spawns("otherSessionList")
  public Entity otherSessionList(SpawnData data) {
    return LobbyHelpers.sessionList(data, false);
  }

  /**
   * Adds a button that allows to create a new session in Lobby Service.
   *
   * @param data The data of the entity.
   * @return Create Session button.
   */
  @Spawns("createSessionButton")
  public Entity createSessionButton(SpawnData data) {
    Button button = new Button("Create New Session");
    button.setFont(CURSIVE_FONT_FACTORY.newFont(45));
    button.setTextFill(Config.SECONDARY_COLOR);
    button.setStyle("-fx-background-color: #6495ed;" + "-fx-padding: 10px");
    button.setOnMouseEntered(e -> button.setOpacity(0.7));
    button.setOnMouseExited(e -> button.setOpacity(1.0));
    button.setOnAction(event -> {
      if (selectedGameService.get() == null) {
        MainApp.errorMessage =
            "Please select a game service in the dropdown menu to create a session.";
        spawn("PromptBox", new SpawnData().put(
            "promptType", PromptTypeInterface.PromptType.ERROR));
      } else {
        Long sessionId = CreateSessionRequest.execute(
            AuthUtils.getAuth().getAccessToken(),
            AuthUtils.getPlayer().getName(),
            selectedGameService.get().getName(),
            ""
        );
        System.out.println("Created session with id: " + sessionId);
      }
    });
    return entityBuilder(data)
        .type(EntityType.CREATE_SESSION_BUTTON)
        .viewWithBBox(button)
        .at(500, 188)
        .build();
  }

  /**
   * Adds a dropdown menu that allows to select a game service for game creation.
   *
   * @param data The data of the entity.
   * @return Game Service dropdown menu.
   */
  @Spawns("gameServiceList")
  public Entity gameServiceList(SpawnData data) {
    gameServiceDropdown = new ComboBox<>();
    gameServiceDropdown.setPrefSize(300, 60);
    gameServiceDropdown.setStyle("-fx-background-color: #000000; -fx-text-fill: #CFFBE7; "
        + "-fx-border-color: #CFFBE7; -fx-font-size: 24px;");
    Callback<ListView<GameServiceJson>, ListCell<GameServiceJson>> cellFactory = new Callback<>() {
      @Override
      public ListCell<GameServiceJson> call(ListView<GameServiceJson> gameServiceJsonListView) {
        return new ListCell<>() {
          @Override
          protected void updateItem(GameServiceJson gameServiceJson, boolean b) {
            super.updateItem(gameServiceJson, b);
            if (gameServiceJson != null) {
              setText(gameServiceJson.getDisplayName());
            }
          }
        };
      }
    };
    gameServiceDropdown.setButtonCell(cellFactory.call(null));
    gameServiceDropdown.setCellFactory(cellFactory);
    gameServiceDropdown.getItems().addAll(Arrays.stream(gameServices.get()).toList());
    gameServiceDropdown.setOnAction(event -> {
      GameServiceJson selected = gameServiceDropdown.getSelectionModel().getSelectedItem();
      if (selected != null) {
        selectedGameService.set(selected);
        gameServiceDropdown.setPromptText(selected.getDisplayName());
        LobbyHelpers.updateSessionList();
        LobbyHelpers.updateSavegamesList(selected.getName());
      }
    });
    gameServiceDropdown.setPromptText("Select a game service");
    if (selectedGameService.get() != null) {
      gameServiceDropdown.getSelectionModel().select(selectedGameService.get());
    }
    return entityBuilder(data)
        .type(EntityType.GAME_SERVICE_LIST)
        .viewWithBBox(gameServiceDropdown)
        .at(810, 200)
        .build();
  }

  /**
   * Adds a header above the table of active sessions.
   *
   * @param data The data of the entity.
   * @return Header for the table of active sessions.
   */
  @Spawns("ownHeader")
  public Entity ownHeader(SpawnData data) {
    Text activeSessions = new Text("Active Sessions");
    activeSessions.setFont(CURSIVE_FONT_FACTORY.newFont(300));
    activeSessions.setFill(Paint.valueOf("#FCD828"));
    activeSessions.setStrokeWidth(2.);
    activeSessions.setStroke(Paint.valueOf("#936D35"));
    activeSessions.setStyle("-fx-text-fill: #CFFBE7; -fx-font-size: 50px; -fx-font-weight: bold;");

    return entityBuilder(data)
        .type(EntityType.OWN_HEADER)
        .viewWithBBox(activeSessions)
        .at(850, 320)
        .build();
  }

  private static final FontFactory CURSIVE_FONT_FACTORY = Config.CURSIVE_FONT_FACTORY;

  /**
   * Adds a header above the table of other players' sessions.
   *
   * @param data The data of the entity.
   * @return Header for the table of other players' sessions.
   */
  @Spawns("otherHeader")
  public Entity otherHeader(SpawnData data) {
    Text otherSessions = new Text(saveGameCheckbox != null && saveGameCheckbox.isSelected()
        ? "Saved Games" : "Other Sessions");
    otherSessions.setFont(CURSIVE_FONT_FACTORY.newFont(300));
    otherSessions.setFill(Paint.valueOf("#FCD828"));
    otherSessions.setStrokeWidth(2.);
    otherSessions.setStroke(Paint.valueOf("#936D35"));
    otherSessions.setStyle("-fx-text-fill: #CFFBE7; -fx-font-size: 50px; -fx-font-weight: bold;");

    return entityBuilder(data)
        .type(EntityType.OTHER_HEADER)
        .viewWithBBox(otherSessions)
        .at(860, 420 + getAppHeight() / 4.0)
        .build();
  }

  /**
   * Adds a button that allows to exit the lobby screen.
   *
   * @param data The data of the entity.
   * @return Exit Lobby button.
   */
  @Spawns("closeButton")
  public Entity closeButton(SpawnData data) {
    Button button = new Button("X");
    button.setStyle(
        "-fx-background-color: transparent; -fx-text-fill: #CFFBE7;"
            + "-fx-border-color: #CFFBE7; -fx-font-size: 24px; -fx-border-width: 1px;"
            + "-fx-border-radius: 100%; -fx-background-radius: 100%; -fx-padding: 4px;"
            + "-fx-font-weight: bold; -fx-min-width: 48px; -fx-min-height: 48px;");
    button.setOnAction(event -> {
      LobbyScreen.exitLobby();
      MainMenuScreen.initUi();
    });
    return entityBuilder(data)
        .type(EntityType.CLOSE_BUTTON)
        .viewWithBBox(button)
        .at(1700, 100)
        .build();
  }

  /**
   * Adds the Splendor logo to the lobby screen.
   *
   * @param data The data of the entity.
   * @return Splendor logo.
   */
  @Spawns("logo")
  public Entity logo(SpawnData data) {
    return entityBuilder(data)
        .type(EntityType.LOGO)
        .viewWithBBox("splendor.png")
        .at(450, 25)
        .scaleOrigin(450, 25)
        .scale(0.2, 0.2)
        .build();
  }

  /**
   * Adds a button that redirects to the Preferences screen.
   *
   * @param data The data of the entity.
   * @return Preferences button.
   */
  @Spawns("preferencesButton")
  public Entity preferencesButton(SpawnData data) {
    Button button = new Button("Preferences");
    button.setFont(CURSIVE_FONT_FACTORY.newFont(50));
    button.setTextFill(Config.SECONDARY_COLOR);
    button.setStyle("-fx-background-color: transparent;");
    // animation
    button.setOnMouseEntered(e -> {
      button.setScaleX(1.25);
      button.setScaleY(1.25);
    });
    button.setOnMouseExited(e -> {
      button.setScaleX(1);
      button.setScaleY(1);
    });
    button.setOnAction(event -> SettingsScreen.initUi(false));
    return entityBuilder(data)
        .type(EntityType.CLOSE_BUTTON)
        .viewWithBBox(button)
        .at(150, 90)
        .build();
  }

  /**
   * Adds the background color to the lobby screen.
   *
   * @param data The data of the entity.
   * @return Background with color.
   */
  @Spawns("background")
  public Entity background(SpawnData data) {
    return entityBuilder(data)
        .type(EntityType.BACKGROUND)
        .viewWithBBox(new Rectangle(1920, 1080, Config.PRIMARY_COLOR))
        .at(0, 0)
        .build();
  }

  /**
   * Adds a checkbox to the lobby screen that shows saved games (used for session creation).
   *
   * @param data The data of the entity.
   * @return Checkbox to show saved games.
   */
  @Spawns("savegameCheckbox")
  public Entity savegamesCheckbox(SpawnData data) {
    saveGameCheckbox = new CheckBox("Show saved games");
    saveGameCheckbox.setFont(CURSIVE_FONT_FACTORY.newFont(40));
    saveGameCheckbox.setTextFill(Color.WHITE);
    saveGameCheckbox.setStyle("-fx-background-color: transparent;");
    saveGameCheckbox.setSelected(false);
    saveGameCheckbox.setOnAction(event -> {
      getGameWorld().getEntitiesByType(EntityType.OTHER_HEADER)
          .forEach(Entity::removeFromWorld);
      spawn("otherHeader");
      if (saveGameCheckbox.isSelected()) {
        getGameWorld().getEntitiesByType(EntityType.OTHER_SESSION_LIST)
            .forEach(Entity::removeFromWorld);
        spawn("savegamesList");
      } else {
        getGameWorld().getEntitiesByType(EntityType.SAVEGAMES_LIST)
            .forEach(Entity::removeFromWorld);
        spawn("otherSessionList");
      }
    });
    return entityBuilder(data)
        .type(EntityType.SAVEGAME_CHECKBOX)
        .viewWithBBox(saveGameCheckbox)
        .at(1150, 200)
        .build();
  }

  /**
   * The savegame table.
   *
   * @param data The data of the entity.
   * @return Table of saved games.
   */
  @Spawns("savegamesList")
  public Entity savegamesList(SpawnData data) {
    return LobbyHelpers.saveGameList(data);
  }
}
