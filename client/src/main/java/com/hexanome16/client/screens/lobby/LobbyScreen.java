package com.hexanome16.client.screens.lobby;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import com.almasb.fxgl.entity.Entity;
import com.hexanome16.client.requests.lobbyservice.gameservice.CreateGameServiceRequest;
import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.client.types.auth.TokensInfo;
import javafx.util.Duration;

/**
 * This class controls the entities of the lobby screen.
 */
public class LobbyScreen {
  /**
   * Spawns all related elements onto the lobby screen.
   */
  public static void initLobby() {
    spawn("background");
    spawn("ownSessionList");
    spawn("otherSessionList");
    spawn("createSessionButton");
    spawn("closeButton");
    spawn("preferencesButton");
    spawn("logo");
    spawn("ownHeader");
    spawn("otherHeader");
    runOnce(() -> {
      TokensInfo tokensInfo = TokenRequest.execute("xox", "laaPhie*aiN0", null);
      assert tokensInfo != null;
      CreateGameServiceRequest.execute(tokensInfo.access_token());
    }, Duration.ZERO);
    run(LobbyFactory::updateSessionList, Duration.seconds(1));
  }

  /**
   * Removes all related elements from the lobby screen.
   */
  public static void exitLobby() {
    getGameWorld().getEntitiesByType(LobbyFactory.Type.BACKGROUND).forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.Type.OWN_SESSION_LIST)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.Type.OTHER_SESSION_LIST)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.Type.CREATE_SESSION_BUTTON)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.Type.CLOSE_BUTTON)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.Type.PREFERENCES_BUTTON)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.Type.LOGO).forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.Type.OWN_HEADER).forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.Type.OTHER_HEADER)
        .forEach(Entity::removeFromWorld);
  }
}
