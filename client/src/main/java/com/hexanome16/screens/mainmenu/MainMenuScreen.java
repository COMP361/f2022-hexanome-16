package com.hexanome16.screens.mainmenu;

import com.almasb.fxgl.ui.UI;
import com.hexanome16.screens.settings.SettingsScreen;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Main Menu UI
 *
 * <p>
 *     To use: {@link #initUI()} <br>
 *     To clear: {@link #clearUI()}
 * </p>
 */
public class MainMenuScreen {
    private static UI uiSingleton;
    private static MainMenuScreenUIController uiControllerSingleton;

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
            uiControllerSingleton = new MainMenuScreenUIController();
            uiSingleton = getAssetLoader().loadUI("MenuScreen.fxml", uiControllerSingleton);
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
        uiControllerSingleton.settingsSection.setOnMouseClicked(event -> {
            MainMenuScreen.clearUI();
            SettingsScreen.initUI();
        });

        //TODO: Add lobby screen navigation
//        uiController.lobbySection.setOnMouseClicked();
    }

    /**
     * Makes UI appear on screen
     */
    public static void initUI() {
        getGameScene().addUI(getUI());
    }

    /**
     * Makes UI disappear from screen
     */
    public static void clearUI() {
        getGameScene().removeUI(getUI());
    }
}
