package models.price;

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

  private final String bonusTypeEquivalent;

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

  /**
   * Returns the gem associated to a given String representation of a bonus type from the client.
   * Returns null if string is NULL or anything else.
   *
   * @param bonusTypeString BonusType as a string. (from client) (under game/prompts/viewprompts).
   * @return The gem associated to the bonus type.
   */
  public static Gem getGem(String bonusTypeString) {
    return switch (bonusTypeString) {
      case "RED" -> Gem.RUBY;
      case "GREEN" -> Gem.EMERALD;
      case "BLUE" -> Gem.SAPPHIRE;
      case "WHITE" -> Gem.DIAMOND;
      case "BLACK" -> Gem.ONYX;
      default -> null;
    };
  }

  /**
   * Returns true if the 3 gems are distinct, false otherwise.
   *
   * @param gem1 First gem.
   * @param gem2 Second gem.
   * @param gem3 Third gem.
   * @return true if the 3 gems are distinct, false otherwise.
   */
  public static boolean areDistinct(Gem gem1, Gem gem2, Gem gem3) {
    return gem1 != gem2 && gem2 != gem3 && gem3 != gem1;
  }

}
