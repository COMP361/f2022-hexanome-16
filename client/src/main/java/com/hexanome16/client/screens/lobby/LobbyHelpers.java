package com.hexanome16.client.screens.lobby;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.client.requests.lobbyservice.gameservices.ListGameServicesRequest;
import com.hexanome16.client.requests.lobbyservice.savegames.CreateSavegameRequest;
import com.hexanome16.client.requests.lobbyservice.savegames.DeleteSavegameRequest;
import com.hexanome16.client.requests.lobbyservice.savegames.GetSavegamesRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.CreateSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.DeleteSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.JoinSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.LaunchSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.LeaveSessionRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.ListSessionsRequest;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.BackgroundService;
import com.hexanome16.common.dto.GameServiceJson;
import com.hexanome16.common.models.sessions.Role;
import com.hexanome16.common.models.sessions.SaveGameJson;
import com.hexanome16.common.models.sessions.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Pair;

class LobbyHelpers {
  /**
   * This method updates the list of sessions shown in the lobby screen.
   */
  static void updateSessionList() {
    if (LobbyFactory.activeSessionList == null || LobbyFactory.otherSessionList == null) {
      return;
    }
    LobbyFactory.activeSessionList.getItems().clear();
    LobbyFactory.otherSessionList.getItems().clear();

    if (AuthUtils.getPlayer() != null) {
      for (Map.Entry<String, Session> entry : LobbyFactory.sessions.get().entrySet()) {
        if (LobbyFactory.selectedGameService.get() != null
            && entry.getValue().getGameParameters().getName().equals(
                LobbyFactory.selectedGameService.get().getName())) {
          if (Arrays.asList(entry.getValue().getPlayers())
              .contains(AuthUtils.getPlayer().getName())) {
            LobbyFactory.activeSessionList.getItems().add(entry);
          } else {
            LobbyFactory.otherSessionList.getItems().add(entry);
          }
        }
      }
    }
  }

  /**
   * This method starts a separate thread that fetches the list of game services from Lobby Service.
   */
  static void updateGameServicesList() {
    if (LobbyFactory.gameServices.get() != null && LobbyFactory.gameServices.get().length > 0) {
      GameServiceJson prevSelection = LobbyFactory.gameServiceDropdown.getSelectionModel()
          .getSelectedItem();
      ObservableList<GameServiceJson> newItems = Arrays.stream(LobbyFactory.gameServices.get())
          .collect(javafx.collections.FXCollections::observableArrayList,
              javafx.collections.ObservableList::add,
              javafx.collections.ObservableList::addAll);
      LobbyFactory.gameServiceDropdown.setItems(newItems);
      if (prevSelection != null && newItems.contains(prevSelection)) {
        LobbyFactory.gameServiceDropdown.getSelectionModel().select(prevSelection);
      }
    }
  }

  static void updateSavegamesList(String gameServer) {
    if (LobbyFactory.saveGameList == null) {
      return;
    }
    LobbyFactory.saveGameList.getItems().clear();
    if (AuthUtils.getPlayer() != null) {
      SaveGameJson[] saveGames = GetSavegamesRequest.execute(gameServer);
      if (saveGames != null) {
        LobbyFactory.saveGameList.getItems().addAll(saveGames);
      }
    }
  }

  /**
   * This method starts a separate thread that fetches the list of game services from the Lobby
   * Service.
   */
  static void createFetchGameServicesThread() {
    if (LobbyFactory.fetchGameServersService.get() == null) {
      BackgroundService fetchService = new BackgroundService(() -> {
        LobbyFactory.gameServices.set(ListGameServicesRequest.execute());
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }, () -> {
        Platform.runLater(LobbyHelpers::updateGameServicesList);
        if (LobbyFactory.shouldFetch.get()) {
          LobbyFactory.fetchGameServersService.get().restart();
        }
      }, () -> {
        if (LobbyFactory.shouldFetch.get()) {
          LobbyFactory.fetchGameServersService.get().restart();
        }
      });
      fetchService.start();
      LobbyFactory.fetchGameServersService.set(fetchService);
    } else {
      LobbyFactory.fetchGameServersService.get().restart();
    }
  }

