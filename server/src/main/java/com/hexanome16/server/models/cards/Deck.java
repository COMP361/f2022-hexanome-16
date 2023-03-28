package com.hexanome16.server.models.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexanome16.server.models.inventory.InventoryAddable;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.Arrays;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Development deck class.
 *
 * @param <T> Inventory Addable to be contained in the deck
 */
@ToString
@Data
public class Deck<T extends InventoryAddable> implements BroadcastContent {
  private final Stack<T> cardList;

  /**
   * Instantiates a new Deck.
   */
  public Deck() {
    cardList = new Stack<>();
  }

  /**
   * Instantiates a new Deck.
   *
   * @param cardList the card list
   */
  public Deck(T[] cardList) {
    this.cardList = new Stack<>();
    this.cardList.addAll(Arrays.stream(cardList).toList());
  }

  /**
   * Get copy of deck.
   *
   * @return immutable copy of internal card list
   */
  @JsonIgnore
  public List<T> getCardList() {
    return Collections.unmodifiableList(cardList);
  }

  /**
   * Add card.
   *
   * @param card the card to be added.
   */
  @JsonIgnore
  public void addCard(T card) {
    cardList.push(card);
  }

  /**
   * Remove card.
   *
   * @param card the card to be removed.
   * @return if the card was in the deck.
   */
  @JsonIgnore
  public boolean removeCard(T card) {
    return cardList.remove(card);
  }

  /**
   * Shuffle the deck.
   */
  @JsonIgnore
  public void shuffle() {
    Collections.shuffle(cardList);
  }

  /**
   * Return next card in the deck.
   *
   * @return card, null if deck is empty
   */
  @JsonIgnore
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
  @JsonIgnore
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
  @JsonIgnore
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
  @JsonIgnore
  public boolean isEmpty() {
    return cardList.isEmpty();
  }

  /**
   * Compares two deck to see if they have the same sequence of cards.
   */
  @Override
  @JsonIgnore
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
    for (InventoryAddable card : this.getCardList()) {
      if (!otherDeck.getCardList().contains(card)) {
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
  @JsonIgnore
  protected boolean canEqual(final Object other) {
    return other instanceof Deck;
  }

}
