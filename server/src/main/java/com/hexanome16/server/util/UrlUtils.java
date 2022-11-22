package com.hexanome16.server.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;

/**
 * Utility class for performing operations on URLs.
 */
public class UrlUtils {
  @Value("${ls.protocol}")
  private static String lsProtocol;

  @Value("${ls.host}")
  private static String lsHost;

  @Value("${ls.port}")
  private static String lsPort;

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
        .replaceAll(" ", "%20")
        .replaceAll("%25", "%")
        .replaceAll("%26", "&")
        .replaceAll("%3D", "=");
  }

  /**
   * Creates a Lobby Service URI based on the passed parameters.
   *
   * @param path  The path to use for the URI.
   * @param query The query parameters to use for the URI.
   * @return The Lobby Service URI.
   */
  public static String createLobbyServiceUri(String path, String query) {
    return lsProtocol + "://" + lsHost + ":" + lsPort + path + "?" + query;
  }
}
