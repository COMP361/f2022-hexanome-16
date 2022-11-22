package com.hexanome16.server.models;

public class Bonus {
  private final BonusType bonusType;

  public Bonus(BonusType bonusType) {
    this.bonusType = bonusType;
  }

  public BonusType getBonusType() {
    return bonusType;
  }
}
