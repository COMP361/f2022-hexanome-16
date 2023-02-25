package com.hexanome16.server.util.broadcastmap;

import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.models.Game;
import dto.PlayerJson;
import dto.WinJson;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.util.HashMap;
import models.Level;

/**
 * This class is used to manage the broadcast content.
 */
public class BroadcastMap {
  private final HashMap<BroadcastMapKey, BroadcastContentManager<BroadcastContent>> broadcastMap;

  /**
   * Default constructor.
   *
   * @param game The game.
   */
  public BroadcastMap(Game game) {
    broadcastMap = new HashMap<>();
    try {
      for (Level level : Level.values()) {
        BroadcastContentManager<BroadcastContent> broadcastContentManager =
            new BroadcastContentManager<>(new DeckHash(game, level));
        broadcastMap.put(BroadcastMapKey.fromLevel(level), broadcastContentManager);
      }
      BroadcastContentManager<BroadcastContent> broadcastContentManagerPlayer =
          new BroadcastContentManager<>(
              new PlayerJson(game.getCurrentPlayer().getName()));
      BroadcastContentManager<BroadcastContent> broadcastContentManagerWinners =
          new BroadcastContentManager<>(new WinJson(new String[game.getPlayers().length]));
      BroadcastContentManager<BroadcastContent> broadcastContentManagerNoble =
          new BroadcastContentManager<>(new NoblesHash(game));
      broadcastMap.put(BroadcastMapKey.PLAYERS, broadcastContentManagerPlayer);
      broadcastMap.put(BroadcastMapKey.WINNERS, broadcastContentManagerWinners);
      broadcastMap.put(BroadcastMapKey.NOBLES, broadcastContentManagerNoble);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
    BroadcastContentManager<BroadcastContent> manager = broadcastMap.get(key);
    if (!valueClass.equals(value.getClass())) {
      throw new IllegalArgumentException("Invalid update value");
    }
    manager.updateBroadcastContent(value);
  }
}
