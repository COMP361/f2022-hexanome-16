package com.hexanome16.server.models;

import com.hexanome16.server.models.user.User;
import java.util.Optional;
import lombok.Getter;

/**
 * Bonus of a bag card: it let you choose another gem bonus.
 */
@Getter
public class BagBonus extends Bonus {

  private Optional<Gem> chosenGem;

  public BagBonus(BonusType bonusType) {
    super(bonusType);
  }

  public void setChosenGem(Gem chosenGem) {
    this.chosenGem = Optional.of(chosenGem);
  }
}
