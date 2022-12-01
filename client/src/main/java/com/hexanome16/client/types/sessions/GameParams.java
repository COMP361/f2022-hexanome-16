package com.hexanome16.client.types.sessions;

import java.util.Objects;

/**
 * This class contains information about session game parameters in Lobby Service.
 */
public final class GameParams {
  private String location;
  private int maxSessionPlayers;
  private int minSessionPlayers;
  private String name;
  private String displayName;
  private String webSupport;

  /**
   * Default params used for testing/UI demo.
   */
  public GameParams() {
    location = "http://127.0.0.1:4243/SplendorService";
    name = "Splendor";
    maxSessionPlayers = 4;
    minSessionPlayers = 2;
    displayName = "Splendor";
    webSupport = "true";
  }

  /**
   * Constructor.
   *
   * @param location          The location of the game service.
   * @param maxSessionPlayers The maximum number of players in a session.
   * @param minSessionPlayers The minimum number of players in a session.
   * @param name              The name of the game service in Lobby Service.
   * @param displayName       The display name of the game service in Lobby Service.
   * @param webSupport        Whether the game service supports web.
   */
  public GameParams(String location, int maxSessionPlayers, int minSessionPlayers, String name,
                    String displayName,
                    String webSupport) {
    this.location = location;
    this.maxSessionPlayers = maxSessionPlayers;
    this.minSessionPlayers = minSessionPlayers;
    this.name = name;
    this.displayName = displayName;
    this.webSupport = webSupport;
  }

  @Override
  public String toString() {
    return "GameParams{"
        + "location='" + location + '\''
        + ", maxSessionPlayers=" + maxSessionPlayers
        + ", minSessionPlayers=" + minSessionPlayers
        + ", name='" + name + '\''
        + ", displayName='" + displayName + '\''
        + ", webSupport='" + webSupport + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameParams that = (GameParams) o;
    return getMaxSessionPlayers() == that.getMaxSessionPlayers()
        && getMinSessionPlayers() == that.getMinSessionPlayers()
        && getLocation().equals(that.getLocation())
        && getName().equals(that.getName())
        && getDisplayName().equals(that.getDisplayName())
        && getWebSupport().equals(that.getWebSupport());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getLocation(), getMaxSessionPlayers(), getMinSessionPlayers(), getName(),
        getDisplayName(), getWebSupport());
  }

  /**
   * Gets game server location.
   *
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * Sets location of the game server.
   *
   * @param location the location
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Gets max number of players in session.
   *
   * @return the max session players
   */
  public int getMaxSessionPlayers() {
    return maxSessionPlayers;
  }

  /**
   * Sets max players per session.
   *
   * @param maxSessionPlayers the max session players
   */
  public void setMaxSessionPlayers(int maxSessionPlayers) {
    this.maxSessionPlayers = maxSessionPlayers;
  }

  /**
   * Gets min number of players in session.
   *
   * @return the min session players
   */
  public int getMinSessionPlayers() {
    return minSessionPlayers;
  }

  /**
   * Sets min players per session.
   *
   * @param minSessionPlayers the min session players
   */
  public void setMinSessionPlayers(int minSessionPlayers) {
    this.minSessionPlayers = minSessionPlayers;
  }

  /**
   * Gets name of the game server.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name of the game server.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets display name of the game server.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets display name of the game server.
   *
   * @param displayName the display name
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Gets web support of the game server.
   *
   * @return the web support
   */
  public String getWebSupport() {
    return webSupport;
  }

  /**
   * Sets web support of the game server.
   *
   * @param webSupport the web support
   */
  public void setWebSupport(String webSupport) {
    this.webSupport = webSupport;
  }
}
