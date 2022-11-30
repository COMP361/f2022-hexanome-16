package com.hexanome16.server.models;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class BonusTest {
  public BagBonus bagBonus = new BagBonus(BonusType.CARD_BONUS);

  @Test
  public void testInitBagBonusType() {
    assertEquals(BonusType.CARD_BONUS, bagBonus.getBonusType());
  }

  @Test
  public void testInitBagBonusGem() {
    bagBonus.setChosenGem(Gem.RUBY);
    assertEquals(Optional.of(Gem.RUBY), bagBonus.getChosenGem());
  }
}
