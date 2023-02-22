package dto;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the response with winners of the game.
 */
@Data
public class WinJson implements BroadcastContent {
  private String[] winners;

  /**
   * Constructor.
   *
   * @param winners the winners
   */
  public WinJson(String[] winners) {
    this.winners = winners;
  }

  @Override
  public boolean isEmpty() {
    return winners == null || winners.length == 0;
  }
}
