package com.hexanome16.client.screens.game.prompts.components.events;

import com.almasb.fxgl.entity.Entity;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * A class of some Custom Events used mainly for prompts and game.
 */
public class SplendorEvents extends Event {
  /**
   * The constant event CLOSING.
   */
  public static final EventType<SplendorEvents> CLOSING = new EventType<>(Event.ANY, "CLOSING");

  /**
   * The constant event BOUGHT.
   */
  public static final EventType<SplendorEvents> BOUGHT = new EventType<>(Event.ANY, "BOUGHT");

  /**
   * The Event entity.
   */
  public Entity eventEntity;

  /**
   * Instantiates a new Splendor events.
   *
   * @param eventType the event type
   * @param entity    the entity
   */
  public SplendorEvents(EventType<? extends Event> eventType, Entity entity) {
    super(eventType);
    eventEntity = entity;
  }

  /**
   * Instantiates a new Splendor events.
   *
   * @param eventType the event type
   */
  public SplendorEvents(EventType<? extends Event> eventType) {
    super(eventType);
  }
}
