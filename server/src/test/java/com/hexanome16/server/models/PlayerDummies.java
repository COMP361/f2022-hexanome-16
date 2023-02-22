package com.hexanome16.server.models;

/**
 * Dummy players for tests.
 */
public class PlayerDummies {
  private static final Player tristan = new Player("tristan", "#FFFFFF");
  private static final Player elea = new Player("elea", "#FFFFFF");
  /**
   * Returns cloned copy of an array containing two valid dummy players.
   */
  public static final Player[] validDummies = new Player[] {tristan, elea}.clone();
}
