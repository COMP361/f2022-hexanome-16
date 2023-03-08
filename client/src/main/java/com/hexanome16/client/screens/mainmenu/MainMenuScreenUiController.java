package com.hexanome16.client.screens.mainmenu;

import com.almasb.fxgl.ui.UIController;
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
  /**
   * The Lobby section.
   */
  @FXML
  VBox lobbySection;
  /**
   * The Rulebook section.
   */
  @FXML
  VBox rulebookSection;
  /**
   * The Settings section.
   */
  @FXML
  VBox settingsSection;
  /**
   * The Logout button.
   */
  @FXML
  VBox logoutButton;

  /**
   * Click lobby event.
   *
   * @param event the event
   */
  @FXML
  void clickLobby(MouseEvent event) {
    System.out.println("click");
  }

  /**
   * Click rulebook event.
   *
   * @param event the event
   */
  @FXML
  void clickRulebook(MouseEvent event) {

  }

  /**
   * Click settings event.
   *
   * @param event the event
   */
  @FXML
  void clickSettings(MouseEvent event) {

  }

  /**
   * Expand lobby event.
   *
   * @param event the event
   */
  @FXML
  void expandLobby(MouseEvent event) {
    resize(lobbySection, EXPAND_SIZE);
  }

  /**
   * Expand rulebook event.
   *
   * @param event the event
   */
  @FXML
  void expandRulebook(MouseEvent event) {
    resize(rulebookSection, EXPAND_SIZE);
  }

  /**
   * Expand settings event.
   *
   * @param event the event
   */
  @FXML
  void expandSettings(MouseEvent event) {
    resize(settingsSection, EXPAND_SIZE);
  }

  /**
   * Shrink lobby event.
   *
   * @param event the event
   */
  @FXML
  void shrinkLobby(MouseEvent event) {
    resize(lobbySection, NORMAL_SIZE);
  }

  /**
   * Shrink rulebook event.
   *
   * @param event the event
   */
  @FXML
  void shrinkRulebook(MouseEvent event) {
    resize(rulebookSection, NORMAL_SIZE);
  }

  /**
   * Shrink settings event.
   *
   * @param event the event
   */
  @FXML
  void shrinkSettings(MouseEvent event) {
    resize(settingsSection, NORMAL_SIZE);
  }

  /**
   * Mouse hover logout button.
   *
   * @param event the event
   */
  @FXML
  void hoverLogoutButton(MouseEvent event) {
    resize(logoutButton, EXPAND_SIZE);
  }

  /**
   * Mouse exit logout button.
   *
   * @param event the event
   */
  @FXML
  void exitLogoutButton(MouseEvent event) {
    resize(logoutButton, NORMAL_SIZE);
  }

  /**
   * Click logout button.
   *
   * @param event the event
   */
  @FXML
  void clickLogoutButton(MouseEvent event) {
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
