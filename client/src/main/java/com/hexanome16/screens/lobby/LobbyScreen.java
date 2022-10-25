package com.hexanome16.screens.lobby;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.scene.Scene;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
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
