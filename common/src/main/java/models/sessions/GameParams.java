package models.sessions;

import lombok.Getter;

/**
 * This class contains information about session game parameters in Lobby Service.
 */
@Getter
public class GameParams {
  protected String location;
  protected int maxSessionPlayers;
  protected int minSessionPlayers;
  protected String name;
  protected String displayName;
  protected String webSupport;
}