  /**
   * This method starts a separate thread that fetches the list of sessions from the Lobby Service.
   */
  static void createFetchSessionThread() {
    if (LobbyFactory.fetchSessionsService.get() == null) {
      BackgroundService fetchService = new BackgroundService(() -> {
        Pair<String, Map<String, Session>> sessionList
            = ListSessionsRequest.execute(LobbyFactory.hashCode.get());
        LobbyFactory.hashCode.set(sessionList.getKey());
        LobbyFactory.sessions.set(sessionList.getValue());
        if (LobbyFactory.sessions.get() == null) {
          LobbyFactory.hashCode.set("");
          LobbyFactory.sessions.set(new HashMap<>());
        }
      }, () -> {
        Platform.runLater(LobbyHelpers::updateSessionList);
        if (LobbyFactory.shouldFetch.get()) {
          LobbyFactory.fetchSessionsService.get().restart();
        }
      }, () -> {
        if (LobbyFactory.shouldFetch.get()) {
          LobbyFactory.fetchSessionsService.get().restart();
        }
      });
      fetchService.start();
      LobbyFactory.fetchSessionsService.set(fetchService);
    } else {
      LobbyFactory.fetchSessionsService.get().restart();
    }
  }

  static Entity sessionList(SpawnData data, boolean isActive) {
    if (LobbyFactory.sessions.get() == null) {
      LobbyFactory.sessions.set(new HashMap<>());
    }
    Map<String, Session> sessionArr;
    TableView<Map.Entry<String, Session>> sessionTableView = new TableView<>();

    if (isActive) {
      LobbyFactory.activeSessionList = sessionTableView;
      sessionArr = LobbyFactory.selectedGameService.get() == null ? new HashMap<>()
          : LobbyFactory.sessions.get().entrySet().stream().filter(
            session -> LobbyFactory.selectedGameService.get().getName().equals(
                session.getValue().getGameParameters().getName())
                && Arrays.asList(session.getValue().getPlayers())
                    .contains(AuthUtils.getPlayer().getName())
      ).collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
    } else {
      LobbyFactory.otherSessionList = sessionTableView;
      sessionArr = LobbyFactory.selectedGameService.get() == null ? new HashMap<>()
          : LobbyFactory.sessions.get().entrySet().stream().filter(
            session -> LobbyFactory.selectedGameService.get().getName().equals(
                session.getValue().getGameParameters().getName())
                && !Arrays.asList(session.getValue().getPlayers())
                .contains(AuthUtils.getPlayer().getName())
      ).collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
    }

    Label placeholder;
    if (LobbyFactory.selectedGameService.get() == null) {
      placeholder = new Label("Please select a game service in the dropdown above");
    } else {
      placeholder = new Label("No sessions found");
    }
    placeholder.setStyle("-fx-text-fill: #CFFBE7; -fx-alignment: CENTER; -fx-font-size: 24px;");
    sessionTableView.setPlaceholder(placeholder);

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
              final Button save = new Button("Save");

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
                  boolean isService = AuthUtils.getPlayer().getRole().equals(
                      Role.ROLE_SERVICE.name());
                  join.setOnAction(event -> {
                    if (session.isLaunched()) {
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
                  save.setOnAction(event -> CreateSavegameRequest.execute(
                      session.getGameParameters().getName(),
                      session.getPlayers(),
                      Long.parseLong(sessionEntry.getKey())));
                  save.setStyle(
                      "-fx-text-fill: yellow; -fx-border-color: yellow;" + commonButtonStyle
                  );
                  ArrayList<Button> buttons = new ArrayList<>();
                  if (isActive) {
                    if (!session.isLaunched()) {
                      if (isOwn && session.getPlayers().length
                          >= session.getGameParameters().getMinSessionPlayers()) {
                        buttons.add(launch);
                      }
                      buttons.add((isOwn || isService) ? delete : leave);
                    } else {
                      buttons.add(join);
                      buttons.add(save);
                      if (isService) {
                        buttons.add(delete);
                      }
                    }
                  } else {
                    if (!session.isLaunched() && session.getPlayers().length
                        < session.getGameParameters().getMaxSessionPlayers()) {
                      buttons.add(join);
                    }
                    if (isOwn || isService) {
                      buttons.add(delete);
                    }
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

    return entityBuilder(data)
        .type(isActive ? EntityType.OWN_SESSION_LIST : EntityType.OTHER_SESSION_LIST)
        .viewWithBBox(sessionTableView)
        .at(160, isActive ? 350 : 450 + getAppHeight() / 4.0)
        .build();
  }

  static Entity saveGameList(SpawnData spawnData) {
    TableView<SaveGameJson> savegameTableView = new TableView<>();
    LobbyFactory.saveGameList = savegameTableView;
    savegameTableView.setStyle("-fx-background-color: #000000; -fx-text-fill: #CFFBE7;");
    final SaveGameJson[] saveGames = LobbyFactory.saveGames.get();

    Label placeholder;
    if (LobbyFactory.selectedGameService.get() == null) {
      placeholder = new Label("Please select a game service in the dropdown above");
    } else {
      placeholder = new Label("No savegames found");
    }
    placeholder.setStyle("-fx-text-fill: #CFFBE7; -fx-alignment: CENTER; -fx-font-size: 24px;");
    savegameTableView.setPlaceholder(placeholder);

    String columnStyle = "-fx-alignment: CENTER; -fx-background-color: #000000; "
        + "-fx-text-fill: #CFFBE7; -fx-font-size: 16px;";

    TableColumn<SaveGameJson, String> idColumn = new TableColumn<>("ID");
    idColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSaveGameId()));
    idColumn.setStyle(columnStyle);

    TableColumn<SaveGameJson, String> gameServerColumn = new TableColumn<>("Game Server");
    gameServerColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGameName()));
    gameServerColumn.setStyle(columnStyle);

    TableColumn<SaveGameJson, String> playersColumn = new TableColumn<>("Players");
    playersColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(String.join(", ", cellData.getValue().getPlayers())));
    playersColumn.setStyle(columnStyle);

