package com.hexanome16.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * For converting json file to level card objects.
 * Only used for creating cards at the start of the game.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CardJson extends BonusJson {
  private String level;
  private int prestigePoint;

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return super.isEmpty() && (level == null || level.isBlank());
  }
}