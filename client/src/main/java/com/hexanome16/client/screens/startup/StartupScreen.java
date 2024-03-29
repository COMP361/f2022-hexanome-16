package com.hexanome16.client.screens.startup;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

/**
 * The Startup Screen is the first screen the user sees
 * after launching the MainApp.
 */
public class StartupScreen {
  /**
   * Go back to the main screen from any screen.
   */
  public static void backToStartupScreen() {
    // TODO: Vbox all components of the startup screens
    spawn("mainScreen", 0, 0);
    spawn("diamond", 730, 360);
    spawn("message", 370, 985);
  }

  /**
   * Removes the startup screen.
   */
  public static void removeStartupScreen() {
    getGameWorld().removeEntities(getGameWorld().getEntitiesByType(
        EntityType.STARTUP, EntityType.LOGIN, EntityType.MESSAGE));
  }
}
