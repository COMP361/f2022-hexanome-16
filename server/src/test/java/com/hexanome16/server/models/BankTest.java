package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.bank.GameBank;
import com.hexanome16.server.models.bank.PlayerBank;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
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
   * init.
   */
  @BeforeEach
  public void init() {
    playerBank = new PlayerBank();
    gameBank = new GameBank();
  }

  /**
   * Test game bank default tokens.
   */
  @Test
  public void testGameBankDefaultTokens() {
    PurchaseMap pm = new PurchaseMap(8, 8, 8,
        8, 8, 0);
    assertTrue(gameBank.toPurchaseMap().canBeUsedToBuy(pm));
  }

  /**
   * Test player bank default tokens.
   */
  @Test
  public void testPlayerBankDefaultTokens() {
    assertFalse(playerBank.toPurchaseMap().canBeUsedToBuy(new PurchaseMap(3, 3, 3,
        3, 3, 3)));
  }

  /**
   * Test decrease player tokens.
   */
  @Test
  public void testDecreasePlayerTokens() {
    playerBank.addGemsToBank(new PurchaseMap(3, 3, 3,
        3, 3, 3));
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
    playerBank.addGemsToBank(new PurchaseMap(1, 1, 1,
            1, 1, 0));
    assertFalse(playerBank.toPurchaseMap().canBeUsedToBuy(new PurchaseMap(6, 6, 6,
        6, 6, 0)));
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

  /**
   * Test hasMoreThanKtokens(int).
   */
  @Test
  public void testHasMoreThanKtokens() {
    playerBank = new PlayerBank(
        new PurchaseMap(1, 1, 1, 1, 1, 1));
    assertFalse(playerBank.hasMoreThanKtokens(6));
    assertTrue(playerBank.hasMoreThanKtokens(5));
  }

  /**
   * Test getOwnedTokenTypes().
   */
  @Test
  public void testGetOwnedTokenTypes() {
    playerBank = new PlayerBank(
        new PurchaseMap(0, 1, 1, 1, 1, 1));
    Gem[] gems = playerBank.getOwnedTokenTypes();
    assertEquals(Set.of(Gem.EMERALD, Gem.SAPPHIRE,
        Gem.DIAMOND, Gem.ONYX, Gem.GOLD),
        Set.copyOf(Arrays.stream(gems).toList()));
  }
}
