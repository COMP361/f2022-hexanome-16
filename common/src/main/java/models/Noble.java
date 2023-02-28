package models;

import lombok.Getter;
import models.price.PriceInterface;

/**
 * This class represents a noble card.
 */
public class Noble {
  @Getter
  protected final CardInfo cardInfo;

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
