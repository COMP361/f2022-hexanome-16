package com.hexanome16.server.models.bank;

import com.hexanome16.common.models.price.PurchaseMap;

/**
 * Player Bank for each player.
 */
public class PlayerBank extends Bank {

  /**
   * The constructor gives each player three tokens to start with.
   */
  public PlayerBank() {
    super(3);
  }

  /**
   * The constructor gives each player the given amount of tokens (used by savegame).
   *
   * @param initMap the amount of tokens to start with
   */
  public PlayerBank(PurchaseMap initMap) {
    super(initMap);
  }
}


