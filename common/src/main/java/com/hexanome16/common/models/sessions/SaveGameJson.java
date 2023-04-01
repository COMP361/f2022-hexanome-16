package com.hexanome16.common.models.sessions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class contains the information about a savegame.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveGameJson {
  private String saveGameId;
  private String gameName;
  private String[] players;
}
