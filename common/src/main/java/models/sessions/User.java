package models.sessions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class contains information about a user in Lobby Service.
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public final class User {
  private final String name;
  private final String password;
  private final String role;
  @Setter
  private String preferredColour;
}
