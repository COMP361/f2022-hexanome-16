package com.hexanome16.server.models.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexanome16.common.models.CardInfo;
import com.hexanome16.common.models.City;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.server.models.cards.Visitable;
import com.hexanome16.server.models.inventory.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.NotImplementedException;

/**
 * ServerCity class for the Cities expansion.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerCity extends City implements Visitable {
  private CardInfo cardInfo;


  /**
   * Instantiates a new ServerCity.
   *
   * @param id            the id
   * @param prestigePoint the number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   */
  public ServerCity(int id, int prestigePoint, String texturePath, PriceMap price) {
    cardInfo = new CardInfo(id, prestigePoint, texturePath, price);
  }

  /**
   * Add this card to the player's inventory.
   *
   * @param inventory the inventory
   * @return true on success
   */
  @Override
  @JsonIgnore
  public boolean addToInventory(Inventory inventory) {
    return false;
  }

  @Override
  public boolean playerMeetsRequirements(Inventory inventory) {
    // TODO: add once gem bonuses are ready
    throw new NotImplementedException();
  }
}
