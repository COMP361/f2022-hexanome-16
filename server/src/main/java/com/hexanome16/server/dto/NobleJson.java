package com.hexanome16.server.dto;

/**
 * For converting json file to noble objects.
 */
public class NobleJson {
  int prestigePoint;

  int onyxAmount;

  int sapphireAmount;

  int emeraldAmount;

  int rubyAmount;

  int diamondAmount;

  public NobleJson() {

  }

  /**
   * Json to java object.
   *
   * @param prestigePoint  prestige point
   * @param onyxAmount     onyx amount
   * @param sapphireAmount sapphire amount
   * @param emeraldAmount  emerald amount
   * @param rubyAmount     ruby amount
   * @param diamondAmount  diamond amount
   */
  public NobleJson(int prestigePoint, int onyxAmount, int sapphireAmount, int emeraldAmount,
                   int rubyAmount, int diamondAmount) {
    this.prestigePoint = prestigePoint;
    this.onyxAmount = onyxAmount;
    this.sapphireAmount = sapphireAmount;
    this.emeraldAmount = emeraldAmount;
    this.rubyAmount = rubyAmount;
    this.diamondAmount = diamondAmount;
  }

  public int getPrestigePoint() {
    return prestigePoint;
  }

  public int getOnyxAmount() {
    return onyxAmount;
  }

  public int getSapphireAmount() {
    return sapphireAmount;
  }

  public int getEmeraldAmount() {
    return emeraldAmount;
  }

  public int getRubyAmount() {
    return rubyAmount;
  }

  public int getDiamondAmount() {
    return diamondAmount;
  }
}
