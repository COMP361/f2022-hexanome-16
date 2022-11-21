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
public abstract class NobleChoiceAbstract extends ChoicePromptAbstract {
  protected ArrayList<Node> noblesSelectionBox = new ArrayList<>();
  protected ArrayList<Texture> noblesToSelect = new ArrayList<>();
  protected int chosenNobleIndex;
  protected double nobleWidth;
  protected double nobleSpacing;

  @Override
  protected void promptOpens() {
    noblesToSelect = getChoiceNobles();
  }



  @Override
  protected void handlePromptForceQuit() {
    noblesSelectionBox = new ArrayList<>();
    noblesToSelect = new ArrayList<>();
    chosenNobleIndex = -1;
  }



  @Override
  protected void addToLayout(HBox choicesLayout) {
    nobleWidth = width() / (noblesToSelect.size() * 2);
    nobleSpacing =
        ((6 * width() / 10.) - (nobleWidth * noblesToSelect.size())) / (noblesToSelect.size() + 1);
    choicesLayout.setSpacing(nobleSpacing);
    // add choices to the layout
    for (Texture t : noblesToSelect) {
      Node nobleNodesList = makeNoble(t);
      choicesLayout.getChildren().add(nobleNodesList);
    }
  }

  private Node makeNoble(Texture texture) {
    // fix texture size
    texture.setFitWidth(nobleWidth);
    texture.setFitHeight(nobleWidth);

    // create selection rectangle and add it to array
    Rectangle selectionRectangle = new Rectangle(nobleWidth * 1.1, nobleWidth * 1.1, Color.WHITE);
    selectionRectangle.setOpacity(0.5);
    noblesSelectionBox.add(selectionRectangle);

    // set up layout of noble and add behaviour
    StackPane myNoble = new StackPane();
    myNoble.getChildren().addAll(selectionRectangle, texture);
    PromptTypeInterface.setOnHoverEffectOpacity(myNoble, selectionRectangle,
        0.5, 0.7,
        e -> ((Rectangle) e).getOpacity() != 1, selectionRectangle);

    myNoble.setOnMouseClicked(e -> {
      for (Node n : noblesSelectionBox) {
        n.setOpacity(0.5);
      }
      selectionRectangle.setOpacity(1);
      chosenNobleIndex = noblesSelectionBox.indexOf(selectionRectangle);
      atConfirmCircle.setOpacity(1);
    });
    return myNoble;
  }


  /**
   * Gets all the Texture of nobles that can be selected.
   *
   * @return a List of all textures.
   */
  protected abstract ArrayList<Texture> getChoiceNobles();

}
