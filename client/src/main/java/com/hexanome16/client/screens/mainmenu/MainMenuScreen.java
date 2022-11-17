package com.hexanome16.client.screens.mainmenu;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import com.almasb.fxgl.ui.UI;
import com.hexanome16.client.screens.lobby.LobbyScreen;
import com.hexanome16.client.screens.settings.SettingsScreen;

/**
 * UI Screen Main Menu Screen after logging in.
 */
public class MainMenuScreen {
  private static UI uiSingleton;
  private static MainMenuScreenUiController uiControllerSingleton;
  private static boolean isVisible = false;

  private static UI getUi() {
    if (uiSingleton == null) {
      uiControllerSingleton = new MainMenuScreenUiController();
      uiSingleton = getAssetLoader().loadUI("MenuScreen.fxml", uiControllerSingleton);
      setupUi();
    }
    return uiSingleton;
  }

  private static void setupUi() {
    uiControllerSingleton.settingsSection.setOnMouseClicked(event -> {
      //MainMenuScreen.clearUI();
      SettingsScreen.initUi(true);
    });
    uiControllerSingleton.lobbySection.setOnMouseClicked(event -> {
      MainMenuScreen.clearUi();
      LobbyScreen.initLobby();
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
