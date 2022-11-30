package com.hexanome16.server.models.sessions;

import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class contains information about session game parameters in Lobby Service.
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
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

  /**
   * Constructor.
   *
   * @param location          The location of the game service.
   * @param maxSessionPlayers The maximum number of players in a session.
   * @param minSessionPlayers The minimum number of players in a session.
   * @param name              The name of the game service in Lobby Service.
   * @param displayName       The display name of the game service in Lobby Service.
   * @param webSupport        Whether the game service supports web.
   */
  public void init(String location, int maxSessionPlayers, int minSessionPlayers, String name,
                   String displayName, String webSupport) {
    this.location = location;
    this.maxSessionPlayers = maxSessionPlayers;
    this.minSessionPlayers = minSessionPlayers;
    this.name = name;
    this.displayName = displayName;
    this.webSupport = webSupport;
  }

}
