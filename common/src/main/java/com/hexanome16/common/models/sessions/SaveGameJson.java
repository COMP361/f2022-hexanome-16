package com.hexanome16.common.models.sessions;

import com.fasterxml.jackson.annotation.JsonProperty;
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
  @JsonProperty("savegameid")
  private String saveGameId;
  @JsonProperty("gamename")
  private String gameName;
  private String[] players;
}
