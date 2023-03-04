package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Enum class which contains all the token bonus Types that can be taken directly from the bank.
 * SERVER DEPENDS ON THESE NAMES TO STAY THE SAME.
 */
public enum BonusType {
  /**
   * Red bonus type.
   */
  RED(Color.RED.darker(), FXGL.texture("ruby.png")),
  /**
   * Green bonus type.
   */
  GREEN(Color.GREEN.darker(), FXGL.texture("emerald.png")),
  /**
   * Blue bonus type.
   */
  BLUE(Color.BLUE.darker(), FXGL.texture("sapphire.png")),
  /**
   * White bonus type.
   */
  WHITE(Color.WHITE.darker(), FXGL.texture("diamond.png")),
  /**
   * Black bonus type.
   */
  BLACK(Color.BLACK, FXGL.texture("onyx.png"));

  private final Color color;
  private final Texture texture;

  BonusType(Color color, Texture t) {
    this.color = color;
    this.texture = t;
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
   * Gets texture.
   *
   * @return the  texture.
   */
  public Texture getTexture() {
    return this.texture;
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
