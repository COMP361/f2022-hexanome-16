package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PriceTest {

  public final PriceMap priceMap = new PriceMap(1, 2, 3, 4, 5);

  public final Price price = new TokenPrice(priceMap);

  @Test
  public void testRubyAmount(){
    assertEquals(1, priceMap.getRubyAmount());
  }

  @Test
  public void testEmeraldAmount(){
    assertEquals(2, priceMap.getEmeraldAmount());
  }

  @Test
  public void testSapphireAmount(){
    assertEquals(3, priceMap.getSapphireAmount());
  }

  @Test
  public void testDiamondAmount(){
    assertEquals(4, priceMap.getDiamondAmount());
  }

  @Test
  public void testOnyxAmount(){
    assertEquals(5, priceMap.getOnyxAmount());
  }

  @Test
  public void testTokenPrice(){
    assertEquals(priceMap, ((TokenPrice)price).getPriceMap());
  }
}
