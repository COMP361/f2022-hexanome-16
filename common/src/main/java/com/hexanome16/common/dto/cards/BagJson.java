package com.hexanome16.common.dto.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * For converting json file to level card objects.
 * Only used for creating cards at the start of the game.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BagJson extends CardJson implements BroadcastContent {
  private int id;

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return super.isEmpty() || id == 0;
  }
}
