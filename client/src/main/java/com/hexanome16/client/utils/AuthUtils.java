package com.hexanome16.client.utils;

import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.common.models.auth.TokensInfo;
import com.hexanome16.common.models.sessions.User;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import lombok.SneakyThrows;

/**
 * This class provides methods to create authorization headers for requests.
 */
public class AuthUtils {
  private static final Timer refreshTimer = new Timer();
  private static final AtomicReference<TokensInfo> auth = new AtomicReference<>();
  private static final AtomicReference<User> player = new AtomicReference<>();

  private AuthUtils() {
    super();
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
   * Gets the player authentication information.
   *
   * @return The player authentication information.
   */
  public static TokensInfo getAuth() {
    return auth.get();
  }

  /**
   * Sets the player authentication information.
   *
   * @param auth The player authentication information.
   */
  public static void setAuth(TokensInfo auth) {
    if (auth == null) {
      AuthUtils.auth.set(null);
      refreshTimer.cancel();
    } else {
      AuthUtils.auth.set(auth);
      refreshTimer.schedule(new TimerTask() {
        @Override
        public void run() {
          TokenRequest.execute(auth.getRefreshToken());
        }
      }, Math.max(auth.getExpiresIn() - 30, 0) * 1000L);
    }
  }

  /**
   * Gets the player information.
   *
   * @return The player information.
   */
  public static User getPlayer() {
    return player.get();
  }

  /**
   * Sets the player information.
   *
   * @param player The player information.
   */
  public static void setPlayer(User player) {
    AuthUtils.player.set(player);
  }
}
