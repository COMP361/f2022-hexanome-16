package com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import java.util.ArrayList;
import java.util.List;

/**
 * A class responsible for populating See Other reserved Cards prompt.
 */
public class SeeOtherReserved extends SeeReservedAbstract {

  @Override
  protected void appendBehaviour(Texture t) {
    // we don't want to add behaviour for now
    return;
  }

  @Override
  protected String promptText() {
    return "Others reserved cards";
  }

  @Override
  protected void promptOpened() {
    // hardCoded for now
    assert (hiddenCards + viewAbleCards.size()) < 3;
    hiddenCards = 2;
    viewAbleCards = new ArrayList<>(List.of(FXGL.texture("card1.png")));
  }

}
