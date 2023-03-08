package com.hexanome16.common.models;

import com.hexanome16.common.models.price.PriceMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a card in a deck of a certain level.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelCard {
  protected Level level;
  protected CardInfo cardInfo;
  protected boolean faceDown;
  protected BonusType bonusType;

  /**
   * Bonus type class.
   */
  public enum BonusType {
    NONE, CASCADING_TWO;
  }

  /**
   * Instantiates a new Level card.
   *
   * @param level         the level
   * @param id            the id
   * @param prestigePoint number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   */
  public LevelCard(Level level, int id, int prestigePoint, String texturePath, PriceMap price) {
    this.level = level;
    cardInfo = new CardInfo(id, prestigePoint, texturePath, price);
    this.faceDown = true;
    bonusType = BonusType.NONE;
  }

  /**
   * Instantiates a new Level card.
   *
   * @param level         the level
   * @param id            the id
   * @param prestigePoint number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   * @param bonusType     Bonus type of card
   */
  public LevelCard(Level level, int id, int prestigePoint, String texturePath,
                   PriceMap price, BonusType bonusType) {
    this(level, id, prestigePoint, texturePath, price);
    // TODO: why does this not start face down?
    this.bonusType = bonusType;
  }
}
