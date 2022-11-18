package com.hexanome16.client.screens.settings;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import com.almasb.fxgl.ui.UI;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;

/**
 * UI Screen for local client settings.
 */
public class SettingsScreen {
  private static UI uiSingleton;
  private static SettingsScreenUiController uiControllerSingleton;
  private static boolean isVisible = false;

  /**
   * Retrieve UI singleton of the settings screen.
   *
   * @return a UI singleton
   */
  private static UI getUi() {
    if (uiSingleton == null) {
      uiControllerSingleton = new SettingsScreenUiController();
      uiSingleton = getAssetLoader().loadUI("SettingsScreen.fxml", uiControllerSingleton);
      setupUi();
    }
    return uiSingleton;
  }

  // more sorry
  private static UI getUi(boolean x) {
    if (uiSingleton == null) {
      uiControllerSingleton = new SettingsScreenUiController();
      uiSingleton = getAssetLoader().loadUI("SettingsScreen.fxml", uiControllerSingleton);
      setupUi(x);
    }
    return uiSingleton;
  }

  private static void setupUi() {
    uiControllerSingleton.doneButton.setOnAction((event) -> {
      SettingsScreen.clearUi();
      MainMenuScreen.initUi();
    });
  }

  // final sorry :(
  private static void setupUi(boolean x) {
    uiControllerSingleton.doneButton.setOnAction((event) -> SettingsScreen.clearUi(x));
  }

  /**
   * Adds UI layer on top of game screen.
   */
  public static void initUi() {
    if (isVisible) {
      return;
    }

    getGameScene().addUI(getUi());
    isVisible = true;
  }

  // please dont be mad tristan xoxo
  /**
   * Adds UI layer on top of game screen.
   */
  public static void initUi(boolean x) {
    if (isVisible) {
      return;
    }

    getGameScene().addUI(getUi(x));
    isVisible = true;
  }

  /**
   * Removes UI layer on top of game screen.
   */
  public static void clearUi() {
    if (!isVisible) {
      return;
    }

    getGameScene().removeUI(getUi());

    isVisible = false;
  }

  //sorry
  /**
   * Removes UI layer on top of game screen.
   */
  public static void clearUi(boolean x) {
    if (!isVisible) {
      return;
    }

    getGameScene().removeUI(getUi(x));

    isVisible = false;
  }


}
