package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Player Bank for each player.
 */
public class PlayerBank extends Bank {
  private final Hashtable<Gem, ArrayList<Token>> playerBank;

  /**
   * The constructor gives each player three tokens to start with.
   */
  public PlayerBank() {
    playerBank = initBank(3);
  }


  @Override
  protected Hashtable<Gem, ArrayList<Token>> getBank() {
    return this.playerBank;
  }
}


