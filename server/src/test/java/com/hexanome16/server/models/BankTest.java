package com.hexanome16.server.models;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BankTest {
  public GameBank gameBank = new GameBank();
  public PlayerBank playerBank = new PlayerBank();
  @Test
  public void testGameBankDefaultTokens() {
    assertTrue(gameBank.hasAtLeast(7, 7, 7,
        7, 7, 5));
  }

  @Test
  public void testPlayerBankDefaultTokens() {
    assertTrue(playerBank.hasAtLeast(3, 3, 3,
        3, 3, 3));
  }
  @Test
  public void testDecreasePlayerTokens() {
    playerBank.incBank(-1, -1, -1,
        -1, -1, -1);
    assertFalse(playerBank.hasAtLeast(3, 3, 3,
        3, 3, 3));
  }
  @Test
  public void testIncreasePlayerTokens() {
    playerBank.incBank(2, 2, 2,
        2, 2, 2);
    assertTrue(playerBank.hasAtLeast(4, 4, 4,
        4, 4, 4));
  }
  @Test
  // This test checks that over-withdrawing is not possible
  public void testDecreasePlayerTokensToNeg() {
    playerBank.incBank(-5, -5, -5,
        -5, -5, -5);
    assertTrue(playerBank.hasAtLeast(0, 0, 0,
        0, 0, 0));
  }
}
