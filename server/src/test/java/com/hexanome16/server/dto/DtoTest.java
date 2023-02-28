package com.hexanome16.server.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.winconditions.WinCondition;
import dto.CardJson;
import dto.DeckJson;
import dto.NobleDeckJson;
import dto.NobleJson;
import dto.PlayerJson;
import java.io.IOException;
import models.Level;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CardJson}, {@link DeckJson}, {@link NobleJson}
 * , {@link NobleDeckJson} and {@link PlayerJson}.
 */
public class DtoTest {

  private final Game game =
      Game.create(12345, new ServerPlayer[] {}, "", "", new WinCondition[] {WinCondition.BASE});

  private final DeckJson deckJson = new DeckJson(game.getLevelDeck(Level.ONE).getCardList(),
      Level.ONE);

  private final NobleDeckJson nobleDeckJson = new NobleDeckJson(game.getNobleDeck().getCardList());

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
    assertFalse(deckJson.isEmpty());
  }

  /**
   * Test nobles not empty.
   */
  @Test
  public void testNoblesNotEmpty() {
    assertFalse(nobleDeckJson.isEmpty());
  }

  /**
   * Test player not empty.
   */
  @Test
  public void testPlayerNotEmpty() {
    assertFalse(playerJson.isEmpty());
  }
}
