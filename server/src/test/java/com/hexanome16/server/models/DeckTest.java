package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests for {@link Deck}.
 */
public class DeckTest {

  public final PriceMap priceMap = new PriceMap(1, 2, 3, 4, 5);
  public final Price price = new TokenPrice(priceMap);
  public final DevelopmentCard noble1 = new Noble(0, 3, "texture.png", price);
  public final DevelopmentCard noble2 = new Noble(1, 3, "texture.png", price);
  public Deck deck = new Deck();

  @BeforeEach
  public void reset() {
    deck = new Deck();
    deck.addCard(noble1);
  }

  @Test
  public void testAddCard() {
    deck.addCard(noble2);
    List<DevelopmentCard> cardList = deck.getCardList();
    assertEquals(noble2, cardList.get(1));
  }

  @Test
  public void testRemoveCard() {
    deck.removeCard(noble1);
    List<DevelopmentCard> cardList = deck.getCardList();
    assertEquals(0, cardList.size());
  }

  @Test
  public void testNextCard() {
    assertEquals(noble1, deck.nextCard());
  }

  @Test
  public void testNextCardNull() {
    deck.nextCard();
    assertNull(deck.nextCard());
  }

  @Test
  public void testGetCardList() {
    List<DevelopmentCard> cardList = new ArrayList<DevelopmentCard>();
    cardList.add(noble1);
    assertEquals(cardList, deck.getCardList());
  }

  @Test
  public void testRemainingAmount() {
    deck.nextCard();
    assertEquals(0, deck.remainingAmount());
  }

  @Test
  public void testIsEmpty() {
    deck.removeCard(noble1);
    assertTrue(deck.isEmpty());
  }

  @Test
  public void testShuffled() {
    deck.addCard(noble2);
    deck.shuffle();
    List<DevelopmentCard> shuffledList = deck.getCardList();
    assertTrue(shuffledList.contains(noble1) && shuffledList.contains(noble2));
  }
}
