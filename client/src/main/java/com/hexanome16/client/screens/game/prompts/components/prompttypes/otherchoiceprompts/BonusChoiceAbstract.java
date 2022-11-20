package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.ChoicePromptAbstract;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Abstract class representing the basics of token Acquiring, used for code reuse.
 */
public abstract class BonusChoiceAbstract extends ChoicePromptAbstract {
  protected BonusType chosenBonus;
  double atTokenRadius = width() / 20;
  ArrayList<BonusType> atAvailableBonuses = new ArrayList<>();
  ArrayList<Node> atMyNodes = new ArrayList<>();

  @Override
  protected void promptOpens() {
    atAvailableBonuses = getAvailableBonuses();
  }


  @Override
  protected void addToLayout(HBox paLayout) {
    paLayout.setSpacing(atTokenRadius / 4.);
    // add choices to the layout
    for (BonusType t : atAvailableBonuses) {
      ArrayList<Node> bonusNodesList = makeBonusType(t);
      Node bonusType = addToBonusType(bonusNodesList, t);
      paLayout.getChildren().add(bonusType);
    }
  }

  private ArrayList<Node> makeBonusType(BonusType bonusType) {

    // initialize and set up actual circle
    Circle bonusCircle = new Circle(atTokenRadius, bonusType.getColor());
    bonusCircle.setStrokeWidth(atTokenRadius * 0.2);
    bonusCircle.setStroke(bonusType.getStrokeColor());

    // initialize and set up the glowing around the circle
    // (also, add it to the list of all the glowing circles in the prompt)
    Circle selectionCircle = new Circle(atTokenRadius * 1.4, Color.WHITE);
    selectionCircle.setOpacity(0.5);
    atMyNodes.add(selectionCircle);

    // initialize and set up bonus layout
    StackPane myBonus = new StackPane();
    myBonus.getChildren().addAll(selectionCircle, bonusCircle);

    // add onHover effect if condition is true
    // (condition is the 4th argument, a predicate object which
    // tests the last argument to the function)
    PromptTypeInterface.setOnHoverEffectOpacity(myBonus, selectionCircle,
        0.5, 0.7,
        e -> ((Circle) e).getOpacity() != 1, selectionCircle);


    return new ArrayList<>(List.of(myBonus, selectionCircle, bonusCircle));
  }

  /**
   * Adds behaviour to Node before returning it, has access to all its children.
   *
   *  @param bonusNode a List containing 3 elements, the first one being the stackPane,
   *                  the second one is the selectionCircle and the third one is
   *                  the actual bonusCircle.
   * @return a Modified version of the first element from the input
   */
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

  @Override
  protected void handlePromptForceQuit() {
    atMyNodes = new ArrayList<>();
    atAvailableBonuses = new ArrayList<>();
    chosenBonus = null;
  }

  /**
   * Getter function, gets all valid bonuses that can be chosen for the prompt.
   *
   * @return an Array of the available bonuses.
   */
  protected abstract ArrayList<BonusType> getAvailableBonuses();

}
