package com.hexanome16.server.models;

/**
 * Noble class.
 */
public class Noble extends DevelopmentCard {

  /**
   * Instantiates a new Noble.
   *
   * @param id            the id
   * @param prestigePoint the number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   */
  public Noble(int id, int prestigePoint, String texturePath, Price price) {
    super(id, prestigePoint, texturePath, price);
  }

  public boolean addToInventory(Inventory inventory) {
    return inventory.acquireNoble(this);
  }

  public boolean reserveCard(Inventory inventory) {
    return inventory.reserveNoble(this);
  }
}
