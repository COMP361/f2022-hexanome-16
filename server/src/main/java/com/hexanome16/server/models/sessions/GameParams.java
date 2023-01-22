package com.hexanome16.server.models.sessions;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class contains information about session game parameters in Lobby Service.
 */
@Getter
@Component
public class GameParams {
  private String location;
  private int maxSessionPlayers;
  private int minSessionPlayers;
  private String name;
  private String displayName;
  private String webSupport;

  @Value("${server.protocol}")
  private String protocol;
  @Value("${server.host}")
  private String host;
  @Value("${server.port}")
  private Integer port;

  /**
   * Default params used for testing/UI demo.
   *
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
