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
  private static UI getUi(boolean mainMenu) {
    if (uiSingleton == null) {
      uiControllerSingleton = new SettingsScreenUiController();
      uiSingleton = getAssetLoader().loadUI("SettingsScreen.fxml", uiControllerSingleton);
      setupUi(mainMenu);
    }
    return uiSingleton;
  }

//  // more sorry
//  private static UI getUi(boolean x) {
//    if (uiSingleton == null) {
//      uiControllerSingleton = new SettingsScreenUiController();
//      uiSingleton = getAssetLoader().loadUI("SettingsScreen.fxml", uiControllerSingleton);
//      setupUi(x);
//    }
//    return uiSingleton;
//  }

  private static void setupUi(boolean mainMenu) {
    uiControllerSingleton.doneButton.setOnAction((event) -> {
      SettingsScreen.clearUi(mainMenu);
      if (mainMenu) {
        MainMenuScreen.initUi();
      }
    });
  }

//  /**
//   * Adds UI layer on top of game screen.
//   */
//  public static void initUi() {
//    if (isVisible) {
//      return;
//    }
//
//    getGameScene().addUI(getUi());
//    isVisible = true;
//  }

  /**
   * Adds UI layer on top of game screen.
   *
   * @param mainMenu true if I need to re-render mainMenu after closing
   */
  public static void initUi(boolean mainMenu) {
    if (isVisible) {
      return;
    }

    getGameScene().addUI(getUi(mainMenu));
    isVisible = true;
  }

//  /**
//   * Removes UI layer on top of game screen.
//   */
//  public static void clearUi() {
//    if (!isVisible) {
//      return;
//    }
//
//    getGameScene().removeUI(getUi());
//
//    isVisible = false;
//  }

  //sorry

  /**
   * Removes UI layer on top of game screen.
   *
   * @param mainMenu true if I need to re-render mainMenu after closing
   */
  public static void clearUi(boolean mainMenu) {
    if (!isVisible) {
      return;
    }

    getGameScene().removeUI(getUi(mainMenu));

    isVisible = false;
  }


}
