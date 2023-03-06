package com.hexanome16.server.models;

import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.price.PriceMap;
import org.apache.commons.lang.NotImplementedException;

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
  public ServerNoble(int id, int prestigePoint, String texturePath, PriceMap price) {
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

  @Override
  public boolean playerMeetsRequirements(Inventory inventory) {
    //TODO: add verification once gem bonuses are made
    throw new NotImplementedException();
  }
}
