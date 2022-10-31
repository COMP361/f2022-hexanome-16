package com.hexanome16.screens.lobby;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.Spawns;
import com.hexanome16.requests.lobbyservice.sessions.ListSessionsRequest;
import com.hexanome16.types.lobby.sessions.Session;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class LobbyScreen extends GameApplication implements EntityFactory {
    private static Map<String, Session> sessionMap = new HashMap<>();
    private static TableView<Session> ownSessionList;
    private static TableView<Session> otherSessionList;

    public enum TYPE {
        SESSION_LIST
    }

    private static void spawnSessionList(Session[] sessions, boolean isOwn) {
        TableView<Session> sessionList = new TableView<>();
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

        Label placeholder = new Label("No sessions found");
        placeholder.setStyle("-fx-text-fill: #ffffff;");
        sessionList.setPlaceholder(placeholder);
        sessionList.getColumns().addAll(creatorColumn, launchedColumn, playersColumn, actionsColumn);
        sessionList.getItems().addAll(sessions);
        sessionList.setPrefSize(1600, 70);
        sessionList.resizeColumn(creatorColumn, 320);
        sessionList.resizeColumn(launchedColumn, 318);
        sessionList.resizeColumn(playersColumn, 320);
        sessionList.resizeColumn(actionsColumn, 320);
        sessionList.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        if (isOwn) {
            ownSessionList = sessionList;
        } else {
            otherSessionList = sessionList;
        }

        Entity sessionTable = entityBuilder()
                .type(TYPE.SESSION_LIST)
                .viewWithBBox(sessionList)
                .at(160, isOwn ? 300 : 450 + sessionList.getHeight())
                .build();

        getGameWorld().addEntity(sessionTable);
    }

    private static void updateSessionList() {
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

    private static void spawnHeader() {
        Label ownHeader = new Label("Your Sessions");
        ownHeader.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        Label otherHeader = new Label("Other Sessions");
        otherHeader.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        Button closeButton = new Button("X");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff; -fx-border-color: #ffffff; -fx-font-size: 24px; -fx-border-width: 1px; -fx-border-radius: 100%; -fx-background-radius: 100%; -fx-padding: 4px; -fx-font-weight: bold; -fx-min-width: 48px; -fx-min-height: 48px;");
        Entity logoEntity = entityBuilder()
                .viewWithBBox("splendor.png")
                .at(450, 25)
                .scaleOrigin(450, 25)
                .scale(0.2, 0.2)
                .build();
        Entity ownHeaderEntity = entityBuilder()
                .viewWithBBox(ownHeader)
                .at(850, 225)
                .build();
        Entity otherHeaderEntity = entityBuilder()
                .viewWithBBox(otherHeader)
                .at(850, ownSessionList.getHeight() + 400)
                .build();
        Entity closeButtonEntity = entityBuilder()
                .viewWithBBox(closeButton)
                .at(1700, 100)
                .build();
        getGameWorld().addEntity(logoEntity);
        getGameWorld().addEntity(ownHeaderEntity);
        getGameWorld().addEntity(otherHeaderEntity);
        getGameWorld().addEntity(closeButtonEntity);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1920);
        gameSettings.setHeight(1080);
        gameSettings.setTitle("Splendor");
        gameSettings.setVersion("0.1");
    }

    @Override
    protected void initGame() {
        super.initGame();
        getGameScene().setBackgroundColor(Paint.valueOf("#282C34"));
        runOnce(() -> spawnSessionList(new Session[]{}, true), Duration.ZERO);
        runOnce(() -> spawnSessionList(new Session[]{}, false), Duration.ZERO);
        runOnce(LobbyScreen::spawnHeader, Duration.ZERO);
        run(LobbyScreen::updateSessionList, Duration.seconds(1));
    }

    public static void main(String[] args) {
        //loopBGM("bgm.mp3");
        launch(args);
    }
}
