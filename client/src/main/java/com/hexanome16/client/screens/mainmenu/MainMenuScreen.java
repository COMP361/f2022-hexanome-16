package com.hexanome16.client.screens.mainmenu;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import com.almasb.fxgl.ui.UI;
import com.hexanome16.client.screens.lobby.LobbyScreen;
import com.hexanome16.client.screens.rulebook.RulebookScreen;
import com.hexanome16.client.screens.settings.SettingsScreen;
import com.hexanome16.client.screens.startup.StartupScreen;
import com.hexanome16.client.utils.AuthUtils;

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
    uiControllerSingleton.rulebookSection.setOnMouseClicked(event -> {
      MainMenuScreen.clearUi();
      RulebookScreen.initUi();
    });
    uiControllerSingleton.logoutButton.setOnMouseClicked(event -> {
      AuthUtils.setPlayer(null);
      AuthUtils.setAuth(null);
      MainMenuScreen.clearUi();
      StartupScreen.backToMainScreen();
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
