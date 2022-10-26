package com.hexanome16.screens.lobby;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.scene.Scene;
import com.hexanome16.types.lobby.sessions.Session;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.hexanome16.Config.*;

//import static com.almasb.fxgl.dsl.FXGL.loopBGM;

public class LobbyScreen extends GameApplication {
    public enum TYPE {
        SESSION,
        LIST,
        BUTTON
    }

    @Override
    protected void initSettings(GameSettings settings) {
        // initialize common game / window settings.
        settings.setTitle(APP_TITLE);
        settings.setVersion(APP_VERSION);
        settings.setWidth(APP_WIDTH);
        settings.setHeight(APP_HEIGHT);
    }

    private void spawnSessionList(Map<String, Session> sessionMap) {
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
                            final Button btn = new Button("Join");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Session session = getTableView().getItems().get(getIndex());
                                        System.out.println(session);
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                    }
                };
        actionsColumn.setCellFactory(actionsCellFactory);

        sessionList.getColumns().add(creatorColumn);
        sessionList.getColumns().add(launchedColumn);
        sessionList.getColumns().add(playersColumn);
        sessionList.getColumns().add(actionsColumn);

        sessionList.getItems().addAll(sessionMap.values());

        Entity sessionTable = entityBuilder()
                .type(TYPE.SESSION)
                .viewWithBBox(sessionList)
                .at(50, 50)
                .buildAndAttach();

        getGameWorld().addEntity(sessionTable);
    }

    private void spawnSession() {
        Scene scene = getGameScene();
        Entity session = entityBuilder()
                .type(TYPE.SESSION)
                .at(100, 100)
                .viewWithBBox("")
                .buildAndAttach();
    }

    @Override
    protected void initGame() {
        //spawnBucket();

        // creates a timer that runs spawnDroplet() every second
        //run(this::spawnDroplet, Duration.seconds(1));

        // loop background music located in /resources/assets/music/
        // loopBGM("bgm.mp3");
    }



    public static void main(String[] args) {
        launch(args);
    }
}
