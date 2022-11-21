package com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.screens.game.prompts.OpenPrompt;
import java.util.ArrayList;
import java.util.List;

/**
 * A class responsible for populating See Own reserved Cards prompt.
 */
public class SeeOwnReserved extends SeeReservedAbstract {
  @Override
  protected void promptOpened() {
    assert (hiddenCards + viewAbleCards.size()) < 3;
    hiddenCards = 0;
    viewAbleCards = new ArrayList<>(
        List.of(FXGL.texture("card1.png"), FXGL.texture("card1.png")));
  }

  @Override
  protected String promptText() {
    return "Own reserved cards";
  }

  @Override
  protected void appendBehaviour(Texture t) {
    t.setOnMouseClicked(e -> {
      OpenPrompt.openPrompt(PromptType.BUYING_RESERVED);
    });
  }

}
