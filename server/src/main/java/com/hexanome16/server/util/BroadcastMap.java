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

  public BroadcastMap() {
    broadcastMap = new HashMap<>();
  }

  public BroadcastContentManager<?> get(String key) {
    return broadcastMap.get(key);
  }

  public HashMap<String, BroadcastContentManager<?>> getMap() {
    return broadcastMap;
  }

  public void put(String key, BroadcastContentManager<?> broadcastContentManager) {
    broadcastMap.put(key, broadcastContentManager);
  }
}
