package com.hexanome16.server.util.broadcastmap;

import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.dto.PlayerJson;
import com.hexanome16.server.dto.WinJson;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.util.Arrays;
import lombok.Getter;

public enum BroadcastMapValue {
  PLAYERS_VALUE(PlayerJson.class),
  WINNERS_VALUE(WinJson.class),
  NOBLES_VALUE(NoblesHash.class),
  DECK_VALUE(DeckHash.class);

  @Getter
  private final Class<? extends BroadcastContent> value;

  BroadcastMapValue(Class<? extends BroadcastContent> value) {
    this.value = value;
  }

  public static BroadcastMapValue fromManager(BroadcastContentManager<? extends BroadcastContent> manager) {
    return Arrays.stream(BroadcastMapValue.values()).filter(
        val -> val.value.equals(manager.getCurrentBroadcastContent().getClass())
    ).findFirst().orElse(null);
  }
}
