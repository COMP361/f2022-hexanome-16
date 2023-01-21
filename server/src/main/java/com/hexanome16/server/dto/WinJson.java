package com.hexanome16.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the response with winners of the game.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinJson {
  private String[] winners;
}
