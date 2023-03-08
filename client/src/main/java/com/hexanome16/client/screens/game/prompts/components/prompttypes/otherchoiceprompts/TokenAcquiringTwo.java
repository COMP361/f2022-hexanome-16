package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import com.hexanome16.client.utils.AuthUtils;
import java.util.ArrayList;

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
    long promptSessionId = GameScreen.getSessionId();
    String auth = AuthUtils.getAuth().getAccessToken();
    PromptsRequests.takeTwo(promptSessionId, auth, chosenBonus);
    PromptComponent.closePrompts();
  }

  @Override
  protected ArrayList<BonusType> getAvailableBonuses() {
    long promptSessionId = GameScreen.getSessionId();
    return PromptsRequests.getAvailableTwoBonuses(promptSessionId);
  }

  @Override
  public boolean isCancelable() {
    return true;
  }
}
