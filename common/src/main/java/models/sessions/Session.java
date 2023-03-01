package models.sessions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class contains information about a session in Lobby Service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true)
public final class Session {
  private String creator;
  private GameParams gameParameters;
  private boolean launched;
  private String[] players;
  @JsonProperty("savegameid")
  private String saveGameId;
  private Map<String, String> playerLocations;
}
