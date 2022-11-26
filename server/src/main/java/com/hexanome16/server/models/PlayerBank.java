package com.hexanome16.server.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Player bank class.
 */
public class PlayerBank {
  private final Map<Gem, TokenStack> tokenStackMap = new HashMap<Gem, TokenStack>();

  public PlayerBank() {
  }

  public Map<Gem, TokenStack> getTokenStackMap() {
    return tokenStackMap;
  }

}
