package com.hexanome16.server.models;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Player class.
 */
public class Player {
  private final String name;
  private final String preferredColour;
  private PlayerBank bank;
  public PlayerBank getBank() { return bank; }
  public void setBank(PlayerBank bank) { this.bank = bank; }

  @JsonCreator
  public Player(String name, String preferredColour) {
    this.name = name;
    this.preferredColour = preferredColour;
  }

  public String getName() {
    return name;
  }

  public String getPreferredColour() {
    return preferredColour;

  }

}
