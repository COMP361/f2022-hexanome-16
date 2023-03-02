package dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the response with winners of the game.
 */
@Data
@NoArgsConstructor
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

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return winners == null || winners.length == 0;
  }
}
