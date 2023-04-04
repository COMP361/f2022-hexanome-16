package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.hexanome16.common.models.CardInfo;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.server.models.cards.Deck;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.inventory.InventoryAddable;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link Deck}.
 */
public class DeckTest {

  /**
   * The Price map.
   */
  public final PriceMap priceMap = new PriceMap(1, 2, 3, 4, 5);
  /**
   * The Noble 1.
   */
  public final ServerNoble noble1 = new ServerNoble(0, 3, "texture.png", priceMap);
  /**
   * The Noble 2.
   */
  public final ServerNoble noble2 = new ServerNoble(1, 3, "texture.png", priceMap);
  /**
   * The Deck.
   */
  public Deck<InventoryAddable> deck;

  /**
   * Reset.
   */
  @BeforeEach
  public void reset() {
    deck = new Deck<>();
    deck.addCard(noble1);
  }

  /**
   * Test add card.
   */
  @Test
  public void testAddCard() {
    deck.addCard(noble2);
    List<InventoryAddable> cardList = deck.getCardList();
    assertEquals(noble2, cardList.get(1));
  }

  /**
   * Test remove card.
   */
  @Test
  public void testRemoveCard() {
    deck.removeCard(noble1);
    List<InventoryAddable> cardList = deck.getCardList();
    assertEquals(0, cardList.size());
  }

  /**
   * Test next card.
   */
  @Test
  public void testNextCard() {
    assertEquals(noble1, deck.nextCard());
  }

  /**
   * Test next card null.
   */
  @Test
  public void testNextCardNull() {
    deck.removeNextCard();
    assertNull(deck.nextCard());
  }

  /**
   * Test get card list.
   */
  @Test
  public void testGetCardList() {
    List<InventoryAddable> cardList = new ArrayList<>();
    cardList.add(noble1);
    assertEquals(cardList, deck.getCardList());
  }

  /**
   * Test get card list is immutable.
   */
  @Test
  public void testGetCardListImmutable() {
    try {
      deck.getCardList().add(noble2);
      fail();
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
  }

  /**
   * Test remaining amount.
   */
  @Test
  public void testRemainingAmount() {
    deck.removeNextCard();
    assertEquals(0, deck.remainingAmount());
  }

  /**
   * Test is empty.
   */
  @Test
  public void testIsEmpty() {
    deck.removeCard(noble1);
    assertTrue(deck.isEmpty());
  }

  /**
   * Test shuffled.
   */
  @Test
  public void testShuffled() {
    deck.addCard(noble2);
    deck.shuffle();
    List<InventoryAddable> shuffledList = deck.getCardList();
    assertTrue(shuffledList.contains(noble1) && shuffledList.contains(noble2));
  }

  @Test
  public void testEqualsEmpty() {
    Deck<ServerNoble> deck1 = new Deck<>();
    Deck<ServerNoble> deck2 = new Deck<>();
    assertEquals(deck1, deck2);
  }

  @Test
  public void testEqualsNotEmpty() {
    // Arrange
    ServerNoble mockNoble = Mockito.mock(ServerNoble.class);
    when(mockNoble.getCardInfo()).thenReturn(new CardInfo(1, 2, "boo", null));
    Deck<ServerNoble> deck1 = new Deck<>();
    Deck<ServerNoble> deck2 = new Deck<>();
    deck1.addCard(mockNoble);
    deck2.addCard(mockNoble);

    // Act

    // Assert
    assertEquals(deck1, deck2);
  }

  @Test
  public void testNotEquals() {
    // Arrange
    ServerNoble mockNoble1 = Mockito.mock(ServerNoble.class);
    when(mockNoble1.getCardInfo()).thenReturn(new CardInfo(1, 2, "boo", null));
    ServerNoble mockNoble2 = Mockito.mock(ServerNoble.class);
    when(mockNoble2.getCardInfo()).thenReturn(new CardInfo(2, 2, "boo", null));
    Deck<ServerNoble> deck1 = new Deck<>();
    Deck<ServerNoble> deck2 = new Deck<>();
    deck1.addCard(mockNoble1);
    deck2.addCard(mockNoble2);

    // Act

    // Assert
    assertNotEquals(deck1, deck2);
  }
}
