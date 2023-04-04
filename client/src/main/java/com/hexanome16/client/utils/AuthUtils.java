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
import javafx.util.Duration;

/**
 * This class provides methods to create authorization headers for requests.
 */
public class AuthUtils {
  private static final AtomicReference<TokensInfo> auth = new AtomicReference<>();
  private static final AtomicReference<User> player = new AtomicReference<>();
  private static final AtomicReference<ScheduledService<Void>> refreshService
      = new AtomicReference<>(createRefreshService());

  private AuthUtils() {
    super();
  }

  private static ScheduledService<Void> createRefreshService() {
    ScheduledService<Void> service = new ScheduledService<>() {
      @Override
      protected Task<Void> createTask() {
        return new Task<>() {
          @Override
          protected Void call() throws Exception {
            TokenRequest.execute(auth.get().getRefreshToken());
            return null;
          }
        };
      }
    };
    service.setRestartOnFailure(false);
    return service;
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
    AuthUtils.auth.set(auth);
    if (auth != null) {
      refreshService.get().setDelay(Duration.seconds(Math.max(0, auth.getExpiresIn() - 30)));
      refreshService.get().start();
    } else {
      refreshService.get().cancel();
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
