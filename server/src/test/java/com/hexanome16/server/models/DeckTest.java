package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Deck}.
 */
public class DeckTest {

  /**
   * The Price map.
   */
  public final PriceMap priceMap = new PriceMap(1, 2, 3, 4, 5);
  /**
   * The Price.
   */
  public final Price price = new TokenPrice(priceMap);
  /**
   * The Noble 1.
   */
  public final DevelopmentCard noble1 = new Noble(0, 3, "texture.png", price);
  /**
   * The Noble 2.
   */
  public final DevelopmentCard noble2 = new Noble(1, 3, "texture.png", price);
  /**
   * The Deck.
   */
  public Deck deck = new Deck();

  /**
   * Reset.
   */
  @BeforeEach
  public void reset() {
    deck = new Deck();
    deck.addCard(noble1);
  }

  /**
   * Test add card.
   */
  @Test
  public void testAddCard() {
    deck.addCard(noble2);
    List<DevelopmentCard> cardList = deck.getCardList();
    assertEquals(noble2, cardList.get(1));
  }

  /**
   * Test remove card.
   */
  @Test
  public void testRemoveCard() {
    deck.removeCard(noble1);
    List<DevelopmentCard> cardList = deck.getCardList();
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
    deck.nextCard();
    assertNull(deck.nextCard());
  }

  /**
   * Test get card list.
   */
  @Test
  public void testGetCardList() {
    List<DevelopmentCard> cardList = new ArrayList<DevelopmentCard>();
    cardList.add(noble1);
    assertEquals(cardList, deck.getCardList());
  }

  /**
   * Test remaining amount.
   */
  @Test
  public void testRemainingAmount() {
    deck.nextCard();
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
    List<DevelopmentCard> shuffledList = deck.getCardList();
    assertTrue(shuffledList.contains(noble1) && shuffledList.contains(noble2));
  }
}
