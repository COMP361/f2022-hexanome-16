package com.hexanome16.client.utils;

import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.common.models.auth.TokensInfo;
import com.hexanome16.common.models.sessions.User;
import java.util.Base64;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

/**
 * This class provides methods to create authorization headers for requests.
 */
public class AuthUtils {
  private static final AtomicReference<TokensInfo> auth = new AtomicReference<>();
  private static final AtomicReference<User> player = new AtomicReference<>();
  private static final AtomicReference<Thread> refreshThread = new AtomicReference<>();

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
    if (refreshThread.get() != null) {
      refreshThread.get().interrupt();
    }
    AuthUtils.auth.set(auth);
    if (auth != null) {
      Thread thread = new Thread(() -> {
        try {
          Thread.sleep(Math.max(0, auth.getExpiresIn() - 30) * 1000L);
        } catch (InterruptedException e) {
          return;
        }
        TokenRequest.execute(auth.getRefreshToken());
      });
      thread.setDaemon(true);
      refreshThread.set(thread);
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
