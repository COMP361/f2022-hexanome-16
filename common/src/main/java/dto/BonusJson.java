package dto;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * For converting json file to level card objects.
 * Only used for creating cards at the start of the game.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonusJson extends BagJson implements BroadcastContent {
  private String bonus;

  @Override
  public boolean isEmpty() {
    return super.isEmpty() && (bonus == null || bonus.isBlank());
  }
}
