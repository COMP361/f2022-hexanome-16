package com.hexanome16.server.models;

/**
 * Card instead of noble.
 */

public class LevelCard extends DevelopmentCard {
  private final Level level;

  public LevelCard(int id, int prestigePoint, String texturePath, Price price, Level level) {
    super(id, prestigePoint, texturePath, price);
    this.level = level;
  }
}
