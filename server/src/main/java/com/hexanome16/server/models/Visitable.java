package com.hexanome16.server.models;


/**
 * Interface to enable card visits to players.
 */
public interface Visitable extends InventoryAddable {
  /**
   * Verifies that the inventory meets the requirements of the visitable.
   *
   * @param inventory inventory that need to contain bonuses
   * @return true if the inventory contains enough bonuses to be visited by visitable
   */
  boolean playerMeetsRequirements(Inventory inventory);
}
