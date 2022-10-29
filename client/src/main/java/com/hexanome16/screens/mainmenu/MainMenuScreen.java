package com.hexanome16.screens.mainmenu;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.CSS;
import com.almasb.fxgl.ui.UI;

import static com.almasb.fxgl.dsl.FXGL.*;

public class MainMenuScreen extends GameApplication {
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1280);
        gameSettings.setHeight(1024);
        gameSettings.setTitle("Settings screen");
        gameSettings.setVersion("0.1");
    }

    @Override
    protected void initUI() {
        MainMenuScreenUIController uiController = new MainMenuScreenUIController();
        UI ui = getAssetLoader().loadUI("MenuScreen.fxml", uiController);
//        CSS css = FXGL.getAssetLoader().loadCSS("MenuScreen.css");
//        getGameScene().appendCSS(css);

        //TODO: Add lobby screen navigation
//        uiController.lobbySection.setOnMouseClicked();

//        uiController.settingsSection.setOnMouseClicked(event -> getGameController().gotoMainMenu());

        getGameScene().addUI(ui);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
