package com.hexanome16.client.screens.lobby;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import com.almasb.fxgl.entity.Entity;

/**
 * This class controls the entities of the lobby screen.
 */
public class LobbyScreen {

  /**
   * Spawns all related elements onto the lobby screen.
   */
  public static void initLobby() {
    LobbyFactory.shouldFetch.set(true);
    LobbyHelpers.createFetchSessionThread();
    LobbyHelpers.createFetchGameServicesThread();
    spawn("background");
    spawn("ownSessionList");
    spawn("otherSessionList");
    spawn("createSessionButton");
    spawn("closeButton");
    spawn("preferencesButton");
    spawn("logo");
    spawn("ownHeader");
    spawn("otherHeader");
    spawn("gameServiceList");
  }

  /**
   * Removes all related elements from the lobby screen.
   */
  public static void exitLobby() {
    LobbyFactory.shouldFetch.set(false);
    LobbyFactory.fetchGameServersService.get().cancel();
    LobbyFactory.fetchSessionsService.get().cancel();
    getGameWorld().getEntitiesByType(EntityType.BACKGROUND).forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(EntityType.OWN_SESSION_LIST)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(EntityType.OTHER_SESSION_LIST)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(EntityType.CREATE_SESSION_BUTTON)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(EntityType.CLOSE_BUTTON)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(EntityType.PREFERENCES_BUTTON)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(EntityType.LOGO).forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(EntityType.OWN_HEADER).forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(EntityType.OTHER_HEADER)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(EntityType.GAME_SERVICE_LIST)
        .forEach(Entity::removeFromWorld);
  }
}
