package com.hexanome16.client.screens.game;

/**
 * Holds the token price for a development card.
 */
public class PriceMap {
  /**
   * The Ruby amount.
   */
  int rubyAmount;
  /**
   * The Emerald amount.
   */
  int emeraldAmount;
  /**
   * The Sapphire amount.
   */
  int sapphireAmount;
  /**
   * The Diamond amount.
   */
  int diamondAmount;
  /**
   * The Onyx amount.
   */
  int onyxAmount;

  /**
   * Instantiates a new Price map.
   */
  public PriceMap() {
  }

  /**
   * Stores a certain price.
   *
   * @param rubyAmount     ruby amount
   * @param emeraldAmount  emerald amount
   * @param sapphireAmount sapphire amount
   * @param diamondAmount  diamond amount
   * @param onyxAmount     onyx amount
   */
  public PriceMap(int rubyAmount, int emeraldAmount, int sapphireAmount, int diamondAmount,
                  int onyxAmount) {
    this.rubyAmount = rubyAmount;
    this.emeraldAmount = emeraldAmount;
    this.sapphireAmount = sapphireAmount;
    this.diamondAmount = diamondAmount;
    this.onyxAmount = onyxAmount;
  }

  /**
   * Gets ruby amount.
   *
   * @return the ruby amount
   */
  public int getRubyAmount() {
    return rubyAmount;
  }

  /**
   * Gets emerald amount.
   *
   * @return the emerald amount
   */
  public int getEmeraldAmount() {
    return emeraldAmount;
  }

  /**
   * Gets sapphire amount.
   *
   * @return the sapphire amount
   */
  public int getSapphireAmount() {
    return sapphireAmount;
  }

  /**
   * Gets diamond amount.
   *
   * @return the diamond amount
   */
  public int getDiamondAmount() {
    return diamondAmount;
  }

  /**
   * Gets onyx amount.
   *
   * @return the onyx amount
   */
  public int getOnyxAmount() {
    return onyxAmount;
  }
}

