package models.sessions;

import lombok.Data;
import lombok.Setter;

/**
 * This class contains information about a user in Lobby Service.
 */
@Data
public final class User {
  private String name;
  private String password;
  private String role;
  @Setter
  private String preferredColour;
}
