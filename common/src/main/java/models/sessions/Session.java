package models.sessions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class contains information about a session in Lobby Service.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class Session {
  private Long id;
  private String creator;
  private GameParams gameParameters;
  private boolean launched;
  private String[] players;
  @JsonProperty("savegameid")
  private String saveGameId;
}
