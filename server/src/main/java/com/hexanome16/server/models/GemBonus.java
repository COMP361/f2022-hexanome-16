package com.hexanome16.server.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Bonus for a base game card.
 */
@Getter
@Setter
@ToString
public class GemBonus extends Bonus {
  private Gem gem;
  private GemAmount gemAmount;

  /**
   * Creates a new gem bonus type.
   *
   * @param bonusType another bonus type
   * @param gem       bonus gem type
   * @param gemAmount bonus gem amount
   */
  public GemBonus(BonusType bonusType, Gem gem, GemAmount gemAmount) {
    super(bonusType);
    this.gem = gem;
    this.gemAmount = gemAmount;
  }

}
