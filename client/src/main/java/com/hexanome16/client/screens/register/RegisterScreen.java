package com.hexanome16.client.screens.register;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class RegisterScreen {
  public static void initRegisterScreen() {
    for (EntityType entityType : EntityType.values()) {
      spawn(entityType.getEntityName());
    }
  }

  public static void exitRegisterScreen() {
    for (EntityType entityType : EntityType.values()) {
      getGameWorld().removeEntities(getGameWorld().getEntitiesByType(entityType));
    }
  }
}
