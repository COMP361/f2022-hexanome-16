package com.hexanome16.server.models;

public class PriceMap {
  int rubyAmount;
  int emeraldAmount;
  int sapphireAmount;
  int diamondAmount;
  int onyxAmount;

  public PriceMap(int rubyAmount, int emeraldAmount, int sapphireAmount, int diamondAmount,
                  int onyxAmount) {
    this.rubyAmount = rubyAmount;
    this.emeraldAmount = emeraldAmount;
    this.sapphireAmount = sapphireAmount;
    this.diamondAmount = diamondAmount;
    this.onyxAmount = onyxAmount;
  }
}
