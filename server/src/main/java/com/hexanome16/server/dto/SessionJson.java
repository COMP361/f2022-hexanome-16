package com.hexanome16.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.util.deserializers.WinConditionDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the response from Lobby Service when a session is launched.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionJson {
  private Player[] players;
  private String creator;
  private String savegame;
  @JsonProperty("gameServer")
  @JsonDeserialize(using = WinConditionDeserializer.class)
  private WinCondition winCondition;
}
