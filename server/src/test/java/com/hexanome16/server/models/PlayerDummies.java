package com.hexanome16.server.models;

/**
 * Dummy players for tests.
 */
public class PlayerDummies {
  private static final ServerPlayer tristan = new ServerPlayer("tristan", "#FFFFFF", 0);
  private static final ServerPlayer elea = new ServerPlayer("elea", "#FFFFFF", 1);
  /**
   * Returns cloned copy of an array containing two valid dummy players.
   */
  public static final ServerPlayer[] validDummies = new ServerPlayer[] {tristan, elea}.clone();
}
