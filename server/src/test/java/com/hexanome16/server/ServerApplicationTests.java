package com.hexanome16.server;

import com.hexanome16.server.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;


@SpringBootTest(classes = ServerApplicationTests.class)
class ServerApplicationTests {

  @Test
  void contextLoads() {
  }

  @Test
  public void testGameBankDefaultTokens() {
    GameBank gameBankTest = new GameBank();
    assertTrue(gameBankTest.hasAtLeast(7, 7, 7,
        7, 7, 5));
  }

  @Test
  public void testPlayerBankDefaultTokens() {
    PlayerBank playerBankTest = new PlayerBank();
    assertTrue(playerBankTest.hasAtLeast(3, 3, 3,
        3, 3, 3));
  }
  @Test
  public void testDecreasePlayerTokens() {
    PlayerBank playerBankTest = new PlayerBank();
    playerBankTest.incBank(-1, -1, -1,
        -1, -1, -1);
    assertFalse(playerBankTest.hasAtLeast(3, 3, 3,
        3, 3, 3));
  }
  @Test
  public void testIncreasePlayerTokens() {
    PlayerBank playerBankTest = new PlayerBank();
    playerBankTest.toString();
    playerBankTest.incBank(1, 1, 1,
        1, 1, 1);
    assertTrue(playerBankTest.hasAtLeast(4, 4, 4,
        4, 4, 4));
  }
  @Test
  // This test checks that over-withdrawing is not possible
  public void testDecreasePlayerTokensToNeg() {
    PlayerBank playerBankTest = new PlayerBank();
    playerBankTest.incBank(-4, -4, -4,
        -4, -4, -4);
    assertTrue(playerBankTest.hasAtLeast(0, 0, 0,
        0, 0, 0));
  }

  @Test
  public void testInitBagBonusType() {
    BagBonus bagBonusTest = new BagBonus(BonusType.CARD_BONUS);
    assertEquals(BonusType.CARD_BONUS, bagBonusTest.getBonusType());
  }

  @Test
  public void testInitBagBonusGem() {
    BagBonus bagBonusTest = new BagBonus(BonusType.CARD_BONUS);
    bagBonusTest.setChosenGem(Gem.RUBY);
    assertEquals(Gem.RUBY, bagBonusTest.getChosenGem());
  }

  @Test
  public void testDeckShuffle() {
    Deck deckTest1 = new Deck();
    deckTest1.shuffle();
    Deck deckTest2 = new Deck();
    deckTest2.shuffle();
    System.out.println(deckTest1.isSameDeck(deckTest2));
    assertFalse(deckTest1.isSameDeck(deckTest2));
  }

}
