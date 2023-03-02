package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class contains information about a player in Game Service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player implements BroadcastContent {
  private String name;
  private String preferredColour;

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return name == null || name.isBlank();
  }
}
