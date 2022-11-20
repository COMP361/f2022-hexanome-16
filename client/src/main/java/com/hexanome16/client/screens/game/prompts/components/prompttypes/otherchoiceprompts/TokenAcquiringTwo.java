package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 * Class responsible for populating Acquiring 2 tokens prompt.
 */
public class TokenAcquiringTwo extends TokenAcquiringAbstract {


  BonusType chosenBonus;



  @Override
  protected ArrayList<BonusType> getAvailableBonuses() {
    return new ArrayList<>(List.of(BonusType.BLACK));
  }

  @Override
  protected String promptText() {
    return "Choose A Token Type";
  }

  @Override
  protected double promptTextSize() {
    return height() / 6;
  }

  @Override
  protected void handlePromptForceQuit() {
    atMyNodes = new ArrayList<>();
    atAvailableBonuses = new ArrayList<>();
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
  protected Node addToBonusType(ArrayList<Node> bonusNode, BonusType bonusType) {

    // gets back what we need to do this operation
    StackPane wholeButton = (StackPane) bonusNode.get(0);
    Circle selectionCircle = (Circle) bonusNode.get(1);

    // adds the desired behaviour
    wholeButton.setOnMouseClicked(e -> {
      for (Node n : atMyNodes) {
        n.setOpacity(0.5);
      }
      selectionCircle.setOpacity(1);
      chosenBonus = bonusType;
      atConfirmCircle.setOpacity(1);
    });

    // return the node with the added behaviour
    return wholeButton;
  }


}
