package com.hexanome16.server.util.broadcastmap;

import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.dto.PlayerJson;
import com.hexanome16.server.dto.WinJson;
import com.hexanome16.server.models.Level;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.Arrays;
import lombok.Getter;

/**
 * This class describes the keys used in the broadcast map with their associated value class.
 */
public enum BroadcastMapKey {
  PLAYERS(PlayerJson.class),
  WINNERS(WinJson.class),
  NOBLES(NoblesHash.class),
  ONE(DeckHash.class),
  TWO(DeckHash.class),
  THREE(DeckHash.class),
  REDONE(DeckHash.class),
  REDTWO(DeckHash.class),
  REDTHREE(DeckHash.class);
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
