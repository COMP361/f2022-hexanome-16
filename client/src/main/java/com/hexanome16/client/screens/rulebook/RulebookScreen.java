package com.hexanome16.client.screens.rulebook;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import com.almasb.fxgl.ui.UI;
import com.hexanome16.client.screens.mainmenu.MainMenuScreen;

/**
 * UI Screen for local client settings.
 */
public class RulebookScreen {
  private static UI uiSingleton;
  private static RulebookScreenUiController uiControllerSingleton;
  private static boolean isVisible = false;

  /**
   * Retrieve UI singleton of the rulebook screen.
   *
   * @return a UI singleton
   */
  private static UI getUi() {
    if (uiSingleton == null) {
      uiControllerSingleton = new RulebookScreenUiController();
      uiSingleton = getAssetLoader().loadUI("RulebookScreen.fxml", uiControllerSingleton);
      setupUi();
    }
    return uiSingleton;
  }

  private static void setupUi() {
    uiControllerSingleton.closeButton.setOnAction((event) -> {
      RulebookScreen.clearUi();
      MainMenuScreen.initUi();
    });
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
}
