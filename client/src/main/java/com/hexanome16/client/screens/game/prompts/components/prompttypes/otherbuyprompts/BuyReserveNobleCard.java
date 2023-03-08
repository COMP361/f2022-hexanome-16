package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherbuyprompts;

import com.almasb.fxgl.dsl.FXGL;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.events.SplendorEvents;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BuyCardPrompt;

/**
 * A class responsible for populating Buy reserved noble card prompt.
 */
public class BuyReserveNobleCard extends BuyCardPrompt {

  @Override
  public boolean isCancelable() {
    return true;
  }

  /**
   * Need to override to have a different image.
   */
  @Override
  protected void setCardImage() {
    // if we have a card entity we are inspecting
    if (atCardEntity != null) {
      cardImage = FXGL.texture(atCardEntity.getComponent(CardComponent.class).texture);
      return;
    }

    cardImage = FXGL.texture("noblereserve.png");
  }

  /**
   * To Override if there is behaviour after someone presses on Confirm, is done after
   * closeBuyPrompt().
   */
  @Override
  protected void cardBought() {
    if (atCardEntity != null) {
      FXGL.getEventBus().fireEvent(new SplendorEvents(SplendorEvents.BOUGHT, atCardEntity));
    }
    PromptUtils.openPrompt(PromptType.CHOOSE_NOBLE_TO_RESERVE);
  }

}
