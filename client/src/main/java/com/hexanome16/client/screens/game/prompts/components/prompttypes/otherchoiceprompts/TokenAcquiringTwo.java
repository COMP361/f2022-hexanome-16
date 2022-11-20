package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.ChoicePrompt;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class responsible for populating Acquiring 2 tokens prompt.
 */
public class TokenAcquiringTwo extends ChoicePrompt {
  double atTokenRadius = width() / 20;
  ArrayList<Node> atMyChoices = new ArrayList<>();
  ArrayList<BonusType> atAvailableBonuses = new ArrayList<>();
  BonusType chosenBonus;

  public TokenAcquiringTwo() {
    getAvailableBonuses();
  }

  private void getAvailableBonuses() {
    // for now this is forcing the list to only have black
    atAvailableBonuses = new ArrayList<>(List.of(BonusType.BLACK));
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
    atMyChoices = new ArrayList<>();
  }

  @Override
  protected boolean canConfirm() {
    return atConfirmCircle.getOpacity() == 1;
  }

  @Override
  protected void handleConfirmation() {
    // this is where you handle what to do with their choice
    PromptComponent.closePrompts();
  }

  @Override
  protected Node promptChoice() {
    // initialize and set up bonuses layout
    HBox bonuses = new HBox();                  //Token Types : Center
    bonuses.setAlignment(Pos.CENTER);
    bonuses.setSpacing(atTokenRadius / 4);
    bonuses.setPrefSize((width() * 6 / 10), (height() * 3 / 4));
    bonuses.setMaxWidth((width() * 6 / 10));

    // add choices to the layout
    for (BonusType t : atAvailableBonuses) {
      Node bonusType = makeBonusType(t);
      bonuses.getChildren().add(bonusType);
    }

    return bonuses;
  }

  private Node makeBonusType(BonusType bonusType) {

    // initialize and set up actual circle
    Circle bonusCircle = new Circle(atTokenRadius, bonusType.getColor());
    bonusCircle.setStrokeWidth(atTokenRadius * 0.2);
    bonusCircle.setStroke(bonusType.getStrokeColor());

    // initialize and set up the glowing around the circle
    // (also, add it to the list of all the glowing circles in the prompt)
    Circle selectionCircle = new Circle(atTokenRadius * 1.4, Color.WHITE);
    selectionCircle.setOpacity(0.5);
    atMyChoices.add(selectionCircle);

    // initialize and set up bonus layout
    StackPane myBonus = new StackPane();
    myBonus.getChildren().addAll(selectionCircle, bonusCircle);

    // add onHover effect if condition is true
    // (condition is the 4th argument, a predicate object which
    // tests the last argument to the function)
    PromptTypeInterface.setOnHoverEffectOpacity(myBonus, selectionCircle,
        0.5, 0.7,
        e -> ((Circle) e).getOpacity() != 1, selectionCircle);

    // clear all other selections prior to the current one and set current selection as
    // desired one.
    myBonus.setOnMouseClicked(e -> {
      for (Node n : atMyChoices) {
        n.setOpacity(0.5);
      }
      selectionCircle.setOpacity(1);
      chosenBonus = bonusType;
      atConfirmCircle.setOpacity(1);
    });
    return myBonus;
  }

}
