package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import com.almasb.fxgl.entity.Entity;
import com.hexanome16.client.Config;
import com.hexanome16.client.MainApp;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.ChoicePromptAbstract;
import com.hexanome16.common.dto.WinJson;
import java.util.Arrays;
import java.util.StringJoiner;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This prompt displays an error message.
 */
public class WinnerPrompt extends ChoicePromptAbstract {
  public static String[] winners;

  @Override
  protected String promptText() {
    return "Game has ended!";
  }

  @Override
  protected double promptTextSize() {
    return getHeight() / 5.;
  }

  @Override
  protected void handlePromptForceQuit() {

  }

  @Override
  protected boolean canConfirm() {
    return true;
  }

  @Override
  protected void handleConfirmation() {
    PromptComponent.closePrompts();
  }

  @Override
  protected void addToLayout(HBox choicesLayout) {
    StringJoiner joiner = new StringJoiner(", ");
    for (String winner : winners) {
      joiner.add(winner);
    }
    Text errorText = new Text("The winner(s) is/are: " + joiner.toString());
    errorText.setTextAlignment(TextAlignment.CENTER);
    errorText.setWrappingWidth(getWidth() * 0.8);
    errorText.setFont(GAME_FONT.newFont(getHeight() / 20.));
    errorText.setFill(Config.SECONDARY_COLOR);
    choicesLayout.getChildren().add(errorText);
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
    super.populatePrompt(entity);
    atConfirmButton.setOpacity(1);
    atConfirmCircle.setOpacity(1);
    if (entity.getProperties().getValueOptional("handleConfirm").isPresent()
        && entity.getObject("handleConfirm") instanceof Runnable) {
      atConfirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        if (canConfirm()) {
          handleConfirmation();
          ((Runnable) entity.getObject("handleConfirm")).run();
        }
      });
    }
  }
}
