package com.hexanome16.server.models;

/**
 * Holds the token price for a development card.
 */
public class PriceMap {
  int rubyAmount;
  int emeraldAmount;
  int sapphireAmount;
  int diamondAmount;
  int onyxAmount;

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

  public int getRubyAmount() {
    return rubyAmount;
  }

  public int getEmeraldAmount() {
    return emeraldAmount;
  }

  public int getSapphireAmount() {
    return sapphireAmount;
  }

  public int getDiamondAmount() {
    return diamondAmount;
  }

  public int getOnyxAmount() {
    return onyxAmount;
  }
}
