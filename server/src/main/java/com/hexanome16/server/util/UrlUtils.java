package com.hexanome16.server.util;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for performing operations on URLs.
 */
@Component
public class UrlUtils {
  @Value("${ls.protocol}")
  private String protocol;
  @Value("${ls.host}")
  private String host;
  @Value("${ls.port}")
  private String port;

  /**
   * Creates a Lobby Service URI based on the passed parameters.
   *
   * @param path  The path to use for the URI.
   * @param query The query parameters to use for the URI.
   * @return The Lobby Service URI.
   */
  public URI createLobbyServiceUri(String path, String query) {
    String urlString = protocol + "://" + host + ":" + port + path + "?" + query;
    return URI.create(URI.create(urlString).toASCIIString().replaceAll("\\+", "%2B"));
  }
}
