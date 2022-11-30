package com.hexanome16.server;

import java.io.File;
import org.testcontainers.containers.DockerComposeContainer;

public class LobbyServiceContainer extends DockerComposeContainer<LobbyServiceContainer> {
  private static LobbyServiceContainer container;

  private LobbyServiceContainer() {
    super(new File("src/test/resources/ls-compose.yml"));
  }

  public static LobbyServiceContainer getInstance() {
    if (container == null) {
      container = new LobbyServiceContainer();
    }
    return container;
  }

  @Override
  public void start() {
    super.start();
    System.setProperty("ls.url", container.getServiceHost("lobby-service", 4242));
    System.setProperty("ls.port",
        String.valueOf(container.getServicePort("lobby-service", 4242)));
  }

  @Override
  public void stop() {
    //do nothing, JVM handles shut down
  }
}
