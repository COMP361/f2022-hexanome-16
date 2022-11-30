package com.hexanome16.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * For converting json file to noble objects.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NobleJson {
  private int prestigePoint;

  private int onyxAmount;

  private int sapphireAmount;

  private int emeraldAmount;

  private int rubyAmount;

  private int diamondAmount;
}
