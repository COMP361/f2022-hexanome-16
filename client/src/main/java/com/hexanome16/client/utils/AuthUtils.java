package com.hexanome16.client.utils;

import com.hexanome16.client.types.auth.TokensInfo;
import com.hexanome16.client.types.user.User;
import java.util.Base64;

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
   * Creates a Bearer authorization header for a request.
   *
   * @param accessToken The access token of the user.
   * @return The Bearer authorization header.
   */
  public static String getBearerHeader(String accessToken) {
    return "Bearer " + accessToken;
  }

  /**
   * Creates a basic HTTP authorization header for a request.
   *
   * @param username The username of the user.
   * @param password The password of the user.
   * @return The Basic authorization header.
   */
  public static String getBasicHeader(String username, String password) {
    return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
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
   * Gets the player authentication information.
   *
   * @return The player authentication information.
   */
  public static TokensInfo getAuth() {
    return auth;
  }

  /**
   * Sets the player information.
   *
   * @param player The player information.
   */
  public static void setPlayer(User player) {
    AuthUtils.player = player;
  }

  /**
   * Gets the player information.
   *
   * @return The player information.
   */
  public static User getPlayer() {
    return player;
  }
}