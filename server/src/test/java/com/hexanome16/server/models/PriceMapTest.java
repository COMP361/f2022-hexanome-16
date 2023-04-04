package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class PriceMapTest {
  private PriceMap underTest;

  @BeforeEach
  void setUp() {
    underTest = new PriceMap();
  }

  @Test
  void hasAtLeastAmountOfGems() {
    PriceMap priceMap1 = new PriceMap(2, 2, 2, 2, 2);
    PriceMap priceMap2 = new PriceMap(2, 2, 2, 2, 2);
    assertTrue(priceMap1.hasAtLeastAmountOfGems(priceMap2));

    priceMap1 = new PriceMap(3, 2, 2, 2, 2);
    priceMap2 = new PriceMap(2, 2, 2, 2, 2);
    assertTrue(priceMap1.hasAtLeastAmountOfGems(priceMap2));
  }

  @Test
  void hasAtLeastAmountOfGemsFalse() {
    PriceMap priceMap1 = new PriceMap(1, 2, 2, 2, 2);
    PriceMap priceMap2 = new PriceMap(2, 2, 2, 2, 2);
    assertFalse(priceMap1.hasAtLeastAmountOfGems(priceMap2));
  }

  @Test
  void testNotNullPriceMapInConstructor() {
    // Arrange
    Map<Gem, Integer> priceMap = Map.of(Gem.RUBY, 2, Gem.ONYX, 2);

    // Act
    PriceMap map = new PriceMap(priceMap);

    // Assert
    assertEquals(2, map.getGemCost(Gem.RUBY));
    assertEquals(2, map.getGemCost(Gem.ONYX));
    assertEquals(0, map.getGemCost(Gem.DIAMOND));
  }

  @Test
  void testNullPriceMapInConstructor() {
    // Arrange
    Executable executable = () -> new PriceMap(null);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Price map cannot be null", exception.getMessage());
  }

  @Test
  void testNullGemsInPriceMapInConstructor() {
    // Arrange
    HashMap<Gem, Integer> map = new HashMap<>();
    map.put(Gem.RUBY, null);
    Executable executable = () -> new PriceMap(map);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Price map cannot contain null values", exception.getMessage());
  }

  @Test
  void testGoldInPriceMapInConstructor() {
    // Arrange
    Executable executable = () -> new PriceMap(Map.of(Gem.GOLD, 1));

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Price map cannot contain gold", exception.getMessage());
  }

  @Test
  void testAddGemsNullMap() {
    // Arrange
    Executable executable = () -> underTest.addGems(null);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot add null gems", exception.getMessage());
  }

  @Test
  void testAddGemsNullMapGems() {
    // Arrange
    HashMap<Gem, Integer> map = new HashMap<>();
    map.put(Gem.RUBY, null);
    Executable executable = () -> underTest.addGems(map);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot add null gems", exception.getMessage());
  }

  @Test
  void testAddGems() {
    // Arrange

    // Act
    underTest.addGems(Gem.RUBY, 1);

    // Assert
    assertEquals(1, underTest.getGemCost(Gem.RUBY));
  }

  @Test
  void testAddNegativeAmountOfGems() {
    // Arrange
    Executable executable = () -> underTest.addGems(Gem.RUBY, -1);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot add negative amount of gems", exception.getMessage());
  }

  @Test
  void testAddGold() {
    // Arrange
    Executable executable = () -> underTest.addGems(Gem.GOLD, 1);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot add gold to a price map", exception.getMessage());
  }

  @Test
  void testRemoveGems() {
    // Arrange
    underTest = new PriceMap(Map.of(Gem.RUBY, 1));

    // Act
    underTest.removeGems(Gem.RUBY, 1);

    // Assert
    assertEquals(0, underTest.getGemCost(Gem.RUBY));
  }

  @Test
  void testRemoveNegativeGems() {
    // Arrange
    Executable executable = () -> underTest.removeGems(Gem.RUBY, -1);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot remove negative amount of gems", exception.getMessage());
  }

  @Test
  void testRemoveGoldGems() {
    // Arrange
    Executable executable = () -> underTest.removeGems(Gem.GOLD, 1);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot remove gold from a price map", exception.getMessage());
  }

  @Test
  void testRemoveTooManyGems() {
    // Arrange
    Executable executable = () -> underTest.removeGems(Gem.RUBY, 1);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot remove more gems than are present", exception.getMessage());
  }

  @Test
  void testRemoveGemMap() {
    // Arrange
    underTest = new PriceMap(Map.of(Gem.RUBY, 1, Gem.ONYX, 2));

    // Act
    underTest.removeGems(Map.of(Gem.RUBY, 1, Gem.ONYX, 1));

    // Assert
    assertEquals(0, underTest.getGemCost(Gem.RUBY));
    assertEquals(1, underTest.getGemCost(Gem.ONYX));
  }

  @Test
  void testRemoveNullGemMap() {
    // Arrange
    Executable executable = () -> underTest.removeGems(null);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot remove null gems", exception.getMessage());
  }

  @Test
  void testRemoveNullGems() {
    // Arrange
    Map<Gem, Integer> map = new HashMap<>();
    map.put(Gem.RUBY, null);
    Executable executable = () -> underTest.removeGems(map);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot remove null gems", exception.getMessage());
  }

  /**
   * Test get types of gems.
   * Should return a key set with everything but GOLD (purchasemaps contain gold, not pricemaps)
   */
  @Test
  void testGetTypesOfGems() {
    // Arrange

    // Act
    var types = underTest.getTypesOfGems();

    // Assert
    for (Gem gem : Gem.values()) {
      if (gem != Gem.GOLD) {
        assertTrue(types.contains(gem));
      }
    }
  }

  @Test
  void testSubtractSuccess() {
    // Arrange
    PriceMap map1 = new PriceMap(1, 0, 0, 0, 0);
    underTest = new PriceMap(2, 1, 3, 0, 1);

    // Act
    PriceInterface newMap = underTest.subtract(map1);

    // Assert
    assertEquals(1, newMap.getGemCost(Gem.RUBY));
    assertEquals(1, newMap.getGemCost(Gem.EMERALD));
    assertEquals(3, newMap.getGemCost(Gem.SAPPHIRE));
    assertEquals(0, newMap.getGemCost(Gem.DIAMOND));
    assertEquals(1, newMap.getGemCost(Gem.ONYX));
  }

  @Test
  void testSubtractBelowZero() {
    // Arrange
    PriceMap map1 = new PriceMap(4, 0, 1, 0, 0);
    underTest = new PriceMap(2, 1, 3, 0, 1);

    // Act
    PriceInterface newMap = underTest.subtract(map1);

    // Assert
    assertEquals(0, newMap.getGemCost(Gem.RUBY));
    assertEquals(1, newMap.getGemCost(Gem.EMERALD));
    assertEquals(2, newMap.getGemCost(Gem.SAPPHIRE));
    assertEquals(0, newMap.getGemCost(Gem.DIAMOND));
    assertEquals(1, newMap.getGemCost(Gem.ONYX));
  }

  @Test
  void testSubtractIncompatible() {
    // Arrange
    // will contain gold tokens
    PurchaseMap purchaseMap = new PurchaseMap();
    underTest = new PriceMap(2, 1, 3, 0, 1);

    // Act
    Executable executable = () -> underTest.subtract(purchaseMap);

    // Assert
    Throwable throwable = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Maps must contains same set of gems", throwable.getMessage());
  }
}