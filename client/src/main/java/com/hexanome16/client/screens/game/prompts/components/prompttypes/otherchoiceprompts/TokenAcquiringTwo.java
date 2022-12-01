package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for populating Acquiring 2 tokens prompt.
 */
public class TokenAcquiringTwo extends BonusChoiceAbstract {

  @Override
  protected String promptText() {
    return "Choose A Token Type";
  }

  @Override
  protected double promptTextSize() {
    return getHeight() / 6;
  }

  @Override
  protected void handlePromptForceQuit() {
    myNodesAttribute = new ArrayList<>();
    availableBonusesAttribute = new ArrayList<>();
    chosenBonus = null;
  }

  @Override
  protected boolean canConfirm() {
    return atConfirmCircle.getOpacity() == 1;
  }

  @Override
  protected void handleConfirmation() {
    // this is where you handle what to do with their choice, refer to chosenBonus
    PromptComponent.closePrompts();
  }

  @Override
  protected ArrayList<BonusType> getAvailableBonuses() {
    return new ArrayList<>(List.of(BonusType.BLACK));
  }

}
