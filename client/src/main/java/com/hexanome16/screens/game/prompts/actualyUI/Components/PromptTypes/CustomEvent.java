package com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes;

import javafx.event.Event;
import javafx.event.EventType;

public class CustomEvent extends Event {
  public static final EventType<CustomEvent> CLOSING = new EventType<>(Event.ANY,"CLOSING");

  public CustomEvent(EventType<? extends Event> eventType) {
    super(eventType);
  }
}
