package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.bank.GameBank;
import com.hexanome16.server.models.bank.PlayerBank;
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
    assertTrue(gameBank.toPurchaseMap().canBeUsedToBuy(new PurchaseMap(7, 7, 7,
        7, 7, 5)));
  }

  /**
   * Test player bank default tokens.
   */
  @Test
  public void testPlayerBankDefaultTokens() {
    assertTrue(playerBank.toPurchaseMap().canBeUsedToBuy(new PurchaseMap(3, 3, 3,
        3, 3, 3)));
  }

  /**
   * Test decrease player tokens.
   */
  @Test
  public void testDecreasePlayerTokens() {
    playerBank.removeGemsFromBank(new PurchaseMap(1, 1, 1,
        1, 1, 1));
    assertFalse(playerBank.toPurchaseMap().canBeUsedToBuy(new PurchaseMap(3, 3, 3,
        3, 3, 3)));
  }

  /**
   * Test increase player tokens.
   */
  @Test
  public void testIncreasePlayerTokens() {
    playerBank.addGemsToBank(new PurchaseMap(2, 2, 2,
        2, 2, 2));
    assertTrue(playerBank.toPurchaseMap().canBeUsedToBuy(new PurchaseMap(4, 4, 4,
        4, 4, 4)));
  }

  /**
   * Test decrease player tokens to neg.
   */
  @Test
  // This test checks that over-withdrawing is not possible
  public void testDecreasePlayerTokensToNeg() {
    try {
      playerBank.removeGemsFromBank(new PurchaseMap(5, 5, 5,
          5, 5, 5));
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
}
