package com.hexanome16.server.models;

/**
 * Interface for card to be reservable.
 */
public interface Reservable extends InventoryAddable {
  /**
   * Reserve this card.
   *
   * @param inventory the inventory
   * @return true on success
   */
  boolean reserveCard(Inventory inventory);
}
