package com.hexanome16.server.models.user;

import java.util.Objects;

/**
 * This class contains information about a user in Lobby Service.
 */
public final class User {
  private final String name;
  private final String password;
  private final String role;
  private String preferredColour;

  /**
   * Constructor.
   *
   * @param name            The name of the user.
   * @param password        The password of the user.
   * @param preferredColour The preferred colour of the user.
   * @param role            The role of the user.
   */
  public User(String name, String password, String role, String preferredColour) {
    this.name = name;
    this.password = password;
    this.role = role;
    this.preferredColour = preferredColour;
  }

  @Override
  public String toString() {
    return "User{"
        + "name='" + name + '\''
        + ", password='" + password + '\''
        + ", preferredColour='" + preferredColour + '\''
        + ", role='" + role + '\'' + '}';
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public String getRole() {
    return role;
  }

  public String getPreferredColour() {
    return preferredColour;
  }

  public void setPreferredColour(String preferredColour) {
    this.preferredColour = preferredColour;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (User) obj;
    return Objects.equals(this.name, that.name)
        && Objects.equals(this.password, that.password)
        && Objects.equals(this.preferredColour, that.preferredColour)
        && Objects.equals(this.role, that.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, password, preferredColour, role);
  }

}
