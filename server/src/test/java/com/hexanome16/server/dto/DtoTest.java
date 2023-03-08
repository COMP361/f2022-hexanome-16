package com.hexanome16.server.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.hexanome16.common.dto.PlayerJson;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.dto.cards.DevelopmentCardJson;
import com.hexanome16.common.dto.cards.NobleDeckJson;
import com.hexanome16.common.dto.cards.NobleJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.winconditions.WinCondition;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DevelopmentCardJson}, {@link DeckJson}, {@link NobleJson}
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
