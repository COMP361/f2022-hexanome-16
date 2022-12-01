package com.hexanome16.server.models.sessions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class contains information about a session in Lobby Service.
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public final class Session {
  private Long id;
  private String creator;
  private GameParams gameParameters;
  private boolean launched;
  private String[] players;
  @JsonProperty("savegameid")
  private String saveGameId;
}
