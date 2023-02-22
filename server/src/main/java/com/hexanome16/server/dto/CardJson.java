package com.hexanome16.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * For converting json file to level card objects.
 * Only used for creating cards at the start of the game.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardJson {
  private String level;
  private String bonus;

  private int id;
  private int prestigePoint;

  private int onyxAmount;

  private int sapphireAmount;

  private int emeraldAmount;

  private int rubyAmount;

  private int diamondAmount;
}