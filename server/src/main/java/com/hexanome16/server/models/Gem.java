package com.hexanome16.server.models;

/**
 * All six splendor gems.
 */
public enum Gem {
  /**
   * Ruby gem.
   */
  RUBY("RED"),
  /**
   * Emerald gem.
   */
  EMERALD("GREEN"),
  /**
   * Sapphire gem.
   */
  SAPPHIRE("BLUE"),
  /**
   * Diamond gem.
   */
  DIAMOND("WHITE"),
  /**
   * Onyx gem.
   */
  ONYX("BLACK"),
  /**
   * Gold gem.
   */
  GOLD("NULL");

  private String bonusTypeEquivalent;

  Gem(String bonusTypeEquivalent) {
    this.bonusTypeEquivalent = bonusTypeEquivalent;
  }

  /**
   * Allows access to bonusTypeEquivalentString.
   *
   * @return Bonus type equivalent.
   */
  public String getBonusType() {
    return bonusTypeEquivalent;
  }
}
