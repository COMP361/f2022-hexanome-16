package com.hexanome16.client.screens.startup;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;

/**
 * The Startup Screen is the first screen the user sees
 * after launching the MainApp.
 */
public class StartupScreen {
  /**
   * Clears the screen of relevant entity types and go back
   * to the main screen from any screen.
   */
  public static void backToMainScreen() {
    // TODO: find a way of clearing the screen when coming back from any screen
    getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.LOGIN));
    getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.STARTUP));
    // TODO: Vbox all components of the startup screens
    spawn("mainscreen", 0, 0);
    spawn("diamond", 730, 360);
    spawn("message", 370, 985);
  }

}
