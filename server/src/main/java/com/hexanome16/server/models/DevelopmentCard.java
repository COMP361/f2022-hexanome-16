package com.hexanome16.server.models;

public abstract class DevelopmentCard {
  private final int id;
  private final int prestigePoint;

  private final Price price;

  public DevelopmentCard(int id, int prestigePoint, Price price) {
    this.id = id;
    this.prestigePoint = prestigePoint;
    this.price = price;
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
}
