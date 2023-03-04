package com.hexanome16.server.models;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.price.PriceMap;
import lombok.Getter;

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
  public ServerLevelCard(int id, int prestigePoint, String texturePath, PriceMap price,
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
