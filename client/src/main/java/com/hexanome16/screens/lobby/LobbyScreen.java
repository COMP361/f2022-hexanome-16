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
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.hexanome16.Config.*;

//import static com.almasb.fxgl.dsl.FXGL.loopBGM;

public class LobbyScreen extends GameApplication {
    private final AtomicReference<Map<String, Session>> sessionMap = new AtomicReference<>(new HashMap<>());

    public enum TYPE {
        SESSION
    }

    @Override
    protected void initSettings(GameSettings settings) {
        // initialize common game / window settings.
        settings.setTitle(APP_TITLE);
        settings.setVersion(APP_VERSION);
        settings.setWidth(APP_WIDTH);
        settings.setHeight(APP_HEIGHT);
    }

    private void spawnSessionList() {
        TableView<Session> sessionList = new TableView<>();

        TableColumn<Session, String> creatorColumn = new TableColumn<>("Creator");
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator"));

        TableColumn<Session, String> launchedColumn = new TableColumn<>("Launched");
        launchedColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().launched() ? "Yes" : "No"
        ));

        TableColumn<Session, String> playersColumn = new TableColumn<>("Players");
        playersColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().players().length + " / " + cellData.getValue().gameParameters().maxSessionPlayers()
        ));

        TableColumn<Session, String> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellValueFactory(new PropertyValueFactory<>("actions"));

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

        sessionList.getItems().addAll(sessionMap.get().values());

        Entity sessionTable = entityBuilder()
                .type(TYPE.SESSION)
                .viewWithBBox(sessionList)
                .at(50, 50)
                .buildAndAttach();

        getGameWorld().addEntity(sessionTable);
    }

    @Override
    protected void initGame() {
        spawnSessionList();
        runOnce(() -> {
            sessionMap.set(ListSessionsRequest.execute(0));
        }, Duration.seconds(0.5));
        run(() -> {
            sessionMap.set(ListSessionsRequest.execute(sessionMap.hashCode()));
        }, Duration.INDEFINITE);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
