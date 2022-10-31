package com.hexanome16.screens.lobby;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.hexanome16.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.requests.lobbyservice.sessions.*;
import com.hexanome16.screens.game.GameScreen;
import com.hexanome16.screens.mainmenu.MainMenuScreen;
import com.hexanome16.screens.settings.SettingsScreen;
import com.hexanome16.types.lobby.auth.TokensInfo;
import com.hexanome16.types.lobby.sessions.Session;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class LobbyFactory implements EntityFactory {
    private static Session[] sessions = new Session[]{};
    private static TableView<Session> ownSessionList;
    private static TableView<Session> otherSessionList;

    public enum TYPE {
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

    @Spawns("ownSessionList")
    public Entity ownSessionList(SpawnData data) {
        Session[] activeSessions = Arrays.stream(sessions).filter(session -> session.creator().equals("testuser") || Arrays.asList(session.players()).contains("testuser")).toArray(Session[]::new);
        return sessionList(data, activeSessions, true);
    }

    @Spawns("otherSessionList")
    public Entity otherSessionList(SpawnData data) {
        Session[] otherSessions = Arrays.stream(sessions).filter(session -> !(session.creator().equals("testuser") || Arrays.asList(session.players()).contains("testuser"))).toArray(Session[]::new);
        return sessionList(data, otherSessions, false);
    }

    private Entity sessionList(SpawnData data, Session[] sessionArr, boolean isOwn) {
        TableView<Session> sessionTableView = new TableView<>();
        if (isOwn) {
            ownSessionList = sessionTableView;
        } else {
            otherSessionList = sessionTableView;
        }
        sessionTableView.setStyle("-fx-background-color: #00000000; -fx-text-fill: #ffffff;");

        TableColumn<Session, String> creatorColumn = new TableColumn<>("Creator");
        creatorColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().creator()));
        creatorColumn.setStyle("-fx-alignment: CENTER; -fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 16px;");

        TableColumn<Session, String> launchedColumn = new TableColumn<>("Launched");
        launchedColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().launched() ? "Yes" : "No"
        ));
        launchedColumn.setStyle("-fx-alignment: CENTER; -fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 16px;");

        TableColumn<Session, String> playersColumn = new TableColumn<>("Players");
        playersColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().players().length + " / " + cellData.getValue().gameParameters().maxSessionPlayers()
        ));
        playersColumn.setStyle("-fx-alignment: CENTER; -fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 16px;");

        TableColumn<Session, String> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("actions"));
        actionsColumn.setStyle("-fx-alignment: CENTER; -fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 16px;");

        Callback<TableColumn<Session, String>, TableCell<Session, String>> actionsCellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Session, String> call(final TableColumn<Session, String> param) {
                        return new TableCell<>() {
                            final Button join = new Button(isOwn ? "Continue" : "Join");
                            final Button leave = new Button("Leave");
                            final Button launch = new Button("Launch");
                            final Button delete = new Button("Delete");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    final Session session = getTableView().getItems().get(getIndex());
                                    join.setOnAction(event -> {
                                        TokensInfo tokensInfo = TokenRequest.execute("testuser", "testpass", null);
                                        assert tokensInfo != null;
                                        JoinSessionRequest.execute(session.id(), "testuser", tokensInfo.access_token());
                                        if (session.launched()) {
                                            GameScreen.initGame();
                                            LobbyScreen.exitLobby();
                                        }
                                    });
                                    join.setStyle("-fx-background-color: #282C34; -fx-text-fill: white; -fx-border-color: white; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                                    leave.setOnAction(event -> {
                                        TokensInfo tokensInfo = TokenRequest.execute("testuser", "testpass", null);
                                        assert tokensInfo != null;
                                        LeaveSessionRequest.execute(session.id(), "testuser", tokensInfo.access_token());
                                    });
                                    leave.setStyle("-fx-background-color: #282C34; -fx-text-fill: darkcyan; -fx-border-color: darkcyan; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                                    launch.setOnAction(event -> {
                                        TokensInfo tokensInfo = TokenRequest.execute("testuser", "testpass", null);
                                        assert tokensInfo != null;
                                        LaunchSessionRequest.execute(session.id(), tokensInfo.access_token());
                                        GameScreen.initGame();
                                        LobbyScreen.exitLobby();
                                    });
                                    launch.setStyle("-fx-background-color: #282C34; -fx-text-fill: green; -fx-border-color: green; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                                    delete.setOnAction(event -> {
                                        TokensInfo tokensInfo = TokenRequest.execute("testuser", "testpass", null);
                                        assert tokensInfo != null;
                                        DeleteSessionRequest.execute(session.id(), tokensInfo.access_token());
                                    });
                                    delete.setStyle("-fx-background-color: #282C34; -fx-text-fill: red; -fx-border-color: red; -fx-font-size: 16px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                                    ArrayList<Button> buttons = new ArrayList<>();
                                    if (isOwn) {
                                        buttons.add(session.launched() ? join : launch);
                                        buttons.add(session.launched() ? leave : delete);
                                    } else if (Arrays.asList(session.players()).contains("testuser")) {
                                        buttons.add(leave);
                                    } else {
                                        buttons.add(join);
                                    }
                                    HBox buttonBox = new HBox(buttons.toArray(new Button[0]));
                                    buttonBox.setStyle("-fx-alignment: CENTER; -fx-spacing: 5px; -fx-padding: 5px;");
                                    setGraphic(buttonBox);
                                    setText(null);
                                }
                            }
                        };
                    }
                };
        actionsColumn.setCellFactory(actionsCellFactory);

        Label placeholder = new Label("No sessions found");
        placeholder.setStyle("-fx-text-fill: #ffffff; -fx-alignment: CENTER; -fx-font-size: 16px;");
        sessionTableView.setPlaceholder(placeholder);

        sessionTableView.getColumns().addAll(creatorColumn, launchedColumn, playersColumn, actionsColumn);
        sessionTableView.setPrefSize(1600, sessionArr.length == 0 ? 88 : 36 + sessionArr.length * 52);
        sessionTableView.resizeColumn(creatorColumn, 320);
        sessionTableView.resizeColumn(launchedColumn, 318);
        sessionTableView.resizeColumn(playersColumn, 320);
        sessionTableView.resizeColumn(actionsColumn, 320);
        sessionTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        sessionTableView.getItems().addAll(sessionArr);

        return entityBuilder(data)
                .type(isOwn ? TYPE.OWN_SESSION_LIST : TYPE.OTHER_SESSION_LIST)
                .viewWithBBox(sessionTableView)
                .at(160, isOwn ? 350 : 550 + ownSessionList.getHeight())
                .build();
    }

    public static void updateSessionList() {
        sessions = ListSessionsRequest.execute(sessions == null ? 0 : Arrays.hashCode(sessions));
        ownSessionList.getItems().clear();
        otherSessionList.getItems().clear();

        if (sessions != null) {
            Session[] ownSessions = Arrays.stream(sessions).filter(session -> session.creator().equals("testuser")).toArray(Session[]::new);
            Session[] otherSessions = Arrays.stream(sessions).filter(session -> !session.creator().equals("testuser")).toArray(Session[]::new);

            ownSessionList.getItems().addAll(ownSessions);
            otherSessionList.getItems().addAll(otherSessions);
            ownSessionList.setPrefHeight(36 + ownSessions.length * 52);
            otherSessionList.setPrefHeight(36 + otherSessions.length * 52);
            getGameWorld().getEntitiesByType(TYPE.OTHER_HEADER).forEach(entity -> entity.setY(400 + ownSessionList.getHeight()));
            getGameWorld().getEntitiesByType(TYPE.OTHER_SESSION_LIST).forEach(entity -> entity.setY(450 + ownSessionList.getHeight()));
        }
    }

    @Spawns("createSessionButton")
    public Entity createSessionButton(SpawnData data) {
        Button button = new Button("Create Session");
        button.setStyle("-fx-background-color: #6495ed; -fx-text-fill: #ffffff; -fx-font-size: 24px; -fx-padding: 10px;");
        button.setOnAction(event -> {
            TokensInfo tokensInfo = TokenRequest.execute("testuser", "testpass", null);
            assert tokensInfo != null;
            String sessionId = CreateSessionRequest.execute(
                    tokensInfo.access_token(),
                    "testuser",
                    "Splendor",
                    null
            );
            System.out.println(sessionId);
            ownSessionList.setPrefHeight(ownSessionList.getHeight() + 41);
        });
        return entityBuilder(data)
                .type(TYPE.CREATE_SESSION_BUTTON)
                .viewWithBBox(button)
                .at(870, 200)
                .build();
    };

    @Spawns("ownHeader")
    public Entity ownHeader(SpawnData data) {
        Label label = new Label("Active Sessions");
        label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px; -fx-font-weight: bold;");
        return entityBuilder(data)
                .type(TYPE.OWN_HEADER)
                .viewWithBBox(label)
                .at(870, 300)
                .build();
    }

    @Spawns("otherHeader")
    public Entity otherHeader(SpawnData data) {
        Label label = new Label("Other Sessions");
        label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px; -fx-font-weight: bold;");
        return entityBuilder(data)
                .type(TYPE.OTHER_HEADER)
                .viewWithBBox(label)
                .at(880, ownSessionList.getHeight() + 500)
                .build();
    }

    @Spawns("closeButton")
    public Entity closeButton(SpawnData data) {
        Button button = new Button("X");
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff; -fx-border-color: #ffffff; -fx-font-size: 24px; -fx-border-width: 1px; -fx-border-radius: 100%; -fx-background-radius: 100%; -fx-padding: 4px; -fx-font-weight: bold; -fx-min-width: 48px; -fx-min-height: 48px;");
        button.setOnAction(event -> {
            LobbyScreen.exitLobby();
            MainMenuScreen.initUI();
        });
        return entityBuilder(data)
                .type(TYPE.CLOSE_BUTTON)
                .viewWithBBox(button)
                .at(1700, 100)
                .build();
    }

    @Spawns("logo")
    public Entity logo(SpawnData data) {
        return entityBuilder(data)
                .type(TYPE.LOGO)
                .viewWithBBox("splendor.png")
                .at(450, 25)
                .scaleOrigin(450, 25)
                .scale(0.2, 0.2)
                .build();
    }

    @Spawns("preferencesButton")
    public Entity preferencesButton(SpawnData data) {
        Button button = new Button("Preferences");
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #61dafb; -fx-underline: true; -fx-font-size: 24px; -fx-font-weight: bold;");
        button.setOnAction(event -> {
            SettingsScreen.initUI(true);
        });
        return entityBuilder(data)
                .type(TYPE.CLOSE_BUTTON)
                .viewWithBBox(button)
                .at(150, 90)
                .build();
    }

    @Spawns("background")
    public Entity background(SpawnData data) {
        return entityBuilder(data)
                .type(TYPE.BACKGROUND)
                .viewWithBBox(new Rectangle(1920, 1080, Paint.valueOf("#282C34")))
                .at(0, 0)
                .build();
    }
}
