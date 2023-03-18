package com.hexanome16.server.models.savegame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.game.Game;

/**
 * This class is used to handle loading and creating savegames.
 */
public class SaveGame {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Loads a game from a savegame JSON file.
   *
   * @param id the id of the savegame (name of the JSON file)
   * @return the parsed game
   */
  public static Game loadGame(String id) {
    return null;
  }
}
