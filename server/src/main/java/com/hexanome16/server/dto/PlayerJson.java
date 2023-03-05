package com.hexanome16.server.dto;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * a player class for long polling.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerJson implements BroadcastContent {
  private String username;

  @Override
  public boolean isEmpty() {
    return username.isEmpty();
  }
}