package com.hexanome16.screens.lobby;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import com.almasb.fxgl.entity.Entity;
import com.hexanome16.requests.lobbyservice.gameservice.CreateGameServiceRequest;
import com.hexanome16.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.types.lobby.auth.TokensInfo;
import javafx.util.Duration;

public class LobbyScreen {
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

  public static void exitLobby() {
    getGameWorld().getEntitiesByType(LobbyFactory.TYPE.BACKGROUND).forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.TYPE.OWN_SESSION_LIST)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.TYPE.OTHER_SESSION_LIST)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.TYPE.CREATE_SESSION_BUTTON)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.TYPE.CLOSE_BUTTON)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.TYPE.PREFERENCES_BUTTON)
        .forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.TYPE.LOGO).forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.TYPE.OWN_HEADER).forEach(Entity::removeFromWorld);
    getGameWorld().getEntitiesByType(LobbyFactory.TYPE.OTHER_HEADER)
        .forEach(Entity::removeFromWorld);
  }
}
