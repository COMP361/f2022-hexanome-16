package com.hexanome16.server.models;

/**
 * Bonus for a base game card.
 */
public class GemBonus extends Bonus {
  private Gem gem;
  private GemAmount gemAmount;

  public GemBonus(BonusType bonusType, Gem gem, GemAmount gemAmount) {
    super(bonusType);
    this.gem = gem;
    this.gemAmount = gemAmount;
  }

  public void setGem(Gem gem) {
    this.gem = gem;
  }

  public void setGemAmount(GemAmount gemAmount) {
    this.gemAmount = gemAmount;
  }
}
