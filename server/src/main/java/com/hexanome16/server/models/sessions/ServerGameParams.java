package com.hexanome16.server.models.sessions;

import java.util.Properties;
import lombok.SneakyThrows;
import models.sessions.GameParams;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * This class contains information about session game parameters in Lobby Service
 * with server-specific information added.
 */
public class ServerGameParams extends GameParams {

  /**
   * Constructor.
   *
   * @param location          game server location
   * @param maxSessionPlayers max number of players in session
   * @param minSessionPlayers min number of players in session
   * @param name              game name
   * @param displayName       game display name
   * @param webSupport        web support
   */
  public ServerGameParams(String location, int maxSessionPlayers, int minSessionPlayers,
                          String name,
                          String displayName, String webSupport) {
    super(location, maxSessionPlayers, minSessionPlayers, name, displayName, webSupport);
  }

  /**
   * Constructor without location (used for properties initialization).
   *
   * @param maxSessionPlayers max number of players in session
   * @param minSessionPlayers min number of players in session
   * @param name              game name
   * @param displayName       game display name
   * @param webSupport        web support
   */
  @SneakyThrows
  public ServerGameParams(int maxSessionPlayers, int minSessionPlayers,
                          String name,
                          String displayName, String webSupport) {
    super("", maxSessionPlayers, minSessionPlayers, name, displayName, webSupport);
    Resource resource = new ClassPathResource("application.properties");
    Properties props = PropertiesLoaderUtils.loadProperties(resource);
    location = props.getProperty("server.protocol") + "://"
        + props.getProperty("server.host") + ":" + props.getProperty("server.port") + "/" + name;
  }

  /**
   * Initializes parameters used for testing.
   *
   * @return GameParams object
   */
  public static ServerGameParams testInit() {
    return new ServerGameParams("http://localhost:4243/Splendor", 4, 2, "Splendor", "Splendor", "true");
  }
}
