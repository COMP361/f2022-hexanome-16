package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Player inventory class.
 */
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

  /* setters *************************************************************************************/
  public void setPlayerBank(PlayerBank playerBank) {
    this.playerBank = playerBank;
  }

  public void setOwnedNobles(List<Noble> ownedNobles) {
    this.ownedNobles = ownedNobles;
  }

  public void setReservedNobles(List<Noble> reservedNobles) {
    this.reservedNobles = reservedNobles;
  }

  public void setOwnedCards(List<LevelCard> ownedCards) {
    this.ownedCards = ownedCards;
  }

  public void setReservedCards(List<LevelCard> reservedCards) {
    this.reservedCards = reservedCards;
  }

  /* getters *************************************************************************************/
  public PlayerBank getPlayerBank() {
    return this.playerBank;
  }

  public List<Noble> getOwnedNobles() {
    return this.ownedNobles;
  }

  public List<Noble> getReservedNobles() {
    return this.reservedNobles;
  }

  public List<LevelCard> getOwnedCards() {
    return this.ownedCards;
  }

  public List<LevelCard> getReservedCards() {
    return this.reservedCards;
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