    TableColumn<SaveGameJson, String> actionsColumn = new TableColumn<>("Actions");
    actionsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("actions"));
    actionsColumn.setStyle(columnStyle);

    Callback<TableColumn<SaveGameJson, String>,
        TableCell<SaveGameJson, String>> actionsCellFactory =
        new Callback<>() {
          @Override
          public TableCell<SaveGameJson, String> call(
              final TableColumn<SaveGameJson, String> param) {
            return new TableCell<>() {
              final Button create = new Button("Create Session");
              final Button delete = new Button("Delete");

              @Override
              public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                  setGraphic(null);
                } else {
                  final SaveGameJson saveGame = getTableView().getItems().get(getIndex());
                  create.setOnAction(event -> {
                    CreateSessionRequest.execute(
                        AuthUtils.getAuth().getAccessToken(),
                        AuthUtils.getPlayer().getName(),
                        saveGame.getGameName(),
                        saveGame.getSaveGameId()
                    );
                  });
                  String commonButtonStyle = "-fx-background-color: #282C34; -fx-font-size: 16px;"
                      + "-fx-border-radius: 5px; -fx-background-radius: 5px;";
                  create.setStyle(
                      "-fx-text-fill: white; -fx-border-color: white;" + commonButtonStyle
                  );
                  delete.setOnAction(event -> DeleteSavegameRequest.execute(
                      saveGame.getGameName(), saveGame.getSaveGameId()));
                  delete.setStyle(
                      "-fx-text-fill: red; -fx-border-color: red; " + commonButtonStyle
                  );
                  HBox buttonBox = new HBox(new Button[] {create, delete});
                  buttonBox.setStyle("-fx-alignment: CENTER; -fx-spacing: 5px; -fx-padding: 5px;");
                  setGraphic(buttonBox);
                }
                setText(null);
              }
            };
          }
        };
    actionsColumn.setCellFactory(actionsCellFactory);

    savegameTableView.getColumns().add(idColumn);
    savegameTableView.getColumns().add(gameServerColumn);
    savegameTableView.getColumns().add(playersColumn);
    savegameTableView.getColumns().add(actionsColumn);

    savegameTableView.resizeColumn(idColumn, 320);
    savegameTableView.resizeColumn(gameServerColumn, 319);
    savegameTableView.resizeColumn(playersColumn, 319);
    savegameTableView.resizeColumn(actionsColumn, 320);
    savegameTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    savegameTableView.setFixedCellSize(50);

    savegameTableView.getItems().addAll(saveGames);
    savegameTableView.setPrefSize(getAppWidth() / 6.0 * 5.0, getAppHeight() / 4.0);

    return entityBuilder(spawnData)
        .type(EntityType.SAVEGAMES_LIST)
        .viewWithBBox(savegameTableView)
        .at(160, 450 + getAppHeight() / 4.0)
        .build();
  }
}
