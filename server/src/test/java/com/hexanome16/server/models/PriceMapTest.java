package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.common.models.price.PriceMap;
import org.junit.jupiter.api.Test;

class PriceMapTest {

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
}