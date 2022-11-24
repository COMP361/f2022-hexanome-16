package com.hexanome16.server.models;

import java.util.Optional;

/**
 * Bonus of a bag card: it let you choose another gem bonus.
 */
public class BagBonus extends Bonus {

  private Optional<Gem> chosenGem;

  public BagBonus(BonusType bonusType) {
    super(bonusType);
  }

  public Optional<Gem> getChosenGem() {
    return chosenGem;
  }

  public void setChosenGem(Gem chosenGem) {
    this.chosenGem = Optional.of(chosenGem);
  }
}
