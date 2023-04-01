package com.hexanome16.common.models.price;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import java.util.Set;

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
  @JsonIgnore
  int getTotalGems();

  /**
   * Gets the total amount of gems, NON JOKERS, in this price.
   *
   * @return the total amount of gems, NON JOKERS
   */
  @JsonIgnore
  int getTotalNonJokers();

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

  /**
   * Compares the prices.
   *
   * @param priceInterface the price interface
   * @return true if the given priceInterface has at least the same amount
   */
  boolean hasAtLeastAmountOfGems(PriceInterface priceInterface);

  /**
   * Gets types of gems in price.
   *
   * @return the types of gems contained inside the price
   */
  Set<Gem> getTypesOfGems();

  /**
   * Subtract price interface from the instance.
   *
   * @param priceInterface the price interface to subtract
   * @return new price interface with reduced prices
   */
  PriceInterface subtract(PriceInterface priceInterface);
}
