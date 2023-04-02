package com.hexanome16.common.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class LevelCardTest {

  private LevelCard underTest;

  @BeforeEach
  void setUp() {
    underTest = new LevelCard();
  }

  @Test
  void testIsAssociatedWhenAssociatedGemIsTrue() {
    // Arrange
    underTest =
        new LevelCard(Level.ONE, 1, 1, "path", new PriceMap(1, 1, 1, 0, 0), LevelCard.BonusType.BAG,
            new PurchaseMap(1, 1, 1, 1, 1, 1));

    // Act
    underTest.associateBagToGem(Gem.RUBY);

    // Assert
    assertTrue(underTest.isAssociated());
  }

  @Test
  void testAssociateBagToGemAddsGemToBonuses() {
    // Arrange
    underTest =
        new LevelCard(Level.ONE, 1, 1, "path", new PriceMap(1, 1, 1, 0, 0), LevelCard.BonusType.BAG,
            new PurchaseMap(0, 0, 0, 0, 0, 0));

    // Act
    underTest.associateBagToGem(Gem.RUBY);

    // Assert
    assertTrue(underTest.isAssociatedGem());
    assertEquals(1, underTest.gemBonus.getGemCost(Gem.RUBY));
  }

  @Test
  void testCantAssociateGoldToBag() {
    // Arrange
    underTest =
        new LevelCard(Level.ONE, 1, 1, "path", new PriceMap(1, 1, 1, 0, 0), LevelCard.BonusType.BAG,
            new PurchaseMap(0, 0, 0, 0, 0, 0));

    // Act
    Executable executable = () -> underTest.associateBagToGem(Gem.GOLD);

    // Assert
    assertThrows(AssertionError.class, executable);
  }
}
