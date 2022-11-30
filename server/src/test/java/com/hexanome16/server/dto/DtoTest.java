package com.hexanome16.server.dto;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CardJson}, {@link DeckHash}, {@link NobleJson}
 * , {@link NoblesHash} and {@link PlayerJson}.
 */
public class DtoTest {

  private final Game game = new Game(12345, null, "", "");

  private final DeckHash deckHash = new DeckHash(game, Level.ONE);

  private final NoblesHash noblesHash = new NoblesHash(game);

  private final PlayerJson playerJson = new PlayerJson("player");

  public DtoTest() throws IOException {
  }

  @Test
  public void testDeckEmpty() {
    assertNotNull(deckHash.isEmpty());
  }

  @Test
  public void testNoblesEmpty() {
    assertNotNull(noblesHash.isEmpty());
  }

  @Test
  public void testPlayerEmpty() {
    assertNotNull(playerJson.isEmpty());
  }
}
