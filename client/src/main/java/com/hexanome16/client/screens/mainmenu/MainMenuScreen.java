package com.hexanome16.client.screens.mainmenu;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.ui.UI;
import com.hexanome16.client.MainApp;
import com.hexanome16.client.requests.lobbyservice.oauth.LogoutRequest;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.lobby.LobbyScreen;
import com.hexanome16.client.screens.register.RegisterScreen;
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
      LogoutRequest.execute();
      MainMenuScreen.clearUi();
      StartupScreen.backToStartupScreen();
    });
    uiControllerSingleton.registerButton.setOnMouseClicked(event -> {
      if (AuthUtils.getPlayer().getRole().equals("ROLE_ADMIN")) {
        MainMenuScreen.clearUi();
        RegisterScreen.initRegisterScreen();
      } else {
        MainMenuScreen.clearUi();
        MainApp.errorMessage = "Sorry, only admin accounts are able to register new players.";
        FXGL.spawn("PromptBox", new SpawnData().put("promptType",
            PromptTypeInterface.PromptType.ERROR)
            .put("handleConfirm", (Runnable) MainMenuScreen::initUi));
      }
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
