package com.hexanome16.client.screens.game;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.common.models.price.Gem;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.Getter;

/**
 * An enum of the possible currency types in a card purchase.
 */
public enum CurrencyType {
  /**
   * Red tokens currency type.
   */
  RED_TOKENS(Color.DARKRED, Gem.RUBY, FXGL.texture("ruby.png")),
  /**
   * Green tokens currency type.
   */
  GREEN_TOKENS(Color.DARKGREEN, Gem.EMERALD, FXGL.texture("emerald.png")),
  /**
   * Blue tokens currency type.
   */
  BLUE_TOKENS(Color.DARKBLUE, Gem.SAPPHIRE, FXGL.texture("sapphire.png")),
  /**
   * White tokens currency type.
   */
  WHITE_TOKENS(Color.WHITE.darker(), Gem.DIAMOND, FXGL.texture("diamond.png")),
  /**
   * Black tokens currency type.
   */
  BLACK_TOKENS(Color.BLACK, Gem.ONYX, FXGL.texture("onyx.png")),
  /**
   * Gold tokens currency type.
   */
  GOLD_TOKENS(Color.GOLD.darker(), Gem.GOLD, FXGL.texture("gold.png")),
  /**
   * Bonus gold cards currency type.
   */
  BONUS_GOLD_CARDS(Color.GOLD.darker(), Gem.GOLD, FXGL.texture("gold.png"));
  /**
   * The Color type.
   */

  @Getter
  private final Color color;
  @Getter
  private final Gem gem;
  @Getter
  private final Texture texture;

  CurrencyType(Color color, Gem gem, Texture texture) {
    this.color = color;
    this.gem = gem;
    this.texture = texture;
  }

  /**
   * Returns the CurrencyType based on Gem.
   *
   * @param gem The gem to get the CurrencyType from.
   * @return The CurrencyType that corresponds to the Gem.
   */
  public static CurrencyType fromGem(Gem gem) {
    return switch (gem) {
      case RUBY -> RED_TOKENS;
      case EMERALD -> GREEN_TOKENS;
      case SAPPHIRE -> BLUE_TOKENS;
      case DIAMOND -> WHITE_TOKENS;
      case ONYX -> BLACK_TOKENS;
      case GOLD -> GOLD_TOKENS;
    };
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
