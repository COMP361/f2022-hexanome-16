package com.hexanome16.client.screens.startup;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

import com.almasb.fxgl.entity.Entity;
import java.util.ArrayList;

/**
 * The Startup Screen is the first screen the user sees
 * after launching the MainApp.
 */
public class StartupScreen {
  /**
   * Clears the screen of relevant entity types and go back
   * to the main screen from any screen.
   */
  public static void backToStartupScreen() {
    // Clears all entities when coming back from any screen
    ArrayList<Entity> allEntities = getGameWorld().getEntities();
    for (int i = 0; i < allEntities.size(); i++) {
      getGameWorld().removeEntity(allEntities.get(i));
    }

    // TODO: Vbox all components of the startup screens
    spawn("mainScreen", 0, 0);
    spawn("diamond", 730, 360);
    spawn("message", 370, 985);
  }

}
