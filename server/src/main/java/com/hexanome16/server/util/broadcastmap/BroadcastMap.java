package com.hexanome16.server.util.broadcastmap;

import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.models.Game;
import dto.PlayerJson;
import dto.WinJson;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import models.Level;

/**
 * This class is used to manage the broadcast content.
 */
public class BroadcastMap {
  private final Map<BroadcastMapKey, BroadcastContentManager<BroadcastContent>> broadcastMap;

  /**
   * Default constructor.
   *
   * @param game The game.
   */
  public BroadcastMap(Game game) {
    Map<BroadcastMapKey, BroadcastContentManager<BroadcastContent>> map = new HashMap<>();
    try {
      for (Level level : Level.values()) {
        BroadcastContentManager<BroadcastContent> broadcastContentManager =
            new BroadcastContentManager<>(new DeckHash(game, level));
        map.put(BroadcastMapKey.fromLevel(level), broadcastContentManager);
      }
      BroadcastContentManager<BroadcastContent> broadcastContentManagerPlayer =
          new BroadcastContentManager<>(
              new PlayerJson(game.getCurrentPlayer().getName()));
      BroadcastContentManager<BroadcastContent> broadcastContentManagerWinners =
          new BroadcastContentManager<>(new WinJson(new String[game.getPlayers().length]));
      BroadcastContentManager<BroadcastContent> broadcastContentManagerNoble =
          new BroadcastContentManager<>(new NoblesHash(game.getNobleDeck()));
      map.put(BroadcastMapKey.PLAYERS, broadcastContentManagerPlayer);
      map.put(BroadcastMapKey.WINNERS, broadcastContentManagerWinners);
      map.put(BroadcastMapKey.NOBLES, broadcastContentManagerNoble);
    } catch (Exception e) {
      e.printStackTrace();
    }
    broadcastMap = Collections.unmodifiableMap(map);
  }

  /**
   * Gets the broadcast content manager for the given key.
   *
   * @param key The key to get the broadcast content manager for.
   * @return The broadcast content manager for the given key.
   */
  public BroadcastContentManager<BroadcastContent> getManager(BroadcastMapKey key) {
    return broadcastMap.get(key);
  }

  /**
   * Updates the broadcast content for the given key.
   *
   * @param key The key to update the broadcast content for.
   * @param value The new broadcast content.
   */
  public void updateValue(BroadcastMapKey key, BroadcastContent value) {
    Class<? extends BroadcastContent> valueClass = key.getAssocClass();
    if (!valueClass.equals(value.getClass())) {
      throw new IllegalArgumentException("Invalid update value");
    }
    broadcastMap.get(key).updateBroadcastContent(value);
  }
}
