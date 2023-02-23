package com.hexanome16.server.util.broadcastmap;

import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.dto.PlayerJson;
import com.hexanome16.server.dto.WinJson;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to manage the broadcast content.
 */
public class BroadcastMap {
  private final HashMap<BroadcastMapKey, BroadcastMapValue> broadcastMap;

  /**
   * Default constructor.
   */
  public BroadcastMap(Game game) {
    broadcastMap = new HashMap<>();
    try {
      for (Level level : Level.values()) {
        BroadcastContentManager<DeckHash> broadcastContentManager =
            new BroadcastContentManager<>(new DeckHash(game, level));
        broadcastMap.put(BroadcastMapKey.fromLevel(level),
            BroadcastMapValue.fromManager(broadcastContentManager));
      }
      BroadcastContentManager<PlayerJson> broadcastContentManagerPlayer =
          new BroadcastContentManager<>(
              new PlayerJson(game.getCurrentPlayer().getName()));
      BroadcastContentManager<WinJson> broadcastContentManagerWinners =
          new BroadcastContentManager<>(new WinJson(new String[game.getPlayers().length]));
      BroadcastContentManager<NoblesHash> broadcastContentManagerNoble =
          new BroadcastContentManager<>(new NoblesHash(game));
      broadcastMap.put(BroadcastMapKey.PLAYERS, BroadcastMapValue.fromManager(broadcastContentManagerPlayer));
      broadcastMap.put(BroadcastMapKey.WINNERS, BroadcastMapValue.fromManager(broadcastContentManagerWinners));
      broadcastMap.put(BroadcastMapKey.NOBLES, BroadcastMapValue.fromManager(broadcastContentManagerNoble));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Constructor.
   *
   * @param map The broadcast map.
   */
  public BroadcastMap(HashMap<String, BroadcastContentManager<? extends BroadcastContent>> map) {
    broadcastMap = new HashMap<>();
    for (Map.Entry<String, BroadcastContentManager<?>> entry : map.entrySet()) {
      BroadcastMapKey key = BroadcastMapKey.fromString(entry.getKey());
      if (key == null) {
        throw new IllegalArgumentException("Invalid map key");
      }
      broadcastMap.put(key, BroadcastMapValue.fromManager(entry.getValue()));
    }
  }

  /**
   * Gets the broadcast content manager for the given key.
   *
   * @param key The key to get the broadcast content manager for.
   * @return The broadcast content manager for the given key.
   */
  public BroadcastMapValue getManager(BroadcastMapKey key) {
    return broadcastMap.get(key);
  }

  public void updateValue(BroadcastMapKey key, BroadcastContent value) {
    Class<? extends BroadcastContent> valueClass = key.getAssocClass();
    BroadcastMapValue manager = broadcastMap.get(key);
    if (!valueClass.isAssignableFrom(value.getClass())) {
      throw new IllegalArgumentException("Invalid update value");
    }
    manager;
  }
}
