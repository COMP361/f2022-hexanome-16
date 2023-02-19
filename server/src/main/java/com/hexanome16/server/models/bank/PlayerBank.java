package com.hexanome16.server.models.bank;

import com.hexanome16.server.models.price.Gem;
import java.util.ArrayList;
import java.util.Hashtable;

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
}


