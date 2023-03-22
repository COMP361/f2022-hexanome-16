package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import java.util.ArrayList;

/**
 * Class responsible for populating Reserving Noble prompt.
 */
public class ChooseNobleReserve extends NobleChoiceAbstract {

  @Override
  public boolean isCancelable() {
    return false;
  }

  @Override
  public boolean canBeOpenedOutOfTurn() {
    return true;
  }

  @Override
  protected String promptText() {
    return "Reserve a noble";
  }

  @Override
  protected double promptTextSize() {
    return getHeight() / 6.;
  }

  @Override
  protected boolean canConfirm() {
    return atConfirmCircle.getOpacity() == 1.;
  }

  @Override
  protected void handleConfirmation() {
    // to modify, use chosenNobleIndex to get index of the choice
    PromptComponent.closePrompts();
  }

  @Override
  protected ArrayList<Texture> getChoiceNobles() {
    // Hard Coded for now
    ArrayList<Texture> myList = new ArrayList<>();
    myList.add(FXGL.texture("noble1.png"));
    myList.add(FXGL.texture("noble2.png"));
    myList.add(FXGL.texture("noble2.png"));
    return myList;
  }
}
