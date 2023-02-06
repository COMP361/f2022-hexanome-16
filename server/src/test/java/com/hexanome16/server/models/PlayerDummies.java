package com.hexanome16.server.models;

/**
 * Dummy players for tests.
 */
public class PlayerDummies {
  private static final Player tristan = new Player("tristan", "#FFFFFF");
  private static final Player imad = new Player("imad", "#FFFFFF");

  /**
   * Returns cloned copy of an array containing two dummy players.
   */
  public static final Player[] dummies = new Player[] {tristan, imad}.clone();
}
