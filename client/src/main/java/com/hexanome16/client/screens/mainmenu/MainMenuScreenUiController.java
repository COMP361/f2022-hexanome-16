package com.hexanome16.client.screens.mainmenu;

import com.almasb.fxgl.ui.UIController;
import com.hexanome16.client.utils.AuthUtils;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * UI Controller for Main Menu Screen.
 *
 * <p>
 * Only for fxml purposes, functionality is mostly in {@link MainMenuScreen}
 * </p>
 */
public class MainMenuScreenUiController implements UIController {

  private static final float EXPAND_SIZE = 1.1f;
  private static final float NORMAL_SIZE = 1f;
  @FXML
  VBox lobbySection;
  @FXML
  VBox rulebookSection;
  @FXML
  VBox settingsSection;

  @FXML
  void clickLobby(MouseEvent event) {
    System.out.println("click");
  }

  @FXML
  void clickRulebook(MouseEvent event) {

  }

  @FXML
  void clickSettings(MouseEvent event) {

  }

  @FXML
  void expandLobby(MouseEvent event) {
    resize(lobbySection, EXPAND_SIZE);
  }

  @FXML
  void expandRulebook(MouseEvent event) {
    resize(rulebookSection, EXPAND_SIZE);
  }

  @FXML
  void expandSettings(MouseEvent event) {
    resize(settingsSection, EXPAND_SIZE);
  }

  @FXML
  void shrinkLobby(MouseEvent event) {
    resize(lobbySection, NORMAL_SIZE);
  }

  @FXML
  void shrinkRulebook(MouseEvent event) {
    resize(rulebookSection, NORMAL_SIZE);
  }

  @FXML
  void shrinkSettings(MouseEvent event) {
    resize(settingsSection, NORMAL_SIZE);
  }

  @FXML
  void logout(MouseEvent event) {
    AuthUtils.setPlayer(null);
    AuthUtils.setAuth(null);
    MainMenuScreen.clearUi();
  }

  private void resize(Node n, float size) {
    ScaleTransition scale =
        new ScaleTransition(Duration.millis(size == EXPAND_SIZE ? 300 : 100), n);
    scale.setToX(size);
    scale.setToY(size);
    if (size == EXPAND_SIZE) {
      n.setViewOrder(-1);
    } else {
      scale.setOnFinished(event -> n.setViewOrder(0));
    }
    scale.play();
  }

  @Override
  public void init() {

  }
}
