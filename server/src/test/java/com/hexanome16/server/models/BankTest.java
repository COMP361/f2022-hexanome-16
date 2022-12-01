package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The type Bank test.
 */
public class BankTest {
  /**
   * The Game bank.
   */
  public GameBank gameBank = new GameBank();
  /**
   * The Player bank.
   */
  public PlayerBank playerBank = new PlayerBank();

  /**
   * Test game bank default tokens.
   */
  @Test
  public void testGameBankDefaultTokens() {
    assertTrue(gameBank.hasAtLeast(7, 7, 7,
        7, 7, 5));
  }

  /**
   * Test player bank default tokens.
   */
  @Test
  public void testPlayerBankDefaultTokens() {
    assertTrue(playerBank.hasAtLeast(3, 3, 3,
        3, 3, 3));
  }

  /**
   * Test decrease player tokens.
   */
  @Test
  public void testDecreasePlayerTokens() {
    playerBank.incBank(-1, -1, -1,
        -1, -1, -1);
    assertFalse(playerBank.hasAtLeast(3, 3, 3,
        3, 3, 3));
  }

  /**
   * Test increase player tokens.
   */
  @Test
  public void testIncreasePlayerTokens() {
    playerBank.incBank(2, 2, 2,
        2, 2, 2);
    assertTrue(playerBank.hasAtLeast(4, 4, 4,
        4, 4, 4));
  }

  /**
   * Test decrease player tokens to neg.
   */
  @Test
  // This test checks that over-withdrawing is not possible
  public void testDecreasePlayerTokensToNeg() {
    playerBank.incBank(-5, -5, -5,
        -5, -5, -5);
    assertTrue(playerBank.hasAtLeast(0, 0, 0,
        0, 0, 0));
  }
}
