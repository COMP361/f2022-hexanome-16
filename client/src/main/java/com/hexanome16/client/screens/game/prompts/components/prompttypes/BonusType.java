package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Enum class which contains all the token bonus Types that can be taken directly from the bank.
 */
public enum BonusType {
  /**
   * Red bonus type.
   */
  RED(Color.RED.darker()),
  /**
   * Green bonus type.
   */
  GREEN(Color.GREEN.darker()),
  /**
   * Blue bonus type.
   */
  BLUE(Color.BLUE.darker()),
  /**
   * White bonus type.
   */
  WHITE(Color.WHITE.darker()),
  /**
   * Black bonus type.
   */
  BLACK(Color.BLACK);

  private final Color color;

  BonusType(Color color) {
    this.color = color;
  }

  /**
   * Gets color.
   *
   * @return the color
   */
  public Paint getColor() {
    return this.color;
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

  /**
   * Returns the Bonus type that relates to the input string.
   *
   * @param bonusTypeString String representation of the bonus type.
   * @return BonusType associated to that string, null if string is not valid
   */
  public static BonusType fromString(String bonusTypeString) {
    return switch (bonusTypeString) {
      case "RED" -> BonusType.RED;
      case "GREEN" -> BonusType.GREEN;
      case "BLUE" -> BonusType.BLUE;
      case "WHITE" -> BonusType.WHITE;
      case "BLACK" -> BonusType.BLACK;
      default -> null;
    };
  }
}
