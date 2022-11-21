package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import java.util.ArrayList;

/**
 * Class responsible for populating Noble Conflict prompt.
 */
public class ChooseNoble extends NobleChoiceAbstract {

  @Override
  protected String promptText() {
    return "Noble conflict";
  }

  @Override
  protected double promptTextSize() {
    return height() / 6.;
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
    myList.add(FXGL.texture("noble1.png"));
    return myList;
  }
}
