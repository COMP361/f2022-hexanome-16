package com.hexanome16.server.util;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Utility class for performing operations on URLs.
 */
@Component
@PropertySource("classpath:application.properties")
public class UrlUtils {
  private final String lsProtocol;

  private final String lsHost;

  private final String lsPort;

  /**
   * Constructor.
   *
   * @param lsProtocol The protocol to use for the Lobby Service.
   * @param lsHost     The host to use for the Lobby Service.
   * @param lsPort     The port to use for the Lobby Service.
   */
  @Autowired
  private UrlUtils(@Value("${ls.protocol}:http") String lsProtocol,
                   @Value("${ls.host}:localhost") String lsHost,
                   @Value("${ls.port}:4242") String lsPort) {
    super();
    this.lsProtocol = lsProtocol;
    this.lsHost = lsHost;
    this.lsPort = lsPort;
  }

  /**
   * Constructor.
   */
  public UrlUtils() {
    super();
    this.lsProtocol = "http";
    this.lsHost = "localhost";
    this.lsPort = "4242";
  }

  /**
   * Creates a Lobby Service URI based on the passed parameters.
   *
   * @param path  The path to use for the URI.
   * @param query The query parameters to use for the URI.
   * @return The Lobby Service URI.
   */
  public URI createLobbyServiceUri(String path, String query) {
    return UriComponentsBuilder.fromUriString(
        lsProtocol + "://" + lsHost + ":" + lsPort + path + "?" + query
    ).build().encode(StandardCharsets.UTF_8).toUri();
  }
}
