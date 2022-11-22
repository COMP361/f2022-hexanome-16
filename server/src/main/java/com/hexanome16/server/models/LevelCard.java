package com.hexanome16.server.models;

public class LevelCard extends DevelopmentCard {
  private final Level level;

  public LevelCard(int id, int prestigePoint, Price price, Level level) {
    super(id, prestigePoint, price);
    this.level = level;
  }
}
