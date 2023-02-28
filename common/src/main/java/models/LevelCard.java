package models;

import lombok.Getter;
import lombok.Setter;
import models.price.PriceInterface;

/**
 * This class represents a card in a deck of a certain level.
 */
@Getter
public class LevelCard {
  protected final Level level;
  protected final CardInfo cardInfo;
  @Setter
  protected boolean faceDown;

  /**
   * Instantiates a new Level card.
   *
   * @param level         the level
   * @param id            the id
   * @param prestigePoint number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   */
  public LevelCard(Level level, int id, int prestigePoint, String texturePath,
                   PriceInterface price) {
    this.level = level;
    cardInfo = new CardInfo(id, prestigePoint, texturePath, price);
    this.faceDown = true;
  }
}
