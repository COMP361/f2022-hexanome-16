package com.hexanome16.client.screens.game;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * An enum of the possible currency types in a card purchase.
 */
public enum CurrencyType {
  RED_TOKENS,
  GREEN_TOKENS,
  BLUE_TOKENS,
  WHITE_TOKENS,
  BLACK_TOKENS,
  GOLD_TOKENS,
  BONUS_GOLD_CARDS;
  static final Map<CurrencyType, Color> colorType = new HashMap<>();

  static {
    colorType.put(RED_TOKENS, Color.DARKRED);
    colorType.put(GREEN_TOKENS, Color.DARKGREEN);
    colorType.put(BLUE_TOKENS, Color.DARKBLUE);
    colorType.put(WHITE_TOKENS, Color.WHITE.darker());
    colorType.put(BLACK_TOKENS, Color.BLACK);
    colorType.put(GOLD_TOKENS, Color.GOLD.darker());
    colorType.put(BONUS_GOLD_CARDS, Color.GOLD.darker());
  }

  /**
   * Gets the color of the CurrencyType.
   *
   * @return the color of the implied argument.
   */
  public Paint getColor() {
    return colorType.get(this);
  }

  /**
   * Gets a color that pops over the color of the CurrencyType, for the stroke.
   *
   * @return A color that would be suitable for the stroke over a CurrencyType.s
   */
  public Paint getStrokeColor() {
    return this.getTextColor();
  }

  /**
   * Gets a color that pops over the color of the CurrencyType, for the Text.
   *
   * @return A color that would be suitable for the Text over a currencyType.
   */
  public Paint getTextColor() {
    if (this == BLACK_TOKENS) {
      return Color.WHITE;
    }
    return Color.BLACK;
  }
}
