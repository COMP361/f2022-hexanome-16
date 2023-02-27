package com.hexanome16.server.models;

import lombok.Getter;
import models.Level;
import models.LevelCard;
import models.price.PriceInterface;

/**
 * Card instead of noble.
 */
@Getter
public class ServerLevelCard extends LevelCard implements InventoryAddable, Reservable {

  /**
   * Instantiates a new Level card.
   *
   * @param id            the id
   * @param prestigePoint number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   * @param level         the level
   */
  public ServerLevelCard(int id, int prestigePoint, String texturePath, PriceInterface price,
                         Level level) {
    super(level, id, prestigePoint, texturePath, price);
  }

  @Override
  public boolean addToInventory(Inventory inventory) {
    return inventory.acquireCard(this);
  }

  @Override
  public boolean reserveCard(Inventory inventory) {
    return inventory.reserveCard(this);
  }

}
