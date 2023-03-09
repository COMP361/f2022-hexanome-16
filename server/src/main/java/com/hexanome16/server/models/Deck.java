package com.hexanome16.server.models;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import lombok.ToString;

/**
 * Development deck class.
 *
 * @param <T> Inventory Addable to be contained in the deck
 */
@ToString
public class Deck<T extends InventoryAddable> implements BroadcastContent {
  private final Stack<T> cardList;

  /**
   * Instantiates a new Deck.
   */
  public Deck() {
    cardList = new Stack<>();
  }

  /**
   * Get copy of deck.
   *
   * @return immutable copy of internal card list
   */
  public List<T> getCardList() {
    return Collections.unmodifiableList(cardList);
  }

  /**
   * Add card.
   *
   * @param card the card to be added.
   */
  public void addCard(T card) {
    cardList.push(card);
  }

  /**
   * Remove card.
   *
   * @param card the card to be removed.
   * @return if the card was in the deck.
   */
  public boolean removeCard(T card) {
    return cardList.remove(card);
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
  public T nextCard() {
    try {
      return cardList.peek();
    } catch (EmptyStackException e) {
      return null;
    }
  }

  /**
   * Remove and return next card from the deck.
   *
   * @return card, null if deck is empty
   */
  public T removeNextCard() {
    try {
      return cardList.pop();
    } catch (EmptyStackException e) {
      return null;
    }
  }

  /**
   * Remaining amount of cards.
   *
   * @return amount of remaining cards
   */
  public int remainingAmount() {
    return cardList.size();
  }

  /**
   * Reverse the deck.
   */
  public void reverse() {
    Collections.reverse(cardList);
  }

  @Override
  public boolean isEmpty() {
    return cardList.isEmpty();
  }

  /**
   * Compares two deck to see if they have the same sequence of cards.
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof final Deck<?> otherDeck)) {
      return false;
    }
    if (!otherDeck.canEqual(this)) {
      return false;
    }
    if (this.getCardList().size() != otherDeck.getCardList().size()) {
      return false;
    }
    for (int i = 0; i < this.getCardList().size() && !this.getCardList().isEmpty(); i++) {
      if (this.getCardList().get(i).getCardInfo().id()
          != otherDeck.getCardList().get(i).getCardInfo().id()) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>canEqual.</p>
   *
   * @param other a {@link java.lang.Object} object
   * @return a boolean
   */
  protected boolean canEqual(final Object other) {
    return other instanceof Deck;
  }

}
