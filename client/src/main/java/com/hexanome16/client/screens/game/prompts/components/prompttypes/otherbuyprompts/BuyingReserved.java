package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherbuyprompts;

import com.hexanome16.client.screens.game.prompts.components.prompttypes.BuyCardPrompt;

/**
 * A class responsible for populating Buy reserved card prompt.
 */
public class BuyingReserved extends BuyCardPrompt {

  @Override
  public boolean isCancelable() {
    return true;
  }

  /**
   * Need to override to not have a ReserveButton, set cardIsReserved to true to do so.
   */
  protected void setCardIsReserved() {
    cardIsReserved = true;
  }

}

