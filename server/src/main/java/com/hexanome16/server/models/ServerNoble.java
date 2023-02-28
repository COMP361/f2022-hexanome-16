package com.hexanome16.server.models;

import models.Noble;
import models.price.PriceInterface;

/**
 * Noble class.
 */
public class ServerNoble extends Noble implements Reservable, Visitable {

  /**
   * Instantiates a new Noble.
   *
   * @param id            the id
   * @param prestigePoint the number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   */
  public ServerNoble(int id, int prestigePoint, String texturePath, PriceInterface price) {
    super(id, prestigePoint, texturePath, price);
  }

  @Override
  public boolean addToInventory(Inventory inventory) {
    return inventory.acquireNoble(this);
  }

  @Override
  public boolean reserveCard(Inventory inventory) {
    return inventory.reserveNoble(this);
  }
}
