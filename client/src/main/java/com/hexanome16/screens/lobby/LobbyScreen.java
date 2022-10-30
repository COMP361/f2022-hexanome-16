package com.hexanome16.screens.lobby;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.hexanome16.requests.lobbyservice.sessions.ListSessionsRequest;
import com.hexanome16.types.lobby.sessions.Session;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.hexanome16.Config.*;

import static com.almasb.fxgl.dsl.FXGL.loopBGM;

public class LobbyScreen extends GameApplication {
    private final AtomicReference<Map<String, Session>> sessionMap = new AtomicReference<>(new HashMap<>());
    private TableView<Session> activeSessionList;
    private TableView<Session> inactiveSessionList;

    public enum TYPE {
        SESSION_LIST
    }

    @Override
    protected void initSettings(GameSettings settings) {
        // initialize common game / window settings.
        settings.setTitle(APP_TITLE);
        settings.setVersion(APP_VERSION);
        settings.setWidth(APP_WIDTH);
        settings.setHeight(APP_HEIGHT);
        settings.setManualResizeEnabled(true);
    }

    private void spawnSessionList(Session[] sessions, TableView<Session> sessionList) {
        sessionList = new TableView<>();
        sessionList.setStyle("-fx-background-color: #00000000; -fx-text-fill: #ffffff;");

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

        sessionList.setPlaceholder(new Label("No sessions available"));
        sessionList.getColumns().addAll(creatorColumn, launchedColumn, playersColumn, actionsColumn);
        sessionList.getItems().addAll(sessions);
        sessionList.setPrefSize(1600, 70);
        sessionList.resizeColumn(creatorColumn, 320);
        sessionList.resizeColumn(launchedColumn, 318);
        sessionList.resizeColumn(playersColumn, 320);
        sessionList.resizeColumn(actionsColumn, 320);
        sessionList.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        Entity sessionTable = entityBuilder()
                .type(TYPE.SESSION_LIST)
                .viewWithBBox(sessionList)
                .at(160, 300)
                .build();

        getGameWorld().addEntity(sessionTable);
    }

    private void updateSessionList() {
        sessionMap.set(ListSessionsRequest.execute(sessionMap.hashCode()));
        sessionList.getItems().clear();
        if (sessionMap.get() != null) {
            sessionList.getItems().addAll(sessionMap.get().values());
            sessionList.setPrefHeight(30 + sessionMap.get().size() * 41);
        }
    }

    private void spawnHeader() {
        Label header = new Label("Active Sessions");
        header.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        Button closeButton = new Button("X");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff; -fx-border-color: #ffffff; -fx-font-size: 24px; -fx-border-width: 1px; -fx-border-radius: 100%; -fx-background-radius: 100%; -fx-padding: 4px; -fx-font-weight: bold; -fx-min-width: 48px; -fx-min-height: 48px;");
        Entity logoEntity = entityBuilder()
                .viewWithBBox("splendor.png")
                .at(450, 25)
                .scaleOrigin(450, 25)
                .scale(0.2, 0.2)
                .build();
        Entity headerEntity = entityBuilder()
                .viewWithBBox(header)
                .at(895, 225)
                .build();
        Entity closeButtonEntity = entityBuilder()
                .viewWithBBox(closeButton)
                .at(1700, 100)
                .build();
        getGameWorld().addEntity(logoEntity);
        getGameWorld().addEntity(headerEntity);
        getGameWorld().addEntity(closeButtonEntity);
    }

    @Override
    protected void initGame() {
        //loopBGM("bgm.mp3");
        getGameScene().setBackgroundColor(Paint.valueOf("#282C34"));
        Session[] sessions = sessionMap.get() != null ? sessionMap.get().values().toArray(new Session[0]) : new Session[0];
        Session[] activeSessions = Arrays.stream(sessions).filter(Session::launched).toArray(Session[]::new);
        Session[] inactiveSessions = Arrays.stream(sessions).filter(session -> !session.launched()).toArray(Session[]::new);
        runOnce(() -> spawnSessionList(activeSessions, activeSessionList), Duration.ZERO);
        runOnce(() -> spawnSessionList(inactiveSessions, inactiveSessionList), Duration.ZERO);
        runOnce(this::spawnHeader, Duration.ZERO);
        run(this::updateSessionList, Duration.seconds(1));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
