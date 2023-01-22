package com.hexanome16.server.dto;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the response with winners of the game.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinJson implements BroadcastContent {
  private String[] winners;

  @Override
  public boolean isEmpty() {
    return winners.length == 0;
  }
}
