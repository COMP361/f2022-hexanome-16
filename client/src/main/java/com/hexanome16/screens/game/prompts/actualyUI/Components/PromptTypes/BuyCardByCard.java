package com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.entity.Entity;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;

public class BuyCardByCard implements PromptTypeInterface {

  double aWidth = getAppWidth() / 2;
  double aHeight = getAppHeight() / 2;
  double aCardHeight = aHeight * 0.7;
  double aCardWidth = aCardHeight * 0.72;
  double topleftX = (getAppWidth() / 2) - (aWidth / 2);
  double topleftY = (getAppHeight() / 2) - (aHeight / 2);

  @Override
  public double width() {
    return aWidth;
  }

  @Override
  public double height() {
    return aHeight;
  }

  @Override
  public void populatePrompt(Entity entity) {
    
  }
}
