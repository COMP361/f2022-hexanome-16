package com.hexanome16.common.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hexanome16.common.models.Player;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the response from Lobby Service when a session is launched.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionJson implements BroadcastContent {
  private Player[] players;
  private String creator;
  private String savegame;
  @JsonProperty("game")
  @JsonAlias("gameServer")
  private String game;

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return players == null || players.length == 0 || creator == null || creator.isBlank()
        || game == null || game.isBlank();
  }
}
