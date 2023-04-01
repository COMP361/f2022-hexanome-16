package com.hexanome16.server.models.cards;

import com.hexanome16.server.models.inventory.Inventory;
import com.hexanome16.server.models.inventory.InventoryAddable;

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
