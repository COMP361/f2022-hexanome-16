package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test for {@link Gem}
 */
public class GemTest {

  /**
   * Testing getBonusType(). (These Strings are the enum
   * elements of the BonusType class in the client side.)
   */
  @Test
  public void testBonusType() {
    assertEquals(Gem.RUBY.getBonusType(), "RED");
    assertEquals(Gem.EMERALD.getBonusType(), "GREEN");
    assertEquals(Gem.SAPPHIRE.getBonusType(), "BLUE");
    assertEquals(Gem.DIAMOND.getBonusType(), "WHITE");
    assertEquals(Gem.ONYX.getBonusType(), "BLACK");
    assertEquals(Gem.GOLD.getBonusType(), "NULL");
  }

  /**
   * Testing getGem().
   */
  @Test
  public void testGetGem() {
    assertEquals(Gem.getGem("RED"), Gem.RUBY);
    assertEquals(Gem.getGem("GREEN"), Gem.EMERALD);
    assertEquals(Gem.getGem("BLUE"), Gem.SAPPHIRE);
    assertEquals(Gem.getGem("WHITE"), Gem.DIAMOND);
    assertEquals(Gem.getGem("BLACK"), Gem.ONYX);
    assertEquals(Gem.getGem("WOW"), null);
  }

  /**
   * Testing areDistinct(Gem gem1, Gem gem2, Gem gem3).
   */
  @Test
  public void testAreDistinct() {
    assertTrue(Gem.areDistinct(Gem.RUBY, Gem.GOLD, Gem.DIAMOND));
    assertFalse(Gem.areDistinct(Gem.RUBY, Gem.RUBY, Gem.DIAMOND));
  }

}
