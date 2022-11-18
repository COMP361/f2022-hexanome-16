package com.hexanome16.client.screens.rulebook;

import com.almasb.fxgl.ui.UIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * UI Controller for rulebook screen.
 *
 * <p>
 * Only for fxml purposes, functionality is mostly in {@link RulebookScreen}
 * </p>
 */
public class RulebookScreenUiController implements UIController {
  @FXML
  Button closeButton; //Package private to expose it to the SettingScreen

  @Override
  public void init() {
  }
}
