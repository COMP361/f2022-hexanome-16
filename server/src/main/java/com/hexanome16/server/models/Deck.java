package com.hexanome16.server.models;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Development deck class.
 */
public class Deck implements BroadcastContent {
  private final List<DevelopmentCard> cardList = new ArrayList<>();
  private int index;

  public Deck() {
    this.index = 0;
  }

  public void addCard(DevelopmentCard card) {
    cardList.add(card);
  }

  public void removeCard(DevelopmentCard card) {
    cardList.remove(card);
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

  public List<DevelopmentCard> getCardList() {
    return cardList;
  }

  public int remainingAmount() {
    return cardList.size() - index - 1;
  }

  @Override
  public boolean isEmpty() {
    return cardList.isEmpty();
  }

  public boolean isSameDeck(Deck otherDeck) {
    if (otherDeck == null) {
      return false;
    }
    if (this.getCardList().size() != otherDeck.getCardList().size()) {
      return false;
    }
    for (int i = 0; i < this.getCardList().size() && !this.getCardList().isEmpty(); i++) {
      if (this.getCardList().get(i).getId() != otherDeck.getCardList().get(i).getId()) {
        return false;
      }
    }
    return true;
  }


}
