package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Game}.
 */
public class GameTest {
  private final Game game = new Game(12345, null, "", "");

  public GameTest() throws IOException {
  }

  @Test
  public void testGetDeck() {
    assertNotNull(game.getDeck(Level.ONE));
  }

  @Test
  public void testGetOnBoardDeck() {
    assertNotNull(game.getOnBoardDeck(Level.ONE));
  }

  @Test
  public void testAddOnBoardCard() {
    List cardList = game.getNobleDeck().getCardList();
    game.addOnBoardCard(Level.ONE);
    assertNotEquals(cardList.size() + 1, game.getOnBoardDeck(Level.ONE).getCardList().size());
  }

  @Test
  public void testRemoveOnBoardCard() {
    List<DevelopmentCard> cardList = game.getDeck(Level.ONE).getCardList();
    DevelopmentCard card = cardList.get(0);
    game.removeOnBoardCard((LevelCard) card);
    assertFalse(game.getOnBoardDeck(Level.ONE).getCardList().contains(card));
  }
}
