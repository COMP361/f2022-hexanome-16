package com.hexanome16.server.models;

import com.hexanome16.server.models.bank.PlayerBank;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Player inventory class.
 */
@Data
public class Inventory {
  /* fields *************************************************************************************/
  private PlayerBank playerBank;
  private List<Noble> ownedNobles;
  private List<Noble> reservedNobles;
  private List<LevelCard> ownedCards;
  private List<LevelCard> reservedCards;

  /* Constructor *********************************************************************************/

  /**
   * Creates Inventory and sets up all fields.
   */
  public Inventory() {
    playerBank = new PlayerBank();
    ownedNobles = new ArrayList<>();
    reservedNobles = new ArrayList<>();
    ownedCards = new ArrayList<>();
    reservedCards = new ArrayList<>();
  }

  /* add methods ******************************************************************************/
  /**
   * Acquire card.
   *
   * @param card the card to add
   * @return true if the card was added to inventory
   */
  public boolean acquireCard(LevelCard card) {
    return ownedCards.add(card);
  }

  /**
   * Reserve the given card.
   *
   * @param card card to reserve
   * @return if the card has been reserved successfully
   */
  public boolean reserveCard(LevelCard card) {
    // if the player already has three reserved cards
    if (reservedCards.size() >= 3) {
      return false;
    }
    return reservedCards.add(card);
  }

  /**
   * Acquire noble boolean.
   *
   * @param noble the noble
   * @return the boolean
   */
  public boolean acquireNoble(Noble noble) {
    return ownedNobles.add(noble);
  }

  /**
   * Reserve noble boolean.
   *
   * @param noble the noble
   * @return the boolean
   */
  public boolean reserveNoble(Noble noble) {
    return reservedNobles.add(noble);
  }
}
