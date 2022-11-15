package com.hexanome16.client.screens.mainmenu;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import com.almasb.fxgl.ui.UI;
import com.hexanome16.client.screens.lobby.LobbyScreen;
import com.hexanome16.client.screens.settings.SettingsScreen;

public class MainMenuScreen {
  private static UI uiSingleton;
  private static MainMenuScreenUIController uiControllerSingleton;
  private static boolean isVisible = false;

  private static UI getUI() {
    if (uiSingleton == null) {
      uiControllerSingleton = new MainMenuScreenUIController();
      uiSingleton = getAssetLoader().loadUI("MenuScreen.fxml", uiControllerSingleton);
      setupUI();
    }
    return uiSingleton;
  }

  private static void setupUI() {
    uiControllerSingleton.settingsSection.setOnMouseClicked(event -> {
      //MainMenuScreen.clearUI();
      SettingsScreen.initUI(true);
    });
    uiControllerSingleton.lobbySection.setOnMouseClicked(event -> {
      MainMenuScreen.clearUI();
      LobbyScreen.initLobby();
    });
  }

  public static void initUI() {
    if (isVisible) {
      return;
    }

    getGameScene().addUI(getUI());
    isVisible = true;
  }

  public static void clearUI() {
    if (!isVisible) {
      return;
    }

    getGameScene().removeUI(getUI());

    isVisible = false;
  }
}
