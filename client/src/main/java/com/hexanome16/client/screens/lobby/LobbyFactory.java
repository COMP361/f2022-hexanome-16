package com.hexanome16.client.screens.lobby;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.ui.FontFactory;
import com.hexanome16.client.Config;
import com.hexanome16.client.requests.lobbyservice.gameservices.ListGameServicesRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.CreateSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.DeleteSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.JoinSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.LaunchSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.LeaveSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.ListSessionsRequest;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;
import com.hexanome16.client.screens.settings.SettingsScreen;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.BackgroundService;
import com.hexanome16.common.dto.GameServiceJson;
import com.hexanome16.common.models.sessions.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 * This class is used to create the entities of the lobby screen.
 */
public class LobbyFactory implements EntityFactory {
  private static final AtomicBoolean shouldFetch = new AtomicBoolean(true);
  private static final AtomicReference<Map<String, Session>> sessions =
      new AtomicReference<>(new HashMap<>());
  private static final AtomicReference<String> hashCode = new AtomicReference<>("");
  private static final AtomicReference<BackgroundService> fetchSessionsService =
      new AtomicReference<>(null);
  private static TableView<Map.Entry<String, Session>> activeSessionList;
  private static TableView<Map.Entry<String, Session>> otherSessionList;
  private static final AtomicReference<GameServiceJson[]> gameServices =
      new AtomicReference<>(new GameServiceJson[0]);
  private static final AtomicReference<BackgroundService> fetchGameServersService
      = new AtomicReference<>(null);
  private static final AtomicReference<String> selectedGameService = new AtomicReference<>("");
  private static ComboBox<GameServiceJson> gameServiceDropdown;

  /**
   * This method updates the list of sessions shown in the lobby screen.
   */
  public static void updateSessionList() {
    if (activeSessionList == null || otherSessionList == null) {
      return;
    }
    activeSessionList.getItems().clear();
    otherSessionList.getItems().clear();

    if (AuthUtils.getPlayer() != null) {
      for (Map.Entry<String, Session> entry : sessions.get().entrySet()) {
        if (Arrays.asList(entry.getValue().getPlayers())
            .contains(AuthUtils.getPlayer().getName())) {
          activeSessionList.getItems().add(entry);
        } else {
          otherSessionList.getItems().add(entry);
        }
      }
    }
  }

  /**
   * This method starts a separate thread that fetches the list of game services from Lobby Service.
   */
  public static void updateGameServicesList() {
    if (gameServices.get() != null && gameServices.get().length > 0) {
      ObservableList<GameServiceJson> newItems = Arrays.stream(gameServices.get()).collect(
          javafx.collections.FXCollections::observableArrayList,
          javafx.collections.ObservableList::add,
          javafx.collections.ObservableList::addAll);
      gameServiceDropdown.setItems(newItems);
    }
  }

  /**
   * This method starts a separate thread that fetches the list of game services from the Lobby
   * Service.
   */
  private static void createFetchGameServicesThread() {
    BackgroundService fetchService = new BackgroundService(() -> {
      gameServices.set(ListGameServicesRequest.execute());
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }, () -> {
      Platform.runLater(LobbyFactory::updateGameServicesList);
      if (shouldFetch.get()) {
        fetchGameServersService.get().restart();
      }
    }, () -> {
      if (shouldFetch.get()) {
        fetchGameServersService.get().restart();
      }
    });
    fetchService.start();
    fetchGameServersService.set(fetchService);
  }

  /**
   * This method starts a separate thread that fetches the list of sessions from the Lobby Service.
   */
  private static void createFetchSessionThread() {
    BackgroundService fetchService = new BackgroundService(() -> {
      Pair<String, Map<String, Session>> sessionList
          = ListSessionsRequest.execute(hashCode.get());
      hashCode.set(sessionList.getKey());
      sessions.set(sessionList.getValue());
      if (sessions.get() == null) {
        hashCode.set("");
        sessions.set(new HashMap<>());
      }
    }, () -> {
      Platform.runLater(LobbyFactory::updateSessionList);
      if (shouldFetch.get()) {
        fetchSessionsService.get().restart();
      }
    }, () -> {
      if (shouldFetch.get()) {
        fetchSessionsService.get().restart();
      }
    });
    fetchService.start();
    fetchSessionsService.set(fetchService);
  }

  /**
   * This method adds the table with sessions where the user is playing to the lobby screen.
   *
   * @param data The data to spawn the entity.
   * @return Session table.
   */
  @Spawns("ownSessionList")
  public Entity ownSessionList(SpawnData data) {
    return sessionList(data, true);
  }

  /**
   * This method adds the table with sessions that the user hasn't joined to the lobby screen.
   *
   * @param data The data to spawn the entity.
   * @return Session table.
   */
  @Spawns("otherSessionList")
  public Entity otherSessionList(SpawnData data) {
    return sessionList(data, false);
  }

