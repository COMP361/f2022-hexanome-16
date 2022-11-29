package com.hexanome16.server.models;

/**
 * Noble class.
 */

public class Noble extends DevelopmentCard {

  public Noble(int id, int prestigePoint, String texturePath, Price price) {
    super(id, prestigePoint, texturePath, price);
  }

  public boolean addToInventory(Inventory inventory) {
    return inventory.aquireNoble(this);
  }

  public boolean reserveCard(Inventory inventory) {
    return inventory.reserveNoble(this);
  }
}
