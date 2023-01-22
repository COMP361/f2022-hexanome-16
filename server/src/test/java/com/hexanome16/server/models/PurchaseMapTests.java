package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

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

  /**
   * Test amount of tokens.
   */
  @Test
  public void testAmountOfTokens() {
    assertEquals(purchaseMap.sumTokensNonJokers(), 5);
  }

  /**
   * Test altConstructor
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
    assertFalse(purchaseMap.canBeUsedToBuy(priceMap));
    assertTrue(purchaseMap.canBeUsedToBuy(purchaseMap));
    assertTrue(purchaseMap.canBeUsedToBuy(PurchaseMap.toPurchaseMap(priceMap)));
    PurchaseMap bigMap = PurchaseMap.toPurchaseMap(priceMap);
    bigMap.diamondAmount += 1;
    assertFalse(purchaseMap.canBeUsedToBuy(bigMap));
  }
}
