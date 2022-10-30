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
