package com.hexanome16.common.models;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.deserializers.OrientPurchaseMapDeserializer;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.OrientPurchaseMap;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link OrientPurchaseMap}.
 */
public class OrientPurchaseMapTest {

  private OrientPurchaseMap underTest;


  /**
   * Tests canBeUsedToBuy for OrientPurchaseMap.
   */
  @Test
  public void testCanBeUsedToBuy() {
    underTest = new OrientPurchaseMap(0, 0, 0, 0, 0,
        0, 0);
    PriceMap pm = new PriceMap(1, 1, 1, 1, 1);
    assertFalse(underTest.canBeUsedToBuy(pm));
    assertFalse(underTest.canBeUsedToBuy(null));
    assertTrue(underTest.canBeUsedToBuy(underTest));
    underTest = new OrientPurchaseMap(2, 2, 2, 2,
        2, 2, 2);
    assertFalse(underTest.canBeUsedToBuy(pm));
  }

  /**
   * Tests that all constructors produce an equivalent object.
   */
  @Test
  public void constructorTests() {
    OrientPurchaseMap target = new OrientPurchaseMap(1, 1, 1, 1, 1, 1, 1);
    Map<Gem, Integer> map = new HashMap<Gem, Integer>();
    for (Gem gem : Gem.values()) {
      map.put(gem, 1);
    }
    underTest = new OrientPurchaseMap(map, 1);
    assertEquals(target, underTest);

    underTest = new OrientPurchaseMap(new PurchaseMap(map), 1);
    assertEquals(target, underTest);

    map.remove(Gem.GOLD);
    underTest = new OrientPurchaseMap(new PriceMap(map), 1, 1);
    assertEquals(target, underTest);
  }

  /**
   * Tests canBeUsedToBuyAlt().
   */
  @Test
  public void testCanBeUsedToBuyAlt() {
    underTest = new OrientPurchaseMap(0, 0, 0, 0, 0,
        1, 0);
    PriceMap pm = new PriceMap(2, 0, 0,
        0, 0);
    assertTrue(underTest.canBeUsedToBuyAlt(pm));
    underTest = new OrientPurchaseMap(0, 0, 0, 0, 0,
        0, 1);
    pm = new PriceMap(2, 0, 0,
        0, 0);
    assertTrue(underTest.canBeUsedToBuyAlt(pm));

    pm = new PriceMap(2, 2, 0,
        0, 0);
    assertTrue(underTest.canBeUsedToBuyAlt(pm));

  }

  /**
   * Tests for subtract().
   */
  @Test
  public void testSubtract() {
    underTest = new OrientPurchaseMap(1, 1, 1, 1, 1,
        1, 1);
    PriceMap pm = new PriceMap(1, 1, 1,
        0, 0);
    PriceInterface target = new OrientPurchaseMap(0, 0, 0, 1,
        1, 1, 1);
    assertEquals(target, underTest.subtract(pm));
  }



}
