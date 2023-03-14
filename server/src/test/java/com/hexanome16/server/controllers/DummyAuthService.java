package com.hexanome16.server.controllers;

import com.hexanome16.common.models.auth.TokensInfo;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Dummy Auth Service for Unit testing.
 *
 * <p>
 * Accounts are hardcoded. {@link DummyAuths}
 * </p>
 */
public class DummyAuthService implements AuthServiceInterface {
  /**
   * Login response entity.
   *
   * <pre>
   * Valid username and password: {@link DummyAuths#validTokensInfos}
   * </pre>
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
          return new ResponseEntity<>(DummyAuths.validTokensInfos.get(0), HttpStatus.OK);
        }
      }
      case "elea" -> {
        if (Objects.equals(password, "1234")) {
          return new ResponseEntity<>(DummyAuths.validTokensInfos.get(1), HttpStatus.OK);
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
   * <pre>
   * Valid refreshTokens: {@link DummyAuths#validTokensInfos}
   * </pre>
   *
   * @param refreshToken the refresh token
   * @return the response entity
   */
  @Override
  public ResponseEntity<TokensInfo> login(String refreshToken) {
    switch (refreshToken) {
      case "abc" -> {
        return new ResponseEntity<>(DummyAuths.validTokensInfos.get(0), HttpStatus.OK);
      }
      case "abcd" -> {
        return new ResponseEntity<>(DummyAuths.validTokensInfos.get(1), HttpStatus.OK);
      }
      default -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
  }

  /**
   * Sends a request to Lobby Service to get the username associated with the passed access token.
   *
   * <pre>
   * Valid accessTokens: {@link DummyAuths#validTokensInfos}
   * </pre>
   *
   * @param accessToken The access token of the user.
   * @return The username associated with the passed access token.
   */
  @Override
  public ResponseEntity<String> getPlayer(String accessToken) {
    switch (accessToken) {
      case "abc" -> {
        return new ResponseEntity<>("tristan", HttpStatus.OK);
      }
      case "abcd" -> {
        return new ResponseEntity<>("elea", HttpStatus.OK);
      }
      default -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
  }

  // TODO from here

  /**
   * This request logs out the user in LS associated with the access token.
   *
   * <pre>
   * Valid accessTokens: {@link DummyAuths#validTokensInfos}
   * </pre>
   *
   * @param accessToken The access token.
   * @return the response entity
   */
  @Override
  public ResponseEntity<Void> logout(String accessToken) {
    switch (accessToken) {
      case "abc", "abcd" -> {
        return new ResponseEntity<>(HttpStatus.OK);
      }
      default -> {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
  }

  /**
   * Verify player by their access token.
   *
   * <pre>
   * Valid session IDs: {@link DummyAuths#validSessionIds}
   * Valid accessTokens: {@link DummyAuths#validTokensInfos}
   * </pre>
   *
   * @param accessToken access token of request
   * @param game        game to verify against
   * @return true if access token and id are valid, see {@link DummyAuthService}
   */
  @Override
  public boolean verifyPlayer(String accessToken, @NonNull Game game) {
    var validAccessTokens = List.of(DummyAuths.validTokensInfos.get(0).getAccessToken(),
        DummyAuths.validTokensInfos.get(1).getAccessToken());
    return validAccessTokens.contains(accessToken);
  }
}
