package com.hexanome16.server.models;

import java.util.List;

public class Game {
  private List<Deck> decks;

  public Game(List<Deck> decks) {
    this.decks = decks;
  }
}
