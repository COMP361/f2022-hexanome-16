package com.hexanome16.server.dto;

public class CardJson {
  String level, bonus;
  int prestigePoint, onyxAmount, sapphireAmount, emeraldAmount, rubyAmount, diamondAmount;

  public CardJson() {
  }

  public CardJson(String level, String bonus, int prestigePoint, int onyxAmount, int sapphireAmount,
                  int emeraldAmount, int rubyAmount, int diamondAmount) {
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
