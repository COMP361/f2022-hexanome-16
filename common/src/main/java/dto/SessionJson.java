package dto;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Player;

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
  private String gameServer;

  @Override
  public boolean isEmpty() {
    return players == null || players.length == 0 || creator == null || creator.isBlank()
        || gameServer == null || gameServer.isBlank();
  }
}
