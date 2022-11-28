package com.hexanome16.server.models;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Player class.
 */
public class Player {
  private final String name;
  private final String preferredColour;
  private Inventory inventory; // the player has an inventory, not a bank
  public Inventory getInventory() { return this.inventory; }
  public void setInventory(Inventory inventory) { this.inventory = inventory; }

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
