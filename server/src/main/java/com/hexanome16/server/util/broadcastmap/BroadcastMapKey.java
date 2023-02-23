package com.hexanome16.server.util.broadcastmap;

import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.dto.PlayerJson;
import com.hexanome16.server.dto.WinJson;
import com.hexanome16.server.models.Level;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.Arrays;
import lombok.Getter;

public enum BroadcastMapKey {
  PLAYERS("PLAYERS", PlayerJson.class),
  WINNERS("WINNERS", WinJson.class),
  NOBLES("NOBLES", NoblesHash.class),
  ONE("ONE", DeckHash.class),
  TWO("TWO", DeckHash.class),
  THREE("THREE", DeckHash.class),
  REDONE("REDONE", DeckHash.class),
  REDTWO("REDTWO", DeckHash.class),
  REDTHREE("REDTHREE", DeckHash.class);

  private final String value;
  @Getter
  private final Class<? extends BroadcastContent> assocClass;

  BroadcastMapKey(String key, Class<? extends BroadcastContent> assocClass) {
    this.value = key;
    this.assocClass = assocClass;
  }

  public static BroadcastMapKey fromString(String str) {
    if (str == null || str.isBlank()) {
      return null;
    }
    return Arrays.stream(BroadcastMapKey.values()).filter(key -> str.trim().equalsIgnoreCase(key.value))
        .findFirst().orElse(null);
  }

  public static BroadcastMapKey fromLevel(Level level) {
    if (level == null) {
      return null;
    }
    return fromString(level.name());
  }
}
