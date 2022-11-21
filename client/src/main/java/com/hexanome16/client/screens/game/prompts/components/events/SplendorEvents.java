package com.hexanome16.client.screens.game.prompts.components.events;

import com.almasb.fxgl.entity.Entity;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * A class of some Custom Events used mainly for prompts and game.
 */
public class SplendorEvents extends Event {
  public static final EventType<SplendorEvents> CLOSING = new EventType<>(Event.ANY, "CLOSING");

  public static final EventType<SplendorEvents> BOUGHT = new EventType<>(Event.ANY, "BOUGHT");

  public Entity eventEntity;

  public SplendorEvents(EventType<? extends Event> eventType, Entity entity) {
    super(eventType);
    eventEntity = entity;
  }

  public SplendorEvents(EventType<? extends Event> eventType) {
    super(eventType);
  }
}
