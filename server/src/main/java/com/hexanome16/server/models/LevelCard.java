package com.hexanome16.server.models;

import lombok.Getter;
import models.CardInfo;
import models.Level;
import models.price.PriceInterface;

/**
 * Card instead of noble.
 */
@Getter
public class LevelCard implements Reservable {
  private final Level level;
  private final CardInfo cardInfo;
  private boolean faceDown;

  /**
   * Instantiates a new Level card.
   *
   * @param id            the id
   * @param prestigePoint number of prestige points
   * @param texturePath   the texture path
   * @param price         the price
   * @param level         the level
   */
  public LevelCard(int id, int prestigePoint, String texturePath, PriceInterface price,
                   Level level) {
    cardInfo = new CardInfo(id, prestigePoint, texturePath, price);
    this.level = level;
    this.faceDown = true;
  }

  @Override
  public boolean addToInventory(Inventory inventory) {
    return inventory.acquireCard(this);
  }

  @Override
  public boolean reserveCard(Inventory inventory) {
    return inventory.reserveCard(this);
  }

  /**
   * Change this card's orientation.
   *
   * @param isFaceDown is this card facing down?
   */
  public void setIsFaceDown(boolean isFaceDown) {
    this.faceDown = isFaceDown;
  }

}
