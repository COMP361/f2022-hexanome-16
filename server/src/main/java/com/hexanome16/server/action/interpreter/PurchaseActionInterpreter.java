package com.hexanome16.server.action.interpreter;

import com.hexanome16.server.action.PurchaseAction;
import com.hexanome16.server.action.generator.PurchaseActionGenerator;
import com.hexanome16.server.models.Game;

/**
 * Purchase action interpreter class.
 */
public class PurchaseActionInterpreter implements ActionInterpreter {

  public PurchaseActionInterpreter() {
  }

  /**
   * Purchases the card here.
   *
   * @param action purchase action
   * @param game game
   */
  public void interpretAndApplyAction(PurchaseAction action, Game game) {
    // Did not check if action is valid here, assume client sends correct action, if you have time
    // you can add the check

    //!!! to be implemented !!!
    // notify long polling methods that a card has been purchased
    game.removeOnBoardCard(action.getCard());
    game.addOnBoardCard(action.getCard().getLevel());
    // Update current player
    //action.getPlayer().addCard(action.getCard());
    //game.setCurrentPlayer((game.getCurrentPlayerIndex()+1)/game.getParticipants().size);
  }

}
