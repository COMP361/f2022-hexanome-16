package com.hexanome16.server.models;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;

/**
 * Development deck class.
 */
@Data
public class Deck implements BroadcastContent {
  private final List<DevelopmentCard> cardList = new ArrayList<>();
  private int index;

  /**
   * Instantiates a new Deck.
   */
  public Deck() {
    this.index = 0;
  }

  /**
   * Add card.
   *
   * @param card the card to be added.
   */
  public void addCard(DevelopmentCard card) {
    cardList.add(card);
  }

  /**
   * Remove card.
   *
   * @param card the card to be removed.
   */
  public void removeCard(DevelopmentCard card) {
    cardList.remove(card);
  }

  /**
   * Shuffle the deck.
   */
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

  /**
   * Remaining amount of cards.
   *
   * @return amount of remaining cards
   */
  public int remainingAmount() {
    return cardList.size() - index;
  }

  @Override
  public boolean isEmpty() {
    return cardList.isEmpty();
  }

  /**
   * Compares two deck to see if they have the same sequence of cards.
   *
   * @param otherDeck second deck used for comparison.
   * @return true if same, else false.
   */
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
