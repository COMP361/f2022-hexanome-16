package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import java.util.EnumMap;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Enum class which contains all the token bonus Types that can be taken directly from the bank.
 */
public enum BonusType {
  RED,
  GREEN,
  BLUE,
  WHITE,
  BLACK;

  public static final EnumMap<BonusType, Color> m = new EnumMap<BonusType, Color>(BonusType.class);

  static {
    m.put(RED, Color.RED.darker());
    m.put(GREEN, Color.GREEN.darker());
    m.put(BLUE, Color.BLUE.darker());
    m.put(WHITE, Color.WHITE.darker());
    m.put(BLACK, Color.BLACK);
  }

  public Paint getColor() {
    return m.get(this);
  }

  /**
   * Gets the color of the stroke around a bonus type, can also be used for text.
   *
   * @return a Color that is visible on top of the bonus's original Color
   */
  public Paint getStrokeColor() {
    if (this == BLACK) {
      return Color.GREY;
    }
    return Color.BLACK;
  }
}
