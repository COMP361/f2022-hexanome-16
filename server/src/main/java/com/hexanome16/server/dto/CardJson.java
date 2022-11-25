package com.hexanome16.server.dto;

/**
 * For converting json file to level card objects.
 */
public class CardJson {
  String level;
  String bonus;

  int id;
  int prestigePoint;

  int onyxAmount;

  int sapphireAmount;

  int emeraldAmount;

  int rubyAmount;

  int diamondAmount;

  public CardJson() {
  }

  /**
   * Json to java object.
   *
   * @param level          card level
   * @param bonus          card bonus
   * @param prestigePoint  card prestige point
   * @param onyxAmount     onyx amount
   * @param sapphireAmount sapphire amount
   * @param emeraldAmount  emerald amount
   * @param rubyAmount     ruby amount
   * @param diamondAmount  diamond amount
   */
  public CardJson(int id, String level, String bonus, int prestigePoint, int onyxAmount,
                  int sapphireAmount,
                  int emeraldAmount, int rubyAmount, int diamondAmount) {
    this.id = id;
    this.level = level;
    this.bonus = bonus;
    this.prestigePoint = prestigePoint;
    this.onyxAmount = onyxAmount;
    this.sapphireAmount = sapphireAmount;
    this.emeraldAmount = emeraldAmount;
    this.rubyAmount = rubyAmount;
    this.diamondAmount = diamondAmount;
  }

  public String getLevel() {
    return level;
  }

  public String getBonus() {
    return bonus;
  }

  public int getId() {
    return id;
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
