package com.hexanome16.server.models;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

}
