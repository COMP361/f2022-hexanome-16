package com.hexanome16.server;

import java.util.ArrayList;
import java.util.Hashtable;

import com.hexanome16.server.models.GameBank;
import com.hexanome16.server.models.Gem;
import com.hexanome16.server.models.PlayerBank;
import com.hexanome16.server.models.Token;
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

  public void testDecreasePlayerTokensToNeg() {
    PlayerBank playerBankTest = new PlayerBank();
    playerBankTest.incBank(-4, -4, -4,
        -4, -4, -4);
    assertTrue(playerBankTest.hasAtLeast(0, 0, 0,
        0, 0, 0));
  }

}
