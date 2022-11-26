package com.hexanome16.server.models;

/**
 * Player class.
 */
public class Player {
  private final String username;
  private PlayerBank bank;
  public PlayerBank getBank() { return bank; }
  public void setBank(PlayerBank bank) { this.bank = bank; }

  public Player(String username) {
    this.username = username;
  }
  public String getUsername() {
    return username;
  }

}
