package com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes;

import com.almasb.fxgl.entity.Entity;
import javafx.event.Event;
import javafx.event.EventType;

public class CustomEvent extends Event {
  public static final EventType<CustomEvent> CLOSING = new EventType<>(Event.ANY, "CLOSING");

  public static final EventType<CustomEvent> BOUGHT = new EventType<>(Event.ANY, "BOUGHT");

  public Entity e;

  public CustomEvent(EventType<? extends Event> eventType, Entity entity) {
    super(eventType);
    e = entity;
  }

  public CustomEvent(EventType<? extends Event> eventType) {
    super(eventType);
  }
}
