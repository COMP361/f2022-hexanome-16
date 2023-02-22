package com.hexanome16.server.models.sessions;

import javax.annotation.PostConstruct;
import models.sessions.GameParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class contains information about session game parameters in Lobby Service
 * with server-specific information added.
 */
@Component
public class ServerGameParams extends GameParams {
  @Value("${server.protocol}")
  private String protocol;
  @Value("${server.host}")
  private String host;
  @Value("${server.port}")
  private Integer port;

  /**
   * Initialize the game params with server-specific information.
   */
  @PostConstruct
  public void init() {
    location = protocol + "://" + host + ":" + port;
    maxSessionPlayers = 4;
    minSessionPlayers = 2;
    name = "Splendor";
    displayName = "Splendor";
    webSupport = "true";
  }
}
