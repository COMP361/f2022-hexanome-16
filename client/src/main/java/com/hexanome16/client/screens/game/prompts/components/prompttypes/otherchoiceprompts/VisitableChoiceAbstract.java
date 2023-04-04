package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.ChoicePromptAbstract;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Abstract class representing the basics of Noble choice.
 */
public abstract class VisitableChoiceAbstract extends ChoicePromptAbstract {
  /**
   * The Nobles selection box.
   */
  protected ArrayList<Node> visitableSelectionBox = new ArrayList<>();
  /**
   * The Nobles to select.
   */
  protected ArrayList<Texture> visitableToSelect = new ArrayList<>();
  /**
   * The Chosen noble index.
   */
  protected int chosenVisitableIndex;
  /**
   * The Noble width.
   */
  protected double visitableWidth;
  /**
   * The Noble spacing.
   */
  protected double visitableSpacing;

  @Override
  protected void promptOpens() {
    visitableToSelect = getChoiceVisitables();
  }


  @Override
  protected void handlePromptForceQuit() {
    visitableSelectionBox = new ArrayList<>();
    visitableToSelect = new ArrayList<>();
    chosenVisitableIndex = -1;
  }


  @Override
  protected void addToLayout(HBox choicesLayout) {
    visitableWidth = getWidth() / (visitableToSelect.size() * 4);
    visitableSpacing = ((6 * getWidth() / 10.)
        - (visitableWidth * visitableToSelect.size()))
        / (visitableToSelect.size() + 1);
    choicesLayout.setSpacing(visitableSpacing);
    // add choices to the layout
    for (Texture t : visitableToSelect) {
      Node nobleNodesList = makeNoble(t);
      choicesLayout.getChildren().add(nobleNodesList);
    }
  }

  private Node makeNoble(Texture texture) {
    // fix texture size
    texture.setFitWidth(visitableWidth);
    texture.setFitHeight(visitableWidth);

    // create selection rectangle and add it to array
    Rectangle selectionRectangle =
        new Rectangle(visitableWidth * 1.1, visitableWidth * 1.1, Color.WHITE);
    selectionRectangle.setOpacity(0.5);
    visitableSelectionBox.add(selectionRectangle);

    // set up layout of noble and add behaviour
    StackPane myNoble = new StackPane();
    myNoble.getChildren().addAll(selectionRectangle, texture);
    PromptTypeInterface.setOnHoverEffectOpacity(myNoble, selectionRectangle, 0.5, 0.7,
        e -> ((Rectangle) e).getOpacity() != 1, selectionRectangle);

    myNoble.setOnMouseClicked(e -> {
      for (Node n : visitableSelectionBox) {
        n.setOpacity(0.5);
      }
      selectionRectangle.setOpacity(1);
      chosenVisitableIndex = visitableSelectionBox.indexOf(selectionRectangle);
      atConfirmCircle.setOpacity(1);
    });
    return myNoble;
  }


  /**
   * Gets all the Texture of nobles that can be selected.
   *
   * @return a List of all textures.
   */
  protected abstract ArrayList<Texture> getChoiceVisitables();

}
