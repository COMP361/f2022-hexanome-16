package com.hexanome16.client.types.sessions;

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
   * @param creator        The creator of the session.
   * @param gameParameters The game parameters of the session.
   * @param launched       Whether the session is launched.
   * @param players        The players in the session.
   * @param savegameid     The savegame id of the session (can be empty).
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
    return getLaunched() == session.getLaunched() && getCreator().equals(session.getCreator())
        && getGameParameters().equals(session.getGameParameters())
        && Arrays.equals(getPlayers(), session.getPlayers())
        && Objects.equals(getSaveGameId(), session.getSaveGameId());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(getCreator(), getGameParameters(), getLaunched(), getSaveGameId());
    result = 31 * result + Arrays.hashCode(getPlayers());
    return result;
  }

  /**
   * Gets creator of the session.
   *
   * @return the creator
   */
  public String getCreator() {
    return creator;
  }

  /**
   * Sets creator of the session.
   *
   * @param creator the creator
   */
  public void setCreator(String creator) {
    this.creator = creator;
  }

  /**
   * Gets game parameters of the session.
   *
   * @return the game parameters
   */
  public GameParams getGameParameters() {
    return gameParameters;
  }

  /**
   * Sets game parameters of the session.
   *
   * @param gameParameters the game parameters
   */
  public void setGameParameters(GameParams gameParameters) {
    this.gameParameters = gameParameters;
  }

  /**
   * Gets launch status of the session.
   *
   * @return the launched
   */
  public boolean getLaunched() {
    return launched;
  }

  /**
   * Sets launch status of the session.
   *
   * @param launched the launched
   */
  public void setLaunched(boolean launched) {
    this.launched = launched;
  }

  /**
   * Get players of the session.
   *
   * @return the string [ ]
   */
  public String[] getPlayers() {
    return players;
  }

  /**
   * Sets players of the session.
   *
   * @param players the players
   */
  public void setPlayers(String[] players) {
    this.players = players;
  }

  /**
   * Gets savegame ID of the session.
   *
   * @return the save game id
   */
  public String getSaveGameId() {
    return savegameid;
  }

  /**
   * Sets savegame ID of the session.
   *
   * @param savegameid the savegameid
   */
  public void setSaveGameId(String savegameid) {
    this.savegameid = savegameid;
  }

  /**
   * Gets ID of the session.
   *
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets ID of the session.
   *
   * @param id the id
   */
  public void setId(Long id) {
    this.id = id;
  }
}
