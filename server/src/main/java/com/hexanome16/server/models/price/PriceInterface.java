package com.hexanome16.server.models.price;

import java.util.Map;

/**
 * Price of a development card.
 */
public interface PriceInterface {
  /**
   * Gets the amount of gems needed for the provided gem type.
   *
   * @param gem the gem type
   * @return the amount of gems needed
   */
  int getGemCost(Gem gem);

  /**
   * Gets the total amount of gems in this price.
   *
   * @return the total amount of gems
   */
  int getTotalGems();

  /**
   * Adds gems to this price.
   *
   * @param gem    the gem type
   * @param amount the amount of gems to add
   */
  void addGems(Gem gem, int amount);

  /**
   * Adds gems to this price.
   *
   * @param gems the gems to add
   */
  void addGems(Map<Gem, Integer> gems);

  /**
   * Removes gems from this price.
   *
   * @param gem    the gem type
   * @param amount the amount of gems to remove
   */
  void removeGems(Gem gem, int amount);

  /**
   * Removes gems from this price.
   *
   * @param gems the gems to remove
   */
  void removeGems(Map<Gem, Integer> gems);
}
