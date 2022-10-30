package com.hexanome16.screens.settings;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.ui.UI;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Settings UI
 *
 * <p>
 *     To use: {@link #initUI()} <br>
 *     To clear: {@link #clearUI()}
 * </p>
 */
public class SettingsScreen {
    private static UI uiSingleton;
    private static SettingsScreenUIController uiControllerSingleton;
    private static boolean isVisible = false;

    /**
     * Singleton factory for UI
     *
     * <p>
     * Creates a static <i>singleton</i> instance of a UI and a controller
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
     * @pre: UI isn't already visible (will not do anything otherwise)
     */
    public static void initUI() {
        if (isVisible) return;

        getGameScene().addUI(getUI());
        isVisible = true;
    }

    /**
     * Makes UI disappear from screen
     * @pre: UI is currently visible (will not do anything otherwise)
     */
    public static void clearUI() {
        if (!isVisible) return;

        getGameScene().removeUI(getUI());

        isVisible = false;
    }
}
