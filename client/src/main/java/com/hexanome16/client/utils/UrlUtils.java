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
    String urlString = LS_PROPERTIES.getString("server.protocol") + "://"
        + LS_PROPERTIES.getString("server.host") + ":"
        + LS_PROPERTIES.getInt("server.port") + path + "?" + query;
    return URI.create(urlString.replaceAll("\\+", "%2B"));
  }

  /**
   * Creates a game server URI based on the passed parameters.
   *
   * @param path  The path to use for the URI.
   * @param query The query parameters to use for the URI.
   * @return The game server URI.
   */
  public static URI createGameServerUri(String path, String query) {
    String urlString = SERVER_PROPERTIES.getString("server.protocol") + "://"
        + SERVER_PROPERTIES.getString("server.host") + ":"
        + SERVER_PROPERTIES.getInt("server.port") + path + "?" + query;
    return URI.create(urlString.replaceAll("\\+", "%2B"));
  }
}
