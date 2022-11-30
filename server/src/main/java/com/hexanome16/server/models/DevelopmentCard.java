package com.hexanome16.server.models;

/**
 * Development card class.
 */
public abstract class DevelopmentCard {
  private final int id;
  private final int prestigePoint;

  private final String texturePath;
  private final Price price;

  /**
   * Creates a new card object with id, prestigePoint, texturePath and price.
   *
   * @param id            card id
   * @param prestigePoint card prestige point
   * @param texturePath   card texture
   * @param price         card price
   */
  public DevelopmentCard(int id, int prestigePoint, String texturePath, Price price) {
    this.id = id;
    this.prestigePoint = prestigePoint;
    this.price = price;
    this.texturePath = texturePath;
  }

  public int getId() {
    return id;
  }

  public int getPrestigePoint() {
    return prestigePoint;
  }

  public Price getPrice() {
    return price;
  }

  public String getTexturePath() {
    return texturePath;
  }

  /** Add this card to the player's inventory.
   *
   * @return true on success */
  abstract boolean addToInventory(Inventory inventory);

  /** Reserve this card.
   *
   * @return true on success */
  abstract boolean reserveCard(Inventory inventory);
}
