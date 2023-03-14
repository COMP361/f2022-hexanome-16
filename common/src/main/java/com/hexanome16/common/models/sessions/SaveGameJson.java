package com.hexanome16.common.models.sessions;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class contains the information about a savegame.
 */
@Data
@NoArgsConstructor
public class SaveGameJson {
  private String savegameid;
  private String gamename;
  private String[] players;
}
