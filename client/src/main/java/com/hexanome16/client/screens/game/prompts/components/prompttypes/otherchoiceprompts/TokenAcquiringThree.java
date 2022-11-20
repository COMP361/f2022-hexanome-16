package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.ChoicePrompt;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class responsible for populating Acquiring 2 tokens prompt.
 */
public class TokenAcquiringThree extends ChoicePrompt {

  double atTokenRadius = width() / 20;
  ArrayList<Node> myNodes = new ArrayList<>();
  double SelectedTokenTypes = 0;

  @Override
  protected String promptText() {
    return null;
  }

  @Override
  protected double promptTextSize() {
    return 0;
  }

  @Override
  protected void handlePromptForceQuit() {

  }

  @Override
  protected boolean canConfirm() {
    return false;
  }

  @Override
  protected void handleConfirmation() {

  }

  @Override
  protected Node promptChoice() {
    return null;
  }

  private Node makeBonusType(BonusType pBonusType) {
    StackPane myBonus = new StackPane();
    Circle Bonus = new Circle(atTokenRadius, pBonusType.getColor());
    Bonus.setStrokeWidth(aHeight / 100);
    Bonus.setStroke(pBonusType.getStrokeColor());
    Circle SelectionRectangle = new Circle(atTokenRadius * 1.4, Color.WHITE);
    SelectionRectangle.setOpacity(0.5);
    myNodes.add(SelectionRectangle);


    myBonus.getChildren().addAll(SelectionRectangle, Bonus);

    myBonus.setOnMouseEntered(e -> {
      if (SelectionRectangle.getOpacity() != 1) {
        SelectionRectangle.setOpacity(0.7);
      }
    });

    myBonus.setOnMouseExited(e -> {
      if (SelectionRectangle.getOpacity() != 1) {
        SelectionRectangle.setOpacity(0.5);
      }
    });

    myBonus.setOnMouseClicked(e -> {
      if (SelectionRectangle.getOpacity() == 1) {
        SelectionRectangle.setOpacity(0.5);
        SelectedTokenTypes--;
      } else if (SelectedTokenTypes < 3 &&
          (SelectionRectangle.getOpacity() == 0.7 || SelectionRectangle.getOpacity() == 0.5)) {
        SelectionRectangle.setOpacity(1);
        SelectedTokenTypes++;
      }
      if (SelectedTokenTypes == 3) {
        confirmationButton.setOpacity(1);
      }
      if (SelectedTokenTypes < 3) {
        confirmationButton.setOpacity(0.5);
      }
    });

    pBonuses.getChildren().add(myBonus);
  }
}
