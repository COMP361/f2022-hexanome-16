package com.hexanome16.screens.lobby;

import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

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
        run(LobbyFactory::updateSessionList, Duration.seconds(1));
    }

    public static void exitLobby() {
        getGameWorld().getEntitiesByType(LobbyFactory.TYPE.BACKGROUND).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(LobbyFactory.TYPE.OWN_SESSION_LIST).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(LobbyFactory.TYPE.OTHER_SESSION_LIST).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(LobbyFactory.TYPE.CREATE_SESSION_BUTTON).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(LobbyFactory.TYPE.CLOSE_BUTTON).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(LobbyFactory.TYPE.PREFERENCES_BUTTON).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(LobbyFactory.TYPE.LOGO).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(LobbyFactory.TYPE.OWN_HEADER).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(LobbyFactory.TYPE.OTHER_HEADER).forEach(Entity::removeFromWorld);
    }
}
