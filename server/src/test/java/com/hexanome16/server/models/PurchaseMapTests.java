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

/**
 * Tests for {@link PurchaseMap}.
 */
public class PurchaseMapTests {
  private final PurchaseMap purchaseMap = new PurchaseMap(
      1, 1, 1, 1, 1, 0
  );
  private final PriceMap priceMap = new PriceMap(
      1, 1, 1, 1, 1
  );
  private PurchaseMap underTest;

  @BeforeEach
  void setUp() {
    underTest = new PurchaseMap();
  }

  /**
   * Test amount of tokens.
   */
  @Test
  public void testAmountOfTokens() {
    assertEquals(purchaseMap.getTotalNonJokers(), 5);
  }

  /**
   * Test altConstructor.
   */
  @Test
  public void testAltConstructor() {
    Map<Gem, Integer> myMap = new HashMap<>();
    myMap.put(Gem.RUBY, 1);
    myMap.put(Gem.EMERALD, 1);
    myMap.put(Gem.SAPPHIRE, 1);
    myMap.put(Gem.DIAMOND, 1);
    myMap.put(Gem.ONYX, 1);
    myMap.put(Gem.GOLD, 0);
    PurchaseMap altPurchaseMap = new PurchaseMap(myMap);
    assertEquals(purchaseMap, altPurchaseMap);
  }

  /**
   * Test toPurchaseMap(PriceMap pm).
   */
  @Test
  public void testToPurchaseMap() {
    assertEquals(purchaseMap, PurchaseMap.toPurchaseMap(priceMap));
  }


  /**
   * Test buy.
   */
  @Test
  public void testBuy() {
    assertFalse(purchaseMap.canBeUsedToBuy(null));
    assertTrue(purchaseMap.canBeUsedToBuy(priceMap));
    assertTrue(purchaseMap.canBeUsedToBuy(purchaseMap));
    assertTrue(purchaseMap.canBeUsedToBuy(PurchaseMap.toPurchaseMap(priceMap)));
    PurchaseMap bigMap = PurchaseMap.toPurchaseMap(priceMap);
    bigMap.addGems(Gem.DIAMOND, 1);
    assertFalse(purchaseMap.canBeUsedToBuy(bigMap));
  }

  @Test
  void testNullMapInConstructor() {
    // Arrange
    Executable executable = () -> new PurchaseMap(null);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Price map cannot be null", exception.getMessage());
  }

  @Test
  void testNullGemsInMapInConstructor() {
    // Arrange
    Map<Gem, Integer> map = new HashMap<>();
    map.put(Gem.RUBY, null);
    Executable executable = () -> new PurchaseMap(map);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Price map cannot contain null values", exception.getMessage());
  }

  @Test
  void testAddNegativeGems() {
    // Arrange
    Executable executable = () -> underTest.addGems(Gem.RUBY, -1);

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot add negative amount of gems", exception.getMessage());
  }

  @Test
  void testRemoveGems() {
    // Arrange
    underTest = new PurchaseMap(Map.of(Gem.RUBY, 1, Gem.ONYX, 4));

    // Act
    underTest.removeGems(Gem.RUBY, 1);
    underTest.removeGems(Gem.ONYX, 2);

    // Assert
    assertEquals(0, underTest.getGemCost(Gem.RUBY));
    assertEquals(2, underTest.getGemCost(Gem.ONYX));
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
  void testRemoveTooManyGems() {
    // Arrange
    Executable executable = () -> underTest.removeGems(Gem.RUBY, 4);
    underTest = new PurchaseMap(Map.of(Gem.RUBY, 2));

    // Act

    // Assert
    Throwable exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Cannot remove more gems than are present", exception.getMessage());
  }

  /**
   * Test get types of gems.
   * Should return a key set containing all gems
   */
  @Test
  void testGetTypesOfGems() {
    // Arrange

    // Act
    var types = underTest.getTypesOfGems();

    // Assert
    for (Gem gem : Gem.values()) {
      assertTrue(types.contains(gem));
    }
  }

  @Test
  void testSubtractNothing() {
    // Arrange
    PurchaseMap purchaseMap1 = new PurchaseMap();
    underTest = new PurchaseMap(1, 2, 3, 1, 0, 3);

    // Act
    PriceInterface newMap = underTest.subtract(purchaseMap1);

    // Assert
    assertEquals(1, newMap.getGemCost(Gem.RUBY));
    assertEquals(2, newMap.getGemCost(Gem.EMERALD));
    assertEquals(3, newMap.getGemCost(Gem.SAPPHIRE));
    assertEquals(1, newMap.getGemCost(Gem.DIAMOND));
    assertEquals(0, newMap.getGemCost(Gem.ONYX));
    assertEquals(3, newMap.getGemCost(Gem.GOLD));
  }

  @Test
  void testSubtractSuccess() {
    // Arrange
    PurchaseMap purchaseMap1 = new PurchaseMap(1, 1, 2, 0, 0, 0);
    underTest = new PurchaseMap(1, 2, 3, 1, 0, 3);

    // Act
    PriceInterface newMap = underTest.subtract(purchaseMap1);

    // Assert
    assertEquals(0, newMap.getGemCost(Gem.RUBY));
    assertEquals(1, newMap.getGemCost(Gem.EMERALD));
    assertEquals(1, newMap.getGemCost(Gem.SAPPHIRE));
    assertEquals(1, newMap.getGemCost(Gem.DIAMOND));
    assertEquals(0, newMap.getGemCost(Gem.ONYX));
    assertEquals(3, newMap.getGemCost(Gem.GOLD));
  }

  @Test
  void testSubtractBelowZero() {
    // Arrange
    PurchaseMap purchaseMap1 = new PurchaseMap(3, 1, 4, 0, 0, 0);
    underTest = new PurchaseMap(1, 2, 3, 1, 0, 3);

    // Act
    PriceInterface newMap = underTest.subtract(purchaseMap1);

    // Assert
    assertEquals(0, newMap.getGemCost(Gem.RUBY));
    assertEquals(1, newMap.getGemCost(Gem.EMERALD));
    assertEquals(0, newMap.getGemCost(Gem.SAPPHIRE));
    assertEquals(1, newMap.getGemCost(Gem.DIAMOND));
    assertEquals(0, newMap.getGemCost(Gem.ONYX));
    assertEquals(3, newMap.getGemCost(Gem.GOLD));
  }

  @Test
  void testSubtractIncompatible() {
    // Arrange
    // will contain gold tokens
    PriceMap priceMap1 = new PriceMap();
    underTest = new PurchaseMap(2, 1, 3, 0, 1, 0);

    // Act
    Executable executable = () -> underTest.subtract(priceMap1);

    // Assert
    Throwable throwable = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Maps must contains same set of gems", throwable.getMessage());
  }

}
