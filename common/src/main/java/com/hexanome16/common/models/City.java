package com.hexanome16.common.models;

import com.hexanome16.common.models.price.PriceMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for city.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
  protected CardInfo cardInfo;

  /**
   * Constructor.
   *
   * @param id            the id
   * @param prestigePoint the number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   */
  public City(int id, int prestigePoint, String texturePath, PriceMap price) {
    cardInfo = new CardInfo(id, prestigePoint, texturePath, price);
  }
}
