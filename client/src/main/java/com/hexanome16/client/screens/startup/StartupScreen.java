package com.hexanome16.client.screens.startup;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;


public class StartupScreen {
  public static void backToMainScreen() {
    // TODO: remove all relevant entities from other screens, or best, find a way of clearing the screen
    getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.LOGIN));
    getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.STARTUP));
    spawn("mainscreen", 0, 0);
    spawn("diamond", 730, 360);
    spawn("message", 370, 985);
  }

}
