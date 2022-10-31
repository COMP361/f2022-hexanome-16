package com.hexanome16.screens.lobby;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.hexanome16.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.requests.lobbyservice.sessions.CreateSessionRequest;
import com.hexanome16.requests.lobbyservice.sessions.ListSessionsRequest;
import com.hexanome16.screens.settings.SettingsScreen;
import com.hexanome16.types.lobby.auth.TokensInfo;
import com.hexanome16.types.lobby.sessions.Session;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class LobbyFactory implements EntityFactory {
    private static Map<String, Session> sessionMap = new HashMap<>();
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
        Session[] sessions = sessionMap.values().stream().filter(session -> session.creator().equals("testuser")).toArray(Session[]::new);
        return sessionList(data, sessions, true);
    }

    @Spawns("otherSessionList")
    public Entity otherSessionList(SpawnData data) {
        Session[] sessions = sessionMap.values().stream().filter(session -> !session.creator().equals("testuser")).toArray(Session[]::new);
        return sessionList(data, sessions, false);
    }

    private Entity sessionList(SpawnData data, Session[] sessions, boolean isOwn) {
        TableView<Session> sessionTableView = new TableView<>();
        if (isOwn) {
            ownSessionList = sessionTableView;
        } else {
            otherSessionList = sessionTableView;
        }
        sessionTableView.setStyle("-fx-background-color: #00000000; -fx-text-fill: #ffffff;");

        TableColumn<Session, String> creatorColumn = new TableColumn<>("Creator");
        creatorColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().creator()));
        creatorColumn.setStyle("-fx-alignment: CENTER; -fx-background-color: #000000; -fx-text-fill: #ffffff;");

        TableColumn<Session, String> launchedColumn = new TableColumn<>("Launched");
        launchedColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().launched() ? "Yes" : "No"
        ));
        launchedColumn.setStyle("-fx-alignment: CENTER; -fx-background-color: #000000; -fx-text-fill: #ffffff;");

        TableColumn<Session, String> playersColumn = new TableColumn<>("Players");
        playersColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().players().length + " / " + cellData.getValue().gameParameters().maxSessionPlayers()
        ));
        playersColumn.setStyle("-fx-alignment: CENTER; -fx-background-color: #000000; -fx-text-fill: #ffffff;");

        TableColumn<Session, String> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellValueFactory(new PropertyValueFactory<>("actions"));
        actionsColumn.setStyle("-fx-alignment: CENTER; -fx-background-color: #000000; -fx-text-fill: #ffffff;");

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
                                    setText(null);
                                } else {
                                    join.setOnAction(event -> {
                                        Session session = getTableView().getItems().get(getIndex());
                                        System.out.println(session);
                                    });
                                    HBox buttons = new HBox(join, leave, launch, delete);
                                    buttons.setStyle("-fx-alignment: CENTER; -fx-spacing: 5px; -fx-padding: 5px;");
                                    setGraphic(buttons);
                                    setText(null);
                                }
                            }
                        };
                    }
                };
        actionsColumn.setCellFactory(actionsCellFactory);

        Label placeholder = new Label("No sessions found");
        placeholder.setStyle("-fx-text-fill: #ffffff;");
        sessionTableView.setPlaceholder(placeholder);
        sessionTableView.getColumns().addAll(creatorColumn, launchedColumn, playersColumn, actionsColumn);
        sessionTableView.setPrefSize(1600, 70);
        sessionTableView.resizeColumn(creatorColumn, 320);
        sessionTableView.resizeColumn(launchedColumn, 318);
        sessionTableView.resizeColumn(playersColumn, 320);
        sessionTableView.resizeColumn(actionsColumn, 320);
        sessionTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        sessionTableView.getItems().addAll(sessions);

        return entityBuilder(data)
                .type(isOwn ? TYPE.OWN_SESSION_LIST : TYPE.OTHER_SESSION_LIST)
                .viewWithBBox(sessionTableView)
                .at(160, isOwn ? 300 : 450 + ownSessionList.getHeight())
                .build();
    }

    public static void updateSessionList() {
        sessionMap = ListSessionsRequest.execute(sessionMap.hashCode());
        ownSessionList.getItems().clear();
        otherSessionList.getItems().clear();

        if (sessionMap != null) {
            Session[] ownSessions = sessionMap.values().stream().filter(session -> session.creator().equals("testuser")).toArray(Session[]::new);
            Session[] otherSessions = sessionMap.values().stream().filter(session -> !session.creator().equals("testuser")).toArray(Session[]::new);

            ownSessionList.getItems().addAll(ownSessions);
            otherSessionList.getItems().addAll(otherSessions);
            ownSessionList.setPrefHeight(30 + ownSessions.length * 41);
            otherSessionList.setPrefHeight(30 + otherSessions.length * 41);
        }
    }

    @Spawns("createSessionButton")
    public Entity createSessionButton(SpawnData data) {
        Button button = new Button("Create Session");
        button.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 20px; -fx-padding: 10px;");
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
        });
        return entityBuilder(data)
                .type(TYPE.CREATE_SESSION_BUTTON)
                .viewWithBBox(button)
                .at(850, 150)
                .build();
    };

    @Spawns("ownHeader")
    public Entity ownHeader(SpawnData data) {
        Label label = new Label("Own Sessions");
        label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px;");
        return entityBuilder(data)
                .type(TYPE.OWN_HEADER)
                .viewWithBBox(label)
                .at(850, 250)
                .build();
    }

    @Spawns("otherHeader")
    public Entity otherHeader(SpawnData data) {
        Label label = new Label("Other Sessions");
        label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px;");
        return entityBuilder(data)
                .type(TYPE.OTHER_HEADER)
                .viewWithBBox(label)
                .at(850, ownSessionList.getHeight() + 400)
                .build();
    }

    @Spawns("closeButton")
    public Entity closeButton(SpawnData data) {
        Button button = new Button("Close");
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff; -fx-border-color: #ffffff; -fx-font-size: 24px; -fx-border-width: 1px; -fx-border-radius: 100%; -fx-background-radius: 100%; -fx-padding: 4px; -fx-font-weight: bold; -fx-min-width: 48px; -fx-min-height: 48px;");
        button.setOnAction(event -> {
            LobbyScreen.exitLobby();
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
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff; -fx-border-color: #ffffff; -fx-font-size: 24px; -fx-border-width: 1px; -fx-border-radius: 100%; -fx-background-radius: 100%; -fx-padding: 4px; -fx-font-weight: bold; -fx-min-width: 48px; -fx-min-height: 48px;");
        button.setOnAction(event -> {
            SettingsScreen.initUI(true);
        });
        return entityBuilder(data)
                .type(TYPE.CLOSE_BUTTON)
                .viewWithBBox(button)
                .at(100, 100)
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
