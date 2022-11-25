package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Development deck class.
 */
public class Deck {
  private final List<DevelopmentCard> cardList = new ArrayList<>();
  private int index;

  public Deck() {
    this.index = 0;
  }

  public void addCard(DevelopmentCard card) {
    cardList.add(card);
  }

  public void shuffle() {
    Collections.shuffle(cardList);
  }

  /**
   * Return next card in the deck.
   *
   * @return card, null if deck is empty
   */
  public DevelopmentCard nextCard() {
    if (remainingAmount() <= 0) {
      return null;
    }
    return cardList.get(index++);
  }

  public int remainingAmount() {
    return cardList.size() - index - 1;
  }

}
