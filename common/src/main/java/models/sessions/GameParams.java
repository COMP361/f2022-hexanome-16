package models.sessions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class contains information about session game parameters in Lobby Service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameParams {
  protected String location;
  protected int maxSessionPlayers;
  protected int minSessionPlayers;
  protected String name;
  protected String displayName;
  protected String webSupport;
}
