package com.hexanome16.server.models;

public abstract class DevelopmentCard {
  private final int id;
  private final int prestigePoint;

  private final String texturePath;
  private final Price price;


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
}
