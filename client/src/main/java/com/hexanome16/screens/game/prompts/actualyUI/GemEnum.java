package com.hexanome16.screens.game.prompts.actualyUI;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum GemEnum {
  RUBY,DIAMONDS, EMERALD,OBSIDIAN;

  public Paint getColor() {
    if (this==GemEnum.DIAMONDS){return Color.WHITE.darker();}
    if (this==GemEnum.OBSIDIAN){return Color.DARKGRAY.darker();}
    if (this==GemEnum.RUBY){return Color.RED.darker();}
    return Color.GREEN.darker();
  }
}
