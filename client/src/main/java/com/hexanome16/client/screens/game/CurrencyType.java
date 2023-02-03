package com.hexanome16.client.screens.game;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * An enum of the possible currency types in a card purchase.
 */
public enum CurrencyType {
  /**
   * Red tokens currency type.
   */
  RED_TOKENS(Color.DARKRED),
  /**
   * Green tokens currency type.
   */
  GREEN_TOKENS(Color.DARKGREEN),
  /**
   * Blue tokens currency type.
   */
  BLUE_TOKENS(Color.DARKBLUE),
  /**
   * White tokens currency type.
   */
  WHITE_TOKENS(Color.WHITE.darker()),
  /**
   * Black tokens currency type.
   */
  BLACK_TOKENS(Color.BLACK),
  /**
   * Gold tokens currency type.
   */
  GOLD_TOKENS(Color.GOLD.darker()),
  /**
   * Bonus gold cards currency type.
   */
  BONUS_GOLD_CARDS(Color.GOLD.darker());
  /**
   * The Color type.
   */

  private final Color color;

  CurrencyType(Color color) {
    this.color = color;
  }

  /**
   * Gets the color of the CurrencyType.
   *
   * @return the color of the implied argument.
   */
  public Paint getColor() {
    return this.color;
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
    if (this == BLACK_TOKENS || this == BLUE_TOKENS) {
      return Color.WHITE;
    }
    return Color.BLACK;
  }
}
