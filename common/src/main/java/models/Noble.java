package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.price.PriceInterface;
import models.price.PriceMap;

/**
 * This class represents a noble card.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Noble {
  protected CardInfo cardInfo;

  /**
   * Constructor.
   *
   * @param id            the id
   * @param prestigePoint the number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   */
  public Noble(int id, int prestigePoint, String texturePath, PriceMap price) {
    cardInfo = new CardInfo(id, prestigePoint, texturePath, price);
  }
}
