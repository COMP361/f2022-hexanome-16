package com.hexanome16.server.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CardJson}, {@link DeckHash}, {@link NobleJson}
 * , {@link NoblesHash} and {@link PlayerJson}.
 */
public class DtoTest {

  private final Game game = new Game(12345, new Player[]{}, "", "", new BaseWinCondition());

  private final DeckHash deckHash = new DeckHash(game, Level.ONE);

  private final NoblesHash noblesHash = new NoblesHash(game);

  private final PlayerJson playerJson = new PlayerJson("player");

  /**
   * Instantiates a new Dto test.
   *
   * @throws IOException the io exception
   */
  public DtoTest() throws IOException {
  }

  /**
   * Test deck not empty.
   */
  @Test
  public void testDeckEmpty() {
    assertFalse(deckHash.isEmpty());
  }

  /**
   * Test nobles not empty.
   */
  @Test
  public void testNoblesNotEmpty() {
    assertFalse(noblesHash.isEmpty());
  }

  /**
   * Test player not empty.
   */
  @Test
  public void testPlayerNotEmpty() {
    assertFalse(playerJson.isEmpty());
  }
}
