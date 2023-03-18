package com.hexanome16.server.models.cards;

import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.server.models.inventory.Inventory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Noble class.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
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
    if (!inventory.hasAtLeastGivenBonuses(cardInfo.price())) {
      return false;
    }
    return inventory.acquireNoble(this);
  }

  @Override
  public boolean reserveCard(Inventory inventory) {
    return inventory.reserveNoble(this);
  }

  @Override
  public boolean playerMeetsRequirements(Inventory inventory) {
    return inventory.hasAtLeastGivenBonuses(cardInfo.price());
  }
}
