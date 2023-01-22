package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link Player}
 */
public class PlayerTest {
  private Player costa;

  /**
   * Test initializer for all the tests.
   */
  @BeforeEach
  public void init() {
    costa = new Player("costa", "#000000");
  }

  /**
   * Testing incPlayerBank(int, int, int, int, int, int).
   */
  @Test
  public void testIncPlayerBank() {
    PlayerBank playerBank = new PlayerBank();
    assertEquals(costa.getBank(), playerBank);
    costa.incPlayerBank(-3, -2,
        0, 0, 0, 0);
    playerBank.incBank(-3, -2,
        0, 0, 0, 0);
    assertEquals(costa.getBank(), playerBank);
  }

  /**
   * Testing incPlayerBank(PurchaseMap purchaseMap).
   */
  @Test
  public void testIncPlayerBankAlt() {
    PlayerBank playerBank = new PlayerBank();
    assertEquals(costa.getBank(), playerBank);
    costa.incPlayerBank(new PurchaseMap(-3, -2,
        0, 0, 0, 0));
    playerBank.incBank(-3, -2,
        0, 0, 0, 0);
    assertEquals(costa.getBank(), playerBank);
  }

  /**
   * Testing hasAtLeast().
   */
  @Test
  public void testHasAtLeast() {
    assertTrue(costa.hasAtLeast(0, 0, 0, 0, 0, 0));
    costa.incPlayerBank(2, 2, 0, 0, 0, 0);
    assertTrue(costa.hasAtLeast(2, 2, 0, 0, 0, 0));
  }

}
