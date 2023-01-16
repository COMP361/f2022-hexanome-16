package com.hexanome16.server.controllers;

import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Dummy Auth Service for Unit testing.
 *
 * <p>
 * Accounts are hardcoded.
 * </p>
 * <p>
 * List of accounts:
 * <p>
 * 1.
 * <pre>
 *   username: tristan
 *   password: 123
 *   accessToken: abc
 *   refreshToken: abc
 * </pre>
 * 2.
 * <pre>
 *   username: elea
 *   password: 1234
 *   accessToken: abcd
 *   refreshToken: abcd
 * </pre>
 * </p>
 */
public class DummyAuthService implements AuthServiceInterface {

  private final TokensInfo[] tokensInfos;

  /**
   * Instantiates the dummy auth service.
   */
  public DummyAuthService() {
    tokensInfos = new TokensInfo[] {
        new TokensInfo("abc", "abc", "bearer", 1800, "read write trust"),
        new TokensInfo("abcd", "abcd", "bearer", 1800, "read write trust"),
    };
  }

  /**
   * Login response entity.
   *
   * @param username the username
   * @param password the password
   * @return the response entity
   */
  @Override
  public ResponseEntity<TokensInfo> login(String username, String password) {
    switch (username) {
      case "tristan" -> {
        if (Objects.equals(password, "123")) {
          return new ResponseEntity<>(tokensInfos[0], HttpStatus.OK);
        }
      }
      case "elea" -> {
        if (Objects.equals(password, "1234")) {
          return new ResponseEntity<>(tokensInfos[1], HttpStatus.OK);
        }
      }
      default -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
    return null;
  }

  /**
   * Login response entity.
   *
   * @param refreshToken the refresh token
   * @return the response entity
   */
  @Override
  public ResponseEntity<TokensInfo> login(String refreshToken) {
    switch (refreshToken) {
      case "abc" -> {
        return new ResponseEntity<>(tokensInfos[0], HttpStatus.OK);
      }
      case "abcd" -> {
        return new ResponseEntity<>(tokensInfos[1], HttpStatus.OK);
      }
      default -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
  }

  /**
   * Sends a request to Lobby Service to get the username associated with the passed access token.
   *
   * @param accessToken The access token of the user.
   * @return The username associated with the passed access token.
   */
  @Override
  public ResponseEntity<String> getPlayer(String accessToken) {
    switch (accessToken) {
      case "abc" -> {
        return new ResponseEntity<>("tristan" ,HttpStatus.OK); }
      case "abcd" -> {
        return new ResponseEntity<>("elea" ,HttpStatus.OK);
      }
      default -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
  }

  //TODO from here

  /**
   * This request logs out the user in LS associated with the access token.
   *
   * @param accessToken The access token.
   * @return the response entity
   */
  @Override
  public ResponseEntity<Void> logout(String accessToken) {
    switch (accessToken) {
      case "abc", "abcd" -> {
        return new ResponseEntity<>(HttpStatus.OK); }
      default -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
  }

  /**
   * Verify player by their access token.
   *
   * @param sessionId   ID of session associated with request
   * @param accessToken access token of request
   * @param gameMap     mapping from ID's to their respective game
   * @return true if access token is in game with session ID, false otherwise.
   */
  @Override
  public boolean verifyPlayer(long sessionId, String accessToken, Map<Long, Game> gameMap) {
    switch (accessToken) {
      case "abc", "abcd" -> {
        return true;
      }
      default -> {
        return false;
      }
    }
  }
}
