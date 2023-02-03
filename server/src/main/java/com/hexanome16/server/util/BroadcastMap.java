package com.hexanome16.server.util;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.util.HashMap;
import org.springframework.stereotype.Component;

/**
 * This class is used to manage the broadcast content.
 */
@Component
public class BroadcastMap {
  private final HashMap<String, BroadcastContentManager<?>> broadcastMap;

  /**
   * Default constructor.
   */
  public BroadcastMap() {
    broadcastMap = new HashMap<>();
  }

  /**
   * Constructor.
   *
   * @param broadcastMap The broadcast map.
   */
  public BroadcastMap(HashMap<String, BroadcastContentManager<?>> broadcastMap) {
    this.broadcastMap = broadcastMap;
  }

  /**
   * Gets the broadcast content manager for the given key.
   *
   * @param key The key to get the broadcast content manager for.
   * @return The broadcast content manager for the given key.
   */
  public BroadcastContentManager<?> get(String key) {
    return broadcastMap.get(key);
  }

  /**
   * Returns the whole map of broadcast content managers.
   *
   * @return The whole map of broadcast content managers.
   */
  public HashMap<String, BroadcastContentManager<?>> getMap() {
    return broadcastMap;
  }

  /**
   * Adds a broadcast content manager to the map.
   *
   * @param key  The key to add the broadcast content manager for.
   * @param broadcastContentManager The broadcast content manager to add.
   */
  public void put(String key, BroadcastContentManager<?> broadcastContentManager) {
    broadcastMap.put(key, broadcastContentManager);
  }
}
