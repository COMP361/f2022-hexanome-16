package com.hexanome16.client.screens.register;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

/**
 * This class provides methods to initialize and destroy the register screen.
 */
public class RegisterScreen {
  /**
   * Initializes the register screen.
   */
  public static void initRegisterScreen() {
    spawn("regBackground");
    spawn("regClose");
    spawn("regForm", 630, 320);
    spawn("regTitle", 880, 420);
    spawn("regUserText", 830, 495);
    spawn("regUsername", 1000, 475);
    spawn("regPasswordText", 830, 545);
    spawn("regPassword", 1000, 520);
    spawn("regRoleText", 830, 600);
    spawn("regRole", 1000, 565);
    spawn("regSubmit", 930, 650);
  }

  /**
   * Destroys the register screen.
   */
  public static void exitRegisterScreen() {
    for (EntityType entityType : EntityType.values()) {
      getGameWorld().removeEntities(getGameWorld().getEntitiesByType(entityType));
    }
  }
}
