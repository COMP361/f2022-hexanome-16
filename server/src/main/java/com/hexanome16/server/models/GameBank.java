package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Game Bank for the whole game.
 */
public class GameBank extends Bank {
  private final Hashtable<Gem, ArrayList<Token>> gameBank;

  /**
   * The game starts with the default amount of tokens.
   */
  public GameBank() {
    gameBank = initBank(7);
  }

  public Hashtable<Gem, ArrayList<Token>>  getGameBank() {
    return this.gameBank;
  }

}
