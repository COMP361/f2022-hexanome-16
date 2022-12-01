package com.hexanome16.server.models.sessions;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public final class Session {
  private Long id;
  private String creator;
  private GameParams gameParameters;
  private boolean launched;
  private String[] players;
  @JsonProperty("savegameid")
  private String saveGameId;

  /**
   * Constructor.
   *
   * @param creator        The creator of the session.
   * @param gameParameters The game parameters of the session.
   * @param launched       Whether the session is launched.
   * @param players        The players in the session.
   * @param saveGameId     The savegame id of the session (can be empty).
   */
  public Session(String creator, GameParams gameParameters, boolean launched, String[] players,
                 String saveGameId) {
    this.creator = creator;
    this.gameParameters = gameParameters;
    this.launched = launched;
    this.players = players;
    this.saveGameId = saveGameId;
  }


}
