package com.hexanome16.common.models.sessions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class contains information about a user in Lobby Service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private String name;
  private String password;
  private String role;
  private String preferredColour;
}
