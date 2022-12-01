package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;


import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for populating Associate Bag prompt.
 */
public class AssociateBagCard extends BonusChoiceAbstract {

  @Override
  protected String promptText() {
    return "Choose bag content";
  }

  @Override
  protected double promptTextSize() {
    return getHeight() / 6.;
  }


  @Override
  protected boolean canConfirm() {
    return atConfirmCircle.getOpacity() == 1;
  }

  @Override
  protected void handleConfirmation() {
    // to modify, relates to chosenBonus
    PromptComponent.closePrompts();
  }


  @Override
  protected ArrayList<BonusType> getAvailableBonuses() {
    // HardCoded for now
    return new ArrayList<>(List.of(BonusType.BLACK, BonusType.WHITE, BonusType.RED));
  }
}
