package com.hexanome16.server.controllers.lobbyservice.gameservice;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * This listener is used to create and delete Game Services when the server starts and stops.
 */
@WebListener
public class StartupShutdownListener implements ServletContextListener {
  private final GameServiceController gameServiceController;

  /**
   * Constructor.
   *
   * @param gameServiceController The GameServiceController.
   */
  public StartupShutdownListener(@Autowired GameServiceController gameServiceController) {
    this.gameServiceController = gameServiceController;
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContextListener.super.contextInitialized(sce);
    gameServiceController.createGameServices();
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContextListener.super.contextDestroyed(sce);
    gameServiceController.deleteGameServices();
  }
}
