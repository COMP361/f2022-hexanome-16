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
public class TokenAcquiringThree extends TokenAcquiringAbstract {

  ArrayList<BonusType> atAvailableBonuses = new ArrayList<>();
  ArrayList<BonusType> selectedTokenTypes = new ArrayList<>();
  ArrayList<Node> myNodes = new ArrayList<>();

  // to modify
  @Override
  protected ArrayList<BonusType> getAvailableBonuses() {
    // for now this is forcing the list to have all types
    return new ArrayList<>(List.of(BonusType.values()));
  }

  @Override
  protected String promptText() {
    return "Choose 3 Token Types";
  }

  @Override
  protected double promptTextSize() {
    return height() / 6;
  }

  @Override
  protected void handlePromptForceQuit() {
    // resets the field that should be empty.
    myNodes = new ArrayList<>();
    selectedTokenTypes = new ArrayList<>();
    atAvailableBonuses = new ArrayList<>();
  }

  @Override
  protected boolean canConfirm() {
    return atConfirmCircle.getOpacity() == 1. && selectedTokenTypes.size() == 3;
  }

  @Override
  protected void handleConfirmation() {
    // this is where you handle what to do with their choice, refer to selectedTokenTypes
    PromptComponent.closePrompts();
  }


  @Override
  protected Node addToBonusType(ArrayList<Node> bonusNode, BonusType bonusType) {
    // gets back what we need to do this operation
    StackPane wholeButton = (StackPane) bonusNode.get(0);
    Circle selectionCircle = (Circle) bonusNode.get(1);

    // adds the desired behaviour
    wholeButton.setOnMouseClicked(e -> {
      if (selectionCircle.getOpacity() == 1) {

        selectionCircle.setOpacity(0.5);
        selectedTokenTypes.remove(bonusType);

      } else if (selectedTokenTypes.size() < 3) {

        selectionCircle.setOpacity(1);
        selectedTokenTypes.add(bonusType);

      }
      if (selectedTokenTypes.size() == 3) {
        atConfirmCircle.setOpacity(1);
      }
      if (selectedTokenTypes.size() < 3) {
        atConfirmCircle.setOpacity(0.5);
      }
    });

    // return the node with the added behaviour
    return wholeButton;
  }

}
