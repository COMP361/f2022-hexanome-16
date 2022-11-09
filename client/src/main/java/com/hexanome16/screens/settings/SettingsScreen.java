package com.hexanome16.screens.settings;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import com.almasb.fxgl.ui.UI;
import com.hexanome16.screens.mainmenu.MainMenuScreen;

public class SettingsScreen {
  private static UI uiSingleton;
  private static SettingsScreenUIController uiControllerSingleton;
  private static boolean isVisible = false;

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

  private static void setupUI() {
    uiControllerSingleton.doneButton.setOnAction((event) -> {
      SettingsScreen.clearUI();
      MainMenuScreen.initUI();
    });
  }

  // final sorry :(
  private static void setupUI(boolean x) {
    uiControllerSingleton.doneButton.setOnAction((event) -> {
      SettingsScreen.clearUI(x);
    });
  }

  public static void initUI() {
    if (isVisible) {
      return;
    }

    getGameScene().addUI(getUI());
    isVisible = true;
  }

  // please dont be mad tristan xoxo
  public static void initUI(boolean x) {
    if (isVisible) {
      return;
    }

    getGameScene().addUI(getUI(x));
    isVisible = true;
  }

  public static void clearUI() {
    if (!isVisible) {
      return;
    }

    getGameScene().removeUI(getUI());

    isVisible = false;
  }

  //sorry
  public static void clearUI(boolean x) {
    if (!isVisible) {
      return;
    }

    getGameScene().removeUI(getUI(x));

    isVisible = false;
  }


}
