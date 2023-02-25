package models;

import java.util.Arrays;

/**
 * Six levels of level cards.
 */
public enum Level {
  /**
   * Level one.
   */
  ONE("ONE"),
  /**
   * Level two.
   */
  TWO("TWO"),
  /**
   * Level three.
   */
  THREE("THREE"),
  /**
   * Red level one.
   */
  REDONE("REDONE"),
  /**
   * Red level two.
   */
  REDTWO("REDTWO"),
  /**
   * Red level three.
   */
  REDTHREE("REDTHREE");

  private final String value;

  Level(String level) {
    this.value = level;
  }

  /**
   * Parses a level value from provided string.
   *
   * @param str string to parse
   * @return level value (or null if string is invalid)
   */
  public static Level fromString(String str) {
    if (str == null || str.isBlank()) {
      return null;
    }
    return Arrays.stream(Level.values()).filter(level -> str.trim().equalsIgnoreCase(level.value))
        .findFirst().orElse(null);
  }
}
