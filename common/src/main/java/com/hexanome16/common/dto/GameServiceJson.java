package com.hexanome16.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class contains information about a game service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameServiceJson {
  private String name;
  private String displayName;
}
