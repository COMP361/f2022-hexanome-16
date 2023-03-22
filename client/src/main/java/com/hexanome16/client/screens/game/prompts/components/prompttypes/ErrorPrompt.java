package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ErrorPrompt implements PromptTypeInterface {
  /**
   * The width.
   */
  double atWidth = FXGL.getAppWidth() / 2.;
  /**
   * The height.
   */
  double atHeight = FXGL.getAppHeight() / 2.;
  /**
   * The top left x.
   */
  double atTopLeftX = FXGL.getAppWidth() / 1.5 - atWidth / 1.5;
  /**
   * The top left y.
   */
  double atTopLeftY = FXGL.getAppHeight() / 1.5 - atHeight / 1.5;

  @Override
  public double getWidth() {
    return atWidth;
  }

  @Override
  public double getHeight() {
    return atHeight;
  }

  @Override
  public boolean isCancelable() {
    return true;
  }

  @Override
  public boolean canBeOpenedOutOfTurn() {
    return true;
  }

  @Override
  public void populatePrompt(Entity entity) {

    // initiate and set up the buttons' layout
    VBox errorHolder = new VBox();
    errorHolder.setAlignment(Pos.TOP_CENTER);
    errorHolder.setSpacing(atButtonHeight / 2);
    errorHolder.setTranslateX(atTopLeftX);
    errorHolder.setTranslateY(atTopLeftY);
    errorHolder.setPrefSize(atWidth, atHeight);

    // add buttons to the layout
    for (Pause.ButtonType t : Pause.ButtonType.values()) {
      Node newButton = addButton(t);
      buttonsHolder.getChildren().add(newButton);
    }

    // add layout to entity
    entity.getViewComponent().addChild(buttonsHolder);
  }
}
