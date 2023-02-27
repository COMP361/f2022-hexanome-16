package models;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class contains information about a player in Game Service.
 */
@Getter
@AllArgsConstructor
public class Player implements BroadcastContent {
  private final String name;
  private final String preferredColour;

  @Override
  public boolean isEmpty() {
    return name == null || name.isBlank();
  }
}
