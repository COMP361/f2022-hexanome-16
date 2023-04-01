package com.hexanome16.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexanome16.common.models.price.PurchaseMap;
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
  private boolean isCurrent;
  private int prestigePoints;
  private int playerOrder;

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return username.isEmpty();
  }
}
