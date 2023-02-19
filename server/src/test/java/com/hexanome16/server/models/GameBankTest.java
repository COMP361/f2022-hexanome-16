package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hexanome16.server.models.bank.GameBank;
import java.util.ArrayList;
import java.util.Set;
import models.price.Gem;
import models.price.PurchaseMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link GameBank}.
 */
public class GameBankTest {
  private GameBank gameBank;


  /**
   * Done before each test.
   */
  @BeforeEach
  public void init() {
    gameBank = new GameBank();
  }

  /**
   * Testing availableTwoTokensType().
   */
  @Test
  public void testAvailableTwoTokensType() {
    ArrayList<Gem> availableTwoTokens = gameBank.availableTwoTokensType();
    assertEquals(Set.copyOf(availableTwoTokens), Set.of(Gem.RUBY, Gem.SAPPHIRE,
        Gem.DIAMOND, Gem.EMERALD, Gem.ONYX, Gem.GOLD));
    gameBank.removeGemsFromBank(new PurchaseMap(3, 4, 0,
        0, 0, 0));
    availableTwoTokens = gameBank.availableTwoTokensType();
    assertEquals(Set.copyOf(availableTwoTokens), Set.of(Gem.RUBY, Gem.SAPPHIRE,
        Gem.DIAMOND, Gem.ONYX, Gem.GOLD));
  }

  /**
   * Testing availableThreeTokensType().
   */
  @Test
  public void testAvailableThreeTokensType() {
    ArrayList<Gem> availableThreeTokens = gameBank.availableThreeTokensType();
    assertEquals(Set.copyOf(availableThreeTokens), Set.of(Gem.RUBY, Gem.SAPPHIRE,
        Gem.DIAMOND, Gem.EMERALD, Gem.ONYX, Gem.GOLD));
    gameBank.removeGemsFromBank(new PurchaseMap(3, 4, 0,
        0, 7, 0));
    availableThreeTokens = gameBank.availableThreeTokensType();
    assertEquals(Set.copyOf(availableThreeTokens), Set.of(Gem.RUBY, Gem.SAPPHIRE,
        Gem.DIAMOND, Gem.EMERALD, Gem.GOLD));
  }
}
