package com.hexanome16.screens.settings;

import com.almasb.fxgl.ui.UI;
import com.hexanome16.screens.mainmenu.MainMenuScreen;

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

    // more sorry
    private static UI getUI(boolean x) {
        if (uiSingleton == null) {
            uiControllerSingleton = new SettingsScreenUIController();
            uiSingleton = getAssetLoader().loadUI("SettingsScreen.fxml", uiControllerSingleton);
            setupUI(x);
        }
        return uiSingleton;
    }

    /**
     * Setup uiSingleton with navigation, need to call during uiSingleton creation
     *
     * @pre: uiSingleton is created
     */
    private static void setupUI() {
        uiControllerSingleton.doneButton.setOnAction((event) -> {
            SettingsScreen.clearUI();
            MainMenuScreen.initUI();
        });
    }

    // final sorry :(
    private static void setupUI( boolean x) {
        uiControllerSingleton.doneButton.setOnAction((event) -> {
            SettingsScreen.clearUI(x);
        });
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

    // please dont be mad tristan xoxo
    public static void initUI(boolean x) {
        if (isVisible) return;

        getGameScene().addUI(getUI(x));
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

    //sorry
    public static void clearUI(boolean x) {
        if (!isVisible) return;

        getGameScene().removeUI(getUI(x));

        isVisible = false;
    }



}
