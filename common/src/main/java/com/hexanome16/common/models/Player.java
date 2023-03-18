package com.hexanome16.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This class contains information about a player in Game Service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Player implements BroadcastContent {
  private String name;
  private String preferredColour;
  private int playerOrder;

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return name == null || name.isBlank();
  }
}
