package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import com.almasb.fxgl.entity.Entity;
import com.hexanome16.client.Config;
import com.hexanome16.client.MainApp;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This prompt displays an error message.
 */
public class ErrorPrompt extends ChoicePromptAbstract {
  private String errorString;

  @Override
  protected String promptText() {
    return "Error";
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
    Text errorText = new Text(errorString);
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
  protected void promptOpens() {
    errorString = MainApp.errorMessage;
  }

  @Override
  public void populatePrompt(Entity entity) {
    super.populatePrompt(entity);
    atConfirmButton.setOpacity(1);
    atConfirmCircle.setOpacity(1);
  }
}
