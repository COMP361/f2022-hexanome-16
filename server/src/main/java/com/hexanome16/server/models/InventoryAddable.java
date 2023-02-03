package com.hexanome16.server.models;

/**
 * Interface to enabling adding object to an inventory.
 */
public interface InventoryAddable {
  /**
   * Add this card to the player's inventory.
   *
   * @param inventory the inventory
   * @return true on success
   */
  boolean addToInventory(Inventory inventory);

  /**
   * Get Card info.
   *
   * @return card info
   */
  CardInfo getCardInfo();
}
