package com.hexanome16.server.models;

import lombok.Getter;
import models.CardInfo;
import models.price.PriceInterface;

/**
 * Noble class.
 */
public class Noble implements Reservable, Visitable {

  @Getter
  private final CardInfo cardInfo;

  /**
   * Instantiates a new Noble.
   *
   * @param id            the id
   * @param prestigePoint the number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   */
  public Noble(int id, int prestigePoint, String texturePath, PriceInterface price) {
    cardInfo = new CardInfo(id, prestigePoint, texturePath, price);
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
