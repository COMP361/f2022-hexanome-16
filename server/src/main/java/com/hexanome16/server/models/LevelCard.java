package com.hexanome16.server.models;

import lombok.Getter;

/**
 * Card instead of noble.
 */
@Getter
public class LevelCard extends DevelopmentCard {
  private final Level level;
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
  public LevelCard(int id, int prestigePoint, String texturePath, Price price, Level level) {
    super(id, prestigePoint, texturePath, price);
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
