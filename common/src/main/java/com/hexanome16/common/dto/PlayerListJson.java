package com.hexanome16.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A player list class for long polling.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerListJson implements BroadcastContent {
  private PlayerJson[] players;

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return players == null || players.length == 0;
  }
}
