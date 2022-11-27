package com.hexanome16.server.models;

/**
 * Player class.
 */
public class Player {
  private final String username;
  private PlayerBank bank;

  public Player(String username) {
    this.username = username;
    this.bank = new PlayerBank();
  }

  public String getUsername() {
    return username;
  }

  public PlayerBank getBank() {
    return bank;
  }
}
