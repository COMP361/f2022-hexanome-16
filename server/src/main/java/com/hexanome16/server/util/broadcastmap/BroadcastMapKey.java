package com.hexanome16.server.util.broadcastmap;

import com.hexanome16.common.dto.PlayerJson;
import com.hexanome16.common.dto.WinJson;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.dto.cards.NobleDeckJson;
import com.hexanome16.common.models.Level;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.Arrays;
import lombok.Getter;

/**
 * This class describes the keys used in the broadcast map with their associated value class.
 */
public enum BroadcastMapKey {
  /**
   * Current player.
   */
  PLAYERS(PlayerJson.class),
  /**
   * Game winners.
   */
  WINNERS(WinJson.class),
  /**
   * Nobles.
   */
  NOBLES(NobleDeckJson.class),
  /**
   * Level one deck.
   */
  ONE(DeckJson.class),
  /**
   * Level two deck.
   */
  TWO(DeckJson.class),
  /**
   * Level three deck.
   */
  THREE(DeckJson.class),
  /**
   * Red level one deck.
   */
  REDONE(DeckJson.class),
  /**
   * Red level two deck.
   */
  REDTWO(DeckJson.class),
  /**
   * Red level three deck.
   */
  REDTHREE(DeckJson.class);
  @Getter
  private final Class<? extends BroadcastContent> assocClass;

  BroadcastMapKey(Class<? extends BroadcastContent> assocClass) {
    this.assocClass = assocClass;
  }

  /**
   * Returns the enum value associated with the given string.
   *
   * @param str The string.
   * @return The enum value.
   */
  public static BroadcastMapKey fromString(String str) {
    if (str == null || str.isBlank()) {
      return null;
    }
    return Arrays.stream(BroadcastMapKey.values())
        .filter(key -> str.trim().equalsIgnoreCase(key.name())).findFirst().orElse(null);
  }

  /**
   * Returns the enum value associated with the given level.
   *
   * @param level The level.
   * @return The enum value.
   */
  public static BroadcastMapKey fromLevel(Level level) {
    if (level == null) {
      return null;
    }
    return fromString(level.name());
  }
}
