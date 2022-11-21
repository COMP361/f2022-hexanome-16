package com.hexanome16.client.utils;

import com.almasb.fxgl.core.asset.AssetType;
import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.dsl.FXGL;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

  /**
   * Decodes the passed UTF-8 String using an algorithm that's compatible with
   * JavaScript's <code>decodeURIComponent</code> function. Returns
   * <code>null</code> if the String is <code>null</code>.
   *
   * @param s The UTF-8 encoded String to be decoded
   * @return the decoded String
   */
  public static String decodeUriComponent(String s) {
    return s == null ? null : s.isBlank() ? "" : URLDecoder.decode(
        s.replaceAll("%", "%25"),
        StandardCharsets.UTF_8
    );
  }

  /**
   * Encodes the passed String as UTF-8 using an algorithm that's compatible
   * with JavaScript's <code>encodeURIComponent</code> function. Returns
   * <code>null</code> if the String is <code>null</code>.
   *
   * @param s The String to be encoded
   * @return the encoded String
   */
  public static String encodeUriComponent(String s) {
    return s == null ? null : s.isBlank() ? "" : URLEncoder.encode(s, StandardCharsets.UTF_8)
        .replaceAll("%25", "%")
        .replaceAll(" ", "%20")
        .replaceAll("%26", "&")
        .replaceAll("%3D", "=");
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
      return new URI(
          LS_PROPERTIES.getString("server.protocol"),
          null,
          LS_PROPERTIES.getString("server.host"),
          LS_PROPERTIES.getInt("server.port"),
          path,
          encodeUriComponent(query),
          null
      );
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
      return new URI(
          SERVER_PROPERTIES.getString("server.protocol"),
          null,
          SERVER_PROPERTIES.getString("server.host"),
          SERVER_PROPERTIES.getInt("server.port"),
          path,
          encodeUriComponent(query),
          null
      );
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
  }
}
