package com.hexanome16.types.lobby.sessions;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class contains information about a session in Lobby Service.
 */
public final class Session {
  private Long id;
  private String creator;
  private GameParams gameParameters;
  private boolean launched;
  private String[] players;
  private String savegameid;

  /**
   * Constructor.
   *
   * @param creator The creator of the session.
   * @param gameParameters The game parameters of the session.
   * @param launched Whether the session is launched.
   * @param players The players in the session.
   * @param savegameid The savegame id of the session (can be empty).
   */
  public Session(String creator, GameParams gameParameters, boolean launched, String[] players,
                 String savegameid) {
    this.creator = creator;
    this.gameParameters = gameParameters;
    this.launched = launched;
    this.players = players;
    this.savegameid = savegameid;
  }

  @Override
  public String toString() {
    return "Session{"
        + "creator='" + creator + '\''
        + ", gameParameters=" + gameParameters
        + ", launched=" + (launched ? "yes" : "no")
        + ", players=" + Arrays.toString(players)
        + ", savegameid='" + savegameid + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Session session = (Session) o;
    return launched() == session.launched() && creator().equals(session.creator())
        && gameParameters().equals(session.gameParameters())
        && Arrays.equals(players(), session.players())
        && Objects.equals(savegameid(), session.savegameid());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(creator(), gameParameters(), launched(), savegameid());
    result = 31 * result + Arrays.hashCode(players());
    return result;
  }

  public String creator() {
    return creator;
  }

  public GameParams gameParameters() {
    return gameParameters;
  }

  public boolean launched() {
    return launched;
  }

  public String[] players() {
    return players;
  }

  public String savegameid() {
    return savegameid;
  }

  public Long id() {
    return id;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public void setGameParameters(GameParams gameParameters) {
    this.gameParameters = gameParameters;
  }

  public void setLaunched(boolean launched) {
    this.launched = launched;
  }

  public void setPlayers(String[] players) {
    this.players = players;
  }

  public void setSavegameid(String savegameid) {
    this.savegameid = savegameid;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
