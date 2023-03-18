package com.hexanome16.server.models.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.inventory.Inventory;
import com.hexanome16.server.models.inventory.InventoryAddable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Card instead of noble.
 */
@Data
@NoArgsConstructor
public class ServerLevelCard extends LevelCard implements InventoryAddable, Reservable {

  /**
   * Instantiates a new Level card.
   *
   * @param id            the id
   * @param prestigePoint number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   * @param level         the level
   * @param gemBonus      gem bonus given when card is bought
   */
  public ServerLevelCard(int id, int prestigePoint, String texturePath, PriceMap price,
                         Level level, PurchaseMap gemBonus) {
    super(level, id, prestigePoint, texturePath, price, gemBonus);
  }

  /**
   * Instantiates a new Level card.
   *
   * @param id            the id
   * @param prestigePoint number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   * @param level         the level
   * @param bonusType     the bonus type.
   * @param gemBonus      gem bonus given when card is bought
   */
  public ServerLevelCard(int id, int prestigePoint, String texturePath, PriceMap price,
                         Level level, BonusType bonusType, PurchaseMap gemBonus) {
    super(level, id, prestigePoint, texturePath, price, bonusType, gemBonus);
  }

  @JsonIgnore
  @Override
  public boolean addToInventory(Inventory inventory) {
    return inventory.acquireCard(this);
  }

  @JsonIgnore
  @Override
  public boolean reserveCard(Inventory inventory) {
    return inventory.reserveCard(this);
  }

}
