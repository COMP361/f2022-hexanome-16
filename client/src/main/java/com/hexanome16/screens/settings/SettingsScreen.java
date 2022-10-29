package com.hexanome16.screens.settings;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.ui.UI;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SettingsScreen extends GameApplication {
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1280);
        gameSettings.setHeight(1024);
        gameSettings.setTitle("Settings screen");
        gameSettings.setVersion("0.1");
    }

    @Override
    protected void initUI() {
        SettingsScreenUIController uiController = new SettingsScreenUIController();
        UI ui = getAssetLoader().loadUI("SettingsScreen.fxml", uiController);
        uiController.doneButton.setOnAction((event) -> getGameController().exit());

        getGameScene().setBackgroundColor(Color.rgb(0,0,5));
        getGameScene().addUI(ui);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
