package com.hexanome16.server.models.sessions;

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

  public String getLocation() {
    return location;
  }

  public int getMaxSessionPlayers() {
    return maxSessionPlayers;
  }

  public int getMinSessionPlayers() {
    return minSessionPlayers;
  }

  public String getName() {
    return name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getWebSupport() {
    return webSupport;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setMaxSessionPlayers(int maxSessionPlayers) {
    this.maxSessionPlayers = maxSessionPlayers;
  }

  public void setMinSessionPlayers(int minSessionPlayers) {
    this.minSessionPlayers = minSessionPlayers;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void setWebSupport(String webSupport) {
    this.webSupport = webSupport;
  }
}
