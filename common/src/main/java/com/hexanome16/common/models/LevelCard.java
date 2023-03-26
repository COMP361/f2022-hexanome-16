package com.hexanome16.common.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This class represents a card in a deck of a certain level.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LevelCard {
  protected Level level;
  protected CardInfo cardInfo;
  protected boolean faceDown;
  protected BonusType bonusType;
  @JsonProperty("bonus")
  protected PurchaseMap gemBonus;

  /**
   * Bonus type class.
   */
  public enum BonusType {
    NONE, CASCADING_TWO, CASCADING_ONE_BAG;
  }

  /**
   * Instantiates a new Level card.
   *
   * @param level         the level
   * @param id            the id
   * @param prestigePoint number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   * @param gemBonus      gem bonus given when card is bought
   */
  public LevelCard(Level level, int id, int prestigePoint, String texturePath, PriceMap price,
                   PurchaseMap gemBonus) {
    this.level = level;
    cardInfo = new CardInfo(id, prestigePoint, texturePath, price);
    this.faceDown = true;
    bonusType = BonusType.NONE;
    this.gemBonus = gemBonus;
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
   * @param gemBonus      gem bonus given when card is bought
   */
  public LevelCard(Level level, int id, int prestigePoint, String texturePath,
                   PriceMap price, BonusType bonusType, PurchaseMap gemBonus) {
    this(level, id, prestigePoint, texturePath, price, gemBonus);
    // TODO: why does this not start face down?
    this.bonusType = bonusType;
  }
}
