package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
  public boolean aquireCard(LevelCard card) {
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

  public boolean aquireNoble(Noble noble) {
    return ownedNobles.add(noble);
  }

  public boolean reserveNoble(Noble noble) {
    return reservedNobles.add(noble);
  }
}
