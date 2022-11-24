package com.hexanome16.client.utils;

import com.almasb.fxgl.core.asset.AssetType;
import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.dsl.FXGL;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.concurrent.Task;

/**
 * Utility class for performing operations on URLs.
 */
public class UrlUtils {
  private static final PropertyMap LS_PROPERTIES = FXGL.getAssetLoader().load(
      AssetType.PROPERTY_MAP,
      "properties/lobby-service-" + System.getenv("PROFILE_ID") + ".properties"
  );

  private static final PropertyMap SERVER_PROPERTIES = FXGL.getAssetLoader().load(
      AssetType.PROPERTY_MAP,
      "properties/server-" + System.getenv("PROFILE_ID") + ".properties"
  );

  private UrlUtils() {
    super();
  }

  //TODO: Add ability to set custom URLs for LS/Game Server.

  /**
   * Creates a Lobby Service URI based on the passed parameters.
   *
   * @param path  The path to use for the URI.
   * @param query The query parameters to use for the URI.
   * @return The Lobby Service URI.
   */
  public static URI createLobbyServiceUri(String path, String query) {
    try {
      String urlString = LS_PROPERTIES.getString("server.protocol") + "://"
          + LS_PROPERTIES.getString("server.host") + ":"
          + LS_PROPERTIES.getInt("server.port") + path + "?" + query;
      return URI.create(new URI(urlString).toASCIIString().replaceAll("\\+", "%2B"));
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Creates a game server URI based on the passed parameters.
   *
   * @param path  The path to use for the URI.
   * @param query The query parameters to use for the URI.
   * @return The game server URI.
   */
  public static URI createGameServerUri(String path, String query) {
    try {
      String urlString = SERVER_PROPERTIES.getString("server.protocol") + "://"
          + SERVER_PROPERTIES.getString("server.host") + ":"
          + SERVER_PROPERTIES.getInt("server.port") + path + "?" + query;
      return URI.create(new URI(urlString).toASCIIString().replaceAll("\\+", "%2B"));
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static class RequestThread {
    private Thread worker;
    private Runnable task;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean finished = new AtomicBoolean(false);
    private int interval;

    public RequestThread(int interval, Runnable task) {
      this.interval = interval;
      this.task = task;
    }

    public boolean isRunning() {
      return running.get();
    }

    public boolean isFinished() {
      return finished.get();
    }

    public void setTask(Runnable task) {
      this.task = task;
    }

    public void setInterval(int interval) {
      this.interval = interval;
    }

    public void start() {
      if (running.compareAndSet(false, true)) {
        worker = new Thread(task);
        worker.start();
      }
    }

    public void stop() {
      if (running.compareAndSet(true, false)) {
        worker.interrupt();
      }
    }
  }
}
