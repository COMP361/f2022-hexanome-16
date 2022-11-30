package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class DeckTest {

  public Deck deck = new Deck();

  public final PriceMap priceMap = new PriceMap(1, 2, 3, 4, 5);

  public final Price price = new TokenPrice(priceMap);

  public final DevelopmentCard noble_1 = new Noble(0, 3, "texture.png", price);

  public final DevelopmentCard noble_2 = new Noble(1, 3, "texture.png", price);

  @BeforeEach
  public void reset(){
    deck = new Deck();
    deck.addCard(noble_1);
  }

  @Test
  public void testAddCard(){
    deck.addCard(noble_2);
    List cardList = deck.getCardList();
    assertEquals(noble_2, cardList.get(1));
  }

  @Test
  public void testRemoveCard(){
    deck.removeCard(noble_1);
    List cardList = deck.getCardList();
    assertEquals(0, cardList.size());
  }

  @Test
  public void testNextCard(){
    assertEquals(noble_1, deck.nextCard());
  }

  @Test
  public void testGetCardList(){
    List<DevelopmentCard> cardList = new ArrayList<DevelopmentCard>();
    cardList.add(noble_1);
    assertEquals(cardList, deck.getCardList());
  }

  @Test
  public void testRemainingAmount(){
    deck.nextCard();
    assertEquals(0, deck.remainingAmount());
  }

  @Test
  public void testIsEmpty(){
    deck.removeCard(noble_1);
    assertTrue(deck.isEmpty());
  }
}
