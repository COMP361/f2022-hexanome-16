package com.hexanome16.server.dto;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;

/**
 * a player class for long polling.
 */
public class PlayerJson implements BroadcastContent {
  private String username;

  public PlayerJson(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  @Override
  public boolean isEmpty() {
    return username.isEmpty();
  }
}
