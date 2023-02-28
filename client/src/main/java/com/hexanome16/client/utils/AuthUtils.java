package com.hexanome16.client.utils;

import models.auth.TokensInfo;
import models.sessions.User;

/**
 * This class provides methods to create authorization headers for requests.
 */
public class AuthUtils {
  private static TokensInfo auth;
  private static User player;

  private AuthUtils() {
    super();
  }

  /**
   * Gets the player authentication information.
   *
   * @return The player authentication information.
   */
  public static TokensInfo getAuth() {
    return auth;
  }

  /**
   * Sets the player authentication information.
   *
   * @param auth The player authentication information.
   */
  public static void setAuth(TokensInfo auth) {
    AuthUtils.auth = auth;
  }

  /**
   * Gets the player information.
   *
   * @return The player information.
   */
  public static User getPlayer() {
    return player;
  }

  /**
   * Sets the player information.
   *
   * @param player The player information.
   */
  public static void setPlayer(User player) {
    AuthUtils.player = player;
  }
}
