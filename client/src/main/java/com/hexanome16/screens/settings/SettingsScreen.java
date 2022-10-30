package com.hexanome16.screens.settings;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.ui.UI;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SettingsScreen {
    private static UI uiSingleton;
    private static SettingsScreenUIController uiControllerSingleton;

    /**
     * Singleton factory for UI
     *
     * <p>
     * Creates a static <i>singleton</i> instance of a UI and a MainMenuScreenUIController
     * </p>
     *
     * @return UI
     */
    private static UI getUI() {
        if (uiSingleton == null) {
            uiControllerSingleton = new SettingsScreenUIController();
            uiSingleton = getAssetLoader().loadUI("SettingsScreen.fxml", uiControllerSingleton);
            setupUI();
        }
        return uiSingleton;
    }

    /**
     * Setup uiSingleton with navigation, need to call during uiSingleton creation
     *
     * @pre: uiSingleton is created
     */
    private static void setupUI() {
        uiControllerSingleton.doneButton.setOnAction((event) -> getGameController().exit());
    }

    /**
     * Makes UI appear on screen
     */
    public static void initUI() {
        getGameScene().setBackgroundColor(Color.rgb(0, 0, 5));
        getGameScene().addUI(getUI());
    }

    /**
     * Makes UI disappear from screen
     */
    public static void clearUI() {
        getGameScene().removeUI(getUI());
    }
}
