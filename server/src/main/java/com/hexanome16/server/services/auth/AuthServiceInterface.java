package com.hexanome16.server.services.auth;

import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.auth.TokensInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Interface for authentication service associated with our game server.
 */
public interface AuthServiceInterface {
  /**
   * Login response entity.
   *
   * @param username the username
   * @param password the password
   * @return the response entity
   */
  @ResponseBody
  ResponseEntity<TokensInfo> login(String username, String password);

  /**
   * Login response entity.
   *
   * @param refreshToken the refresh token
   * @return the response entity
   */
  @ResponseBody
  ResponseEntity<TokensInfo> login(String refreshToken);

  /**
   * Sends a request to Lobby Service to get the username associated with the passed access token.
   *
   * @param accessToken The access token of the user.
   * @return The username associated with the passed access token, null if not found.
   */
  @ResponseBody
  ResponseEntity<String> getPlayer(String accessToken);

  /**
   * This request logs out the user in LS associated with the access token.
   *
   * @param accessToken The access token.
   * @return the response entity
   */
  ResponseEntity<Void> logout(String accessToken);

  /**
   * Verify player by their access token.
   *
   * @param accessToken access token of request
   * @param game        game to verify
   * @return true if access token is in game with session ID, false otherwise or if game is null
   */
  boolean verifyPlayer(String accessToken, @NonNull Game game);
}
