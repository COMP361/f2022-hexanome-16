package com.hexanome16.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.Optional;
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
   * For bag cards.
   */
  protected boolean associatedGem;

  /**
   * Bonus type class.
   */
  public enum BonusType {
    NONE, CASCADING_TWO, CASCADING_ONE_BAG, BAG, TWO_GOLD_TOKENS, SACRIFICE, RESERVE_NOBLE;
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
    this.associatedGem = true;
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
    associatedGem = !isBag();
  }

  /**
   * checks if card is a bag card.
   *
   * @return true if the card is a bag card.
   */
  @JsonIgnore
  public boolean isBag() {
    return bonusType == BonusType.BAG || bonusType == BonusType.CASCADING_ONE_BAG;
  }

  /**
   * true if the bag card was already associated to a card.
   *
   * @return true if card was associated to a type, false otherwise
   */
  @JsonIgnore
  public boolean isAssociated() {
    return associatedGem;
  }

  /**
   * Associated the card to a gem type.
   *
   * @param gem gem we want to associate the card to, not null.
   * @pre card needs to have never been associated before and card needs to be a bag card and
   *      gem isn't GOLD (cant associate to gold gem).
   */
  public void associateBagToGem(Gem gem) {
    assert (!isAssociated() && isBag() && gem != Gem.GOLD && gem != null);
    gemBonus.addGems(gem, 1);
    associatedGem = true;
  }
}
