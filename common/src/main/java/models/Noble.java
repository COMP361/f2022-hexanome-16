package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.price.PriceInterface;

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
  public Noble(int id, int prestigePoint, String texturePath, PriceInterface price) {
    cardInfo = new CardInfo(id, prestigePoint, texturePath, price);
  }
}