  private Entity sessionList(SpawnData data, boolean isActive) {
    if (sessions.get() == null) {
      sessions.set(new HashMap<>());
    }
    Map<String, Session> sessionArr;
    TableView<Map.Entry<String, Session>> sessionTableView = new TableView<>();
    if (isActive) {
      activeSessionList = sessionTableView;
      sessionArr = sessions.get().entrySet().stream().filter(
          session -> Arrays.asList(session.getValue().getPlayers())
              .contains(AuthUtils.getPlayer().getName())
      ).collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
    } else {
      otherSessionList = sessionTableView;
      sessionArr = sessions.get().entrySet().stream().filter(
          session -> !Arrays.asList(session.getValue().getPlayers())
              .contains(AuthUtils.getPlayer().getName())
      ).collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
    }
    sessionTableView.setStyle("-fx-background-color: #000000; -fx-text-fill: #CFFBE7;");

    String columnStyle = "-fx-alignment: CENTER; -fx-background-color: #000000; "
        + "-fx-text-fill: #CFFBE7; -fx-font-size: 16px;";

    TableColumn<Map.Entry<String, Session>, String> creatorColumn = new TableColumn<>("Creator");
    creatorColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().getCreator()));
    creatorColumn.setStyle(columnStyle);

    TableColumn<Map.Entry<String, Session>, String> launchedColumn = new TableColumn<>("Launched");
    launchedColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
        cellData.getValue().getValue().isLaunched() ? "Yes" : "No"
    ));
    launchedColumn.setStyle(columnStyle);

    TableColumn<Map.Entry<String, Session>, String> playersColumn = new TableColumn<>("Players");
    playersColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
        cellData.getValue().getValue().getPlayers().length
            + " / "
            + cellData.getValue().getValue().getGameParameters().getMaxSessionPlayers()
    ));
    playersColumn.setStyle(columnStyle);

    TableColumn<Map.Entry<String, Session>, String> serverColumn = new TableColumn<>("Extensions");
    serverColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
        cellData.getValue().getValue().getGameParameters().getDisplayName()
    ));
    serverColumn.setStyle(columnStyle);

    TableColumn<Map.Entry<String, Session>, String> actionsColumn = new TableColumn<>("Actions");
    actionsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("actions"));
    actionsColumn.setStyle(columnStyle);

    Callback<TableColumn<Map.Entry<String, Session>, String>,
        TableCell<Map.Entry<String, Session>, String>> actionsCellFactory =
        new Callback<>() {
          @Override
          public TableCell<Map.Entry<String, Session>, String> call(
              final TableColumn<Map.Entry<String, Session>, String> param) {
            return new TableCell<>() {
              final Button join = new Button("Join");
              final Button leave = new Button("Leave");
              final Button launch = new Button("Launch");
              final Button delete = new Button("Delete");

              @Override
              public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                  setGraphic(null);
                } else {
                  final Map.Entry<String, Session> sessionEntry
                      = getTableView().getItems().get(getIndex());
                  final Session session = sessionEntry.getValue();
                  boolean isOwn = session.getCreator().equals(AuthUtils.getPlayer().getName());
                  join.setOnAction(event -> {
                    if (session.isLaunched()) {
                      shouldFetch.set(false);
                      fetchSessionsService.get().cancel();
                      LobbyScreen.exitLobby();
                      FXGL.getWorldProperties().setValue("players", session.getPlayers());
                      GameScreen.initGame(Long.parseLong(sessionEntry.getKey()));
                    } else {
                      JoinSessionRequest.execute(Long.parseLong(sessionEntry.getKey()),
                          AuthUtils.getPlayer().getName(), AuthUtils.getAuth().getAccessToken());
                    }
                  });
                  String commonButtonStyle = "-fx-background-color: #282C34; -fx-font-size: 16px;"
                      + "-fx-border-radius: 5px; -fx-background-radius: 5px;";
                  join.setStyle(
                      "-fx-text-fill: white; -fx-border-color: white;" + commonButtonStyle
                  );
                  leave.setOnAction(event -> LeaveSessionRequest.execute(
                      Long.parseLong(sessionEntry.getKey()),
                      AuthUtils.getPlayer().getName(), AuthUtils.getAuth().getAccessToken()));
                  leave.setStyle(
                      "-fx-text-fill: darkcyan; -fx-border-color: darkcyan;" + commonButtonStyle
                  );
                  launch.setOnAction(event -> {
                    LaunchSessionRequest.execute(Long.parseLong(sessionEntry.getKey()),
                        AuthUtils.getAuth().getAccessToken());
                    shouldFetch.set(false);
                    fetchSessionsService.get().cancel();
                    LobbyScreen.exitLobby();
                    FXGL.getWorldProperties().setValue("players", session.getPlayers());
                    GameScreen.initGame(Long.parseLong(sessionEntry.getKey()));
                  });
                  launch.setStyle(
                      "-fx-text-fill: green; -fx-border-color: green;" + commonButtonStyle
                  );
                  delete.setOnAction(event -> DeleteSessionRequest.execute(
                      Long.parseLong(sessionEntry.getKey()),
                      AuthUtils.getAuth().getAccessToken()));
                  delete.setStyle(
                      "-fx-text-fill: red; -fx-border-color: red; " + commonButtonStyle
                  );
                  ArrayList<Button> buttons = new ArrayList<>();
                  if (isActive) {
                    if (!session.isLaunched()) {
                      if (isOwn && session.getPlayers().length
                          >= session.getGameParameters().getMinSessionPlayers()) {
                        buttons.add(launch);
                      }
                      buttons.add(isOwn ? delete : leave);
                    } else {
                      buttons.add(join);
                    }
                  } else if (!session.isLaunched() && session.getPlayers().length
                      < session.getGameParameters().getMaxSessionPlayers()) {
                    buttons.add(join);
                  }
                  HBox buttonBox = new HBox(buttons.toArray(new Button[0]));
                  buttonBox.setStyle("-fx-alignment: CENTER; -fx-spacing: 5px; -fx-padding: 5px;");
                  setGraphic(buttonBox);
                }
                setText(null);
              }
            };
          }
        };
    actionsColumn.setCellFactory(actionsCellFactory);

    Label placeholder = new Label("No sessions found");
    placeholder.setStyle("-fx-text-fill: #CFFBE7; -fx-alignment: CENTER; -fx-font-size: 24px;");
    sessionTableView.setPlaceholder(placeholder);

    sessionTableView.getColumns().add(creatorColumn);
    sessionTableView.getColumns().add(serverColumn);
    sessionTableView.getColumns().add(launchedColumn);
    sessionTableView.getColumns().add(playersColumn);
    sessionTableView.getColumns().add(actionsColumn);

    sessionTableView.resizeColumn(creatorColumn, 240);
    sessionTableView.resizeColumn(serverColumn, 240);
    sessionTableView.resizeColumn(launchedColumn, 238);
    sessionTableView.resizeColumn(playersColumn, 240);
    sessionTableView.resizeColumn(actionsColumn, 240);
    sessionTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    sessionTableView.setFixedCellSize(50);

    sessionTableView.getItems().addAll(sessionArr.entrySet());
    sessionTableView.setPrefSize(getAppWidth() / 6.0 * 5.0, getAppHeight() / 4.0);

    if (fetchSessionsService.get() == null || !fetchSessionsService.get().isRunning()) {
      shouldFetch.set(true);
      createFetchSessionThread();
    }

    if (fetchGameServersService.get() == null) {
      createFetchGameServicesThread();
    }

    return entityBuilder(data)
        .type(isActive ? EntityType.OWN_SESSION_LIST : EntityType.OTHER_SESSION_LIST)
        .viewWithBBox(sessionTableView)
        .at(160, isActive ? 350 : 450 + getAppHeight() / 4.0)
        .build();
  }

  /**
   * Adds a button that allows to create a new session in Lobby Service.
   *
   * @param data The data of the entity.
   * @return Create Session button.
   */
  @Spawns("createSessionButton")
  public Entity createSessionButton(SpawnData data) {
    Button button = new Button("Create Session");
    button.setFont(CURSIVE_FONT_FACTORY.newFont(45));
    button.setTextFill(Config.SECONDARY_COLOR);
    button.setStyle("-fx-background-color: #6495ed;" + "-fx-padding: 10px");
    button.setOnMouseEntered(e -> button.setOpacity(0.7));
    button.setOnMouseExited(e -> button.setOpacity(1.0));
    button.setOnAction(event -> {
      Long sessionId = CreateSessionRequest.execute(
          AuthUtils.getAuth().getAccessToken(),
          AuthUtils.getPlayer().getName(),
          selectedGameService.get(),
          ""
      );
      System.out.println("Created session with id: " + sessionId);
    });
    return entityBuilder(data)
        .type(EntityType.CREATE_SESSION_BUTTON)
        .viewWithBBox(button)
        .at(750 - 15, 200 - 12)
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
    gameServiceDropdown.setPrefSize(250, 60);
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
        selectedGameService.set(selected.getName());
      }
    });
    return entityBuilder(data)
        .type(EntityType.GAME_SERVICE_LIST)
        .viewWithBBox(gameServiceDropdown)
        .at(950, 200)
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
    Text otherSessions = new Text("Other Sessions");
    otherSessions.setFont(CURSIVE_FONT_FACTORY.newFont(300));
    otherSessions.setFill(Paint.valueOf("#FCD828"));
    otherSessions.setStrokeWidth(2.);
    otherSessions.setStroke(Paint.valueOf("#936D35"));
    otherSessions.setStyle("-fx-text-fill: #CFFBE7; -fx-font-size: 50px; -fx-font-weight: bold;");

    return entityBuilder(data)
        .type(EntityType.OTHER_HEADER)
        .viewWithBBox(otherSessions)
        .at(880 - 20, 400 + getAppHeight() / 4.0 + 20)
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
}
