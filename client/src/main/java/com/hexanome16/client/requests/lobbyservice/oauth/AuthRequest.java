package com.hexanome16.client.requests.lobbyservice.oauth;

import com.hexanome16.client.requests.lobbyservice.user.GetUserRequest;
import com.hexanome16.client.utils.AuthUtils;

/**
 * This class combines authentication and fetching user information from Lobby Service.
 */
public class AuthRequest {
  /**
   * This method authenticates a user and fetches their information.
   *
   * @param username The username of the user.
   * @param password The password of the user.
   */
  public static void execute(String username, String password) {
    TokenRequest.execute(username, password);
    if (AuthUtils.getAuth() != null) {
      System.out.println(AuthUtils.getAuth());
      GetUserRequest.execute(username, AuthUtils.getAuth().getAccessToken());
      System.out.println(AuthUtils.getPlayer());
    }
  }
}
