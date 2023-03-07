package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.hexanome16.client.screens.game.prompts.components.prompttypes.ChoicePromptAbstract;
import javafx.scene.layout.HBox;

/**
 * Prompt for Choosing level two card.
 */
public class ChooseLevelTwo extends ChoicePromptAbstract {

  @Override
  public boolean isCancelable() {
    return false;
  }

  @Override
  protected boolean setCancelable() {
    return false;
  }

  @Override
  protected String promptText() {
    return "Choose Level Two";
  }

  @Override
  protected double promptTextSize() {
    return getHeight() / 6.;
  }

  @Override
  protected void handlePromptForceQuit() {

  }

  @Override
  protected boolean canConfirm() {
    return atConfirmCircle.getOpacity() == 1.;
  }

  @Override
  protected void handleConfirmation() {

  }

  @Override
  protected void addToLayout(HBox choicesLayout) {

  }
}
