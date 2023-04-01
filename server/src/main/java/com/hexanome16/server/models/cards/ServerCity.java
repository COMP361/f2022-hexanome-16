package com.hexanome16.server.models.cards;

import com.hexanome16.common.models.City;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.server.models.inventory.Inventory;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ServerCity class for the Cities expansion.
 */
@Data
@NoArgsConstructor
public class ServerCity extends City implements Visitable {
  /**
   * Instantiates a new ServerCity.
   *
   * @param id            the id
   * @param prestigePoint the number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   */
  public ServerCity(int id, int prestigePoint, String texturePath, PriceMap price) {
    super(id, prestigePoint, texturePath, price);
  }

  /**
   * Add this card to the player's inventory.
   *
   * @param inventory the inventory
   * @return true on success
   */
  @Override
  public boolean addToInventory(Inventory inventory) {
    return false;
  }

  @Override
  public boolean playerMeetsRequirements(Inventory inventory) {
    System.out.println("player prestige point: " + inventory.getPrestigePoints());
    System.out.println("city prestige point: " + cardInfo.prestigePoint());
    System.out.println(inventory.getGemBonuses());
    System.out.println(cardInfo.price());
    return inventory.getPrestigePoints() >= cardInfo.prestigePoint()
        && inventory.getGemBonuses().getGemCost(Gem.RUBY) >= cardInfo.price().getGemCost(Gem.RUBY)
        && inventory.getGemBonuses().getGemCost(Gem.EMERALD)
        >= cardInfo.price().getGemCost(Gem.EMERALD)
        && inventory.getGemBonuses().getGemCost(Gem.SAPPHIRE)
        >= cardInfo.price().getGemCost(Gem.SAPPHIRE)
        && inventory.getGemBonuses().getGemCost(Gem.DIAMOND)
        >= cardInfo.price().getGemCost(Gem.DIAMOND)
        && inventory.getGemBonuses().getGemCost(Gem.ONYX) >= cardInfo.price().getGemCost(Gem.ONYX);
  }
}
