package com.hexanome16.client.screens.lobby;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.hexanome16.client.requests.lobbyservice.sessions.CreateSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.DeleteSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.JoinSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.LaunchSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.LeaveSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.ListSessionsRequest;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;
import com.hexanome16.client.screens.settings.SettingsScreen;
import com.hexanome16.client.types.sessions.Session;
import com.hexanome16.client.utils.AuthUtils;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 * This class is used to create the entities of the lobby screen.
 */
public class LobbyFactory implements EntityFactory {
  private static Session[] sessions = new Session[] {};
  private static TableView<Session> activeSessionList;
  private static TableView<Session> otherSessionList;
  private static String hashCode = "";
  private static Thread fetchSessionsThread;
  private static boolean shouldFetch = true;

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
      for (Session session : sessions) {
        if (Arrays.asList(session.getPlayers()).contains(AuthUtils.getPlayer().getName())) {
          activeSessionList.getItems().add(session);
        } else {
          otherSessionList.getItems().add(session);
        }
      }
    }
  }

  /**
   * This method starts a separate thread that fetches the list of sessions from the Lobby Service.
   */
  private static void createFetchSessionThread() {
    if (shouldFetch) {
      Task<Void> fetchSessionsTask = new Task<>() {
        @Override
        protected Void call() throws Exception {
          Pair<String, Session[]> sessionList = ListSessionsRequest.execute(hashCode);
          hashCode = sessionList.getKey();
          sessions = sessionList.getValue();
          if (sessions == null) {
            hashCode = "";
            sessions = new Session[] {};
          }
          return null;
        }
      };
      fetchSessionsTask.setOnSucceeded(e -> {
        fetchSessionsThread = null;
        if (shouldFetch) {
          Platform.runLater(LobbyFactory::updateSessionList);
          createFetchSessionThread();
        }
      });
      fetchSessionsTask.setOnFailed(e -> {
        fetchSessionsThread = null;
        throw new RuntimeException(fetchSessionsTask.getException());
      });
      fetchSessionsTask.setOnCancelled(e -> {
        fetchSessionsThread = null;
      });
      fetchSessionsThread = new Thread(fetchSessionsTask);
      fetchSessionsThread.setDaemon(true);
      fetchSessionsThread.start();
    }
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
    if (sessions == null) {
      sessions = new Session[] {};
    }
    Session[] sessionArr;
    TableView<Session> sessionTableView = new TableView<>();
    if (isActive) {
      activeSessionList = sessionTableView;
      sessionArr = Arrays.stream(sessions).filter(
          session -> Arrays.asList(session.getPlayers()).contains(AuthUtils.getPlayer().getName())
      ).toArray(Session[]::new);
    } else {
      otherSessionList = sessionTableView;
      sessionArr = Arrays.stream(sessions).filter(
          session -> !(Arrays.asList(session.getPlayers())
              .contains(AuthUtils.getPlayer().getName()))
      ).toArray(Session[]::new);
    }
    sessionTableView.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");

    String columnStyle = "-fx-alignment: CENTER; -fx-background-color: #000000; "
        + "-fx-text-fill: #ffffff; -fx-font-size: 16px;";

    TableColumn<Session, String> creatorColumn = new TableColumn<>("Creator");
    creatorColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCreator()));
    creatorColumn.setStyle(columnStyle);

    TableColumn<Session, String> launchedColumn = new TableColumn<>("Launched");
    launchedColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
        cellData.getValue().getLaunched() ? "Yes" : "No"
    ));
    launchedColumn.setStyle(columnStyle);

    TableColumn<Session, String> playersColumn = new TableColumn<>("Players");
    playersColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
        cellData.getValue().getPlayers().length
            + " / "
            + cellData.getValue().getGameParameters().getMaxSessionPlayers()
    ));
    playersColumn.setStyle(columnStyle);

    TableColumn<Session, String> actionsColumn = new TableColumn<>("Actions");
    actionsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("actions"));
    actionsColumn.setStyle(columnStyle);

    Callback<TableColumn<Session, String>, TableCell<Session, String>> actionsCellFactory =
        new Callback<>() {
          @Override
          public TableCell<Session, String> call(final TableColumn<Session, String> param) {
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
                  final Session session = getTableView().getItems().get(getIndex());
                  boolean isOwn = session.getCreator().equals(AuthUtils.getPlayer().getName());
                  join.setOnAction(event -> {
                    if (session.getLaunched()) {
                      shouldFetch = false;
                      fetchSessionsThread.interrupt();
                      fetchSessionsThread = null;
                      LobbyScreen.exitLobby();
                      FXGL.getWorldProperties().setValue("players", session.getPlayers());
                      GameScreen.initGame(session.getId());
                    } else {
                      JoinSessionRequest.execute(session.getId(), AuthUtils.getPlayer().getName(),
                          AuthUtils.getAuth().getAccessToken());
                    }
                  });
                  String commonButtonStyle = "-fx-background-color: #282C34; -fx-font-size: 16px;"
                      + "-fx-border-radius: 5px; -fx-background-radius: 5px;";
                  join.setStyle(
                      "-fx-text-fill: white; -fx-border-color: white;" + commonButtonStyle
                  );
                  leave.setOnAction(event -> {
                    LeaveSessionRequest.execute(session.getId(), AuthUtils.getPlayer().getName(),
                        AuthUtils.getAuth().getAccessToken());
                  });
                  leave.setStyle(
                      "-fx-text-fill: darkcyan; -fx-border-color: darkcyan;" + commonButtonStyle
                  );
                  launch.setOnAction(event -> {
                    LaunchSessionRequest.execute(session.getId(),
                        AuthUtils.getAuth().getAccessToken());
                    shouldFetch = false;
                    fetchSessionsThread.interrupt();
                    fetchSessionsThread = null;
                    LobbyScreen.exitLobby();
                    FXGL.getWorldProperties().setValue("players", session.getPlayers());
                    GameScreen.initGame(session.getId());
                  });
                  launch.setStyle(
                      "-fx-text-fill: green; -fx-border-color: green;" + commonButtonStyle
                  );
                  delete.setOnAction(event -> {
                    DeleteSessionRequest.execute(session.getId(),
                        AuthUtils.getAuth().getAccessToken());
                  });
                  delete.setStyle(
                      "-fx-text-fill: red; -fx-border-color: red; " + commonButtonStyle
                  );
                  ArrayList<Button> buttons = new ArrayList<>();
                  if (isActive) {
                    buttons.add(session.getLaunched() || !isOwn ? join : launch);
                    if (!session.getLaunched()) {
                      buttons.add(isOwn ? delete : leave);
                    }
                  } else if (!session.getLaunched()) {
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
    placeholder.setStyle("-fx-text-fill: #ffffff; -fx-alignment: CENTER; -fx-font-size: 24px;");
    sessionTableView.setPlaceholder(placeholder);

    sessionTableView.getColumns().add(creatorColumn);
    sessionTableView.getColumns().add(launchedColumn);
    sessionTableView.getColumns().add(playersColumn);
    sessionTableView.getColumns().add(actionsColumn);

    sessionTableView.resizeColumn(creatorColumn, 320);
    sessionTableView.resizeColumn(launchedColumn, 318);
    sessionTableView.resizeColumn(playersColumn, 320);
    sessionTableView.resizeColumn(actionsColumn, 320);
    sessionTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    sessionTableView.setFixedCellSize(50);

    sessionTableView.getItems().addAll(sessionArr);
    sessionTableView.setPrefSize(getAppWidth() / 6.0 * 5.0, getAppHeight() / 4.0);

    if (fetchSessionsThread == null) {
      shouldFetch = true;
      createFetchSessionThread();
    }

    return entityBuilder(data)
        .type(isActive ? Type.OWN_SESSION_LIST : Type.OTHER_SESSION_LIST)
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
    button.setStyle(
        "-fx-background-color: #6495ed; -fx-text-fill: #ffffff; -fx-font-size: 24px; "
            + "-fx-padding: 10px;"
    );
    button.setOnAction(event -> {
      String sessionId = CreateSessionRequest.execute(
          AuthUtils.getAuth().getAccessToken(),
          AuthUtils.getPlayer().getName(),
          "Splendor",
          null
      );
      System.out.println("Created session with id: " + sessionId);
      //ownSessionList.setPrefHeight(ownSessionList.getHeight() + 41);
    });
    return entityBuilder(data)
        .type(Type.CREATE_SESSION_BUTTON)
        .viewWithBBox(button)
        .at(870, 200)
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
    Label label = new Label("Active Sessions");
    label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px; -fx-font-weight: bold;");
    return entityBuilder(data)
        .type(Type.OWN_HEADER)
        .viewWithBBox(label)
        .at(870, 300)
        .build();
  }

  /**
   * Adds a header above the table of other players' sessions.
   *
   * @param data The data of the entity.
   * @return Header for the table of other players' sessions.
   */
  @Spawns("otherHeader")
  public Entity otherHeader(SpawnData data) {
    Label label = new Label("Other Sessions");
    label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px; -fx-font-weight: bold;");
    return entityBuilder(data)
        .type(Type.OTHER_HEADER)
        .viewWithBBox(label)
        .at(880, 400 + getAppHeight() / 4.0)
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
        "-fx-background-color: transparent; -fx-text-fill: #ffffff;"
            + "-fx-border-color: #ffffff; -fx-font-size: 24px; -fx-border-width: 1px;"
            + "-fx-border-radius: 100%; -fx-background-radius: 100%; -fx-padding: 4px;"
            + "-fx-font-weight: bold; -fx-min-width: 48px; -fx-min-height: 48px;");
    button.setOnAction(event -> {
      LobbyScreen.exitLobby();
      MainMenuScreen.initUi();
    });
    return entityBuilder(data)
        .type(Type.CLOSE_BUTTON)
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
        .type(Type.LOGO)
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
    button.setStyle(
        "-fx-background-color: transparent; -fx-text-fill: #61dafb;"
            + "-fx-underline: true; -fx-font-size: 24px; -fx-font-weight: bold;");
    button.setOnAction(event -> {
      SettingsScreen.initUi(true);
    });
    return entityBuilder(data)
        .type(Type.CLOSE_BUTTON)
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
        .type(Type.BACKGROUND)
        .viewWithBBox(new Rectangle(1920, 1080, Paint.valueOf("#282C34")))
        .at(0, 0)
        .build();
  }

  /**
   * Enum of possible entities in the lobby screen.
   */
  public enum Type {
    OWN_SESSION_LIST,
    OTHER_SESSION_LIST,
    CREATE_SESSION_BUTTON,
    CLOSE_BUTTON,
    PREFERENCES_BUTTON,
    LOGO,
    OWN_HEADER,
    OTHER_HEADER,
    BACKGROUND
  }
}
