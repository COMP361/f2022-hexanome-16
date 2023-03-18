package com.hexanome16.server.services.game;

import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.savegame.SaveGame;

/**
 * This interface provides methods for working with savegames.
 */
public interface SavegameServiceInterface {
  /**
   * Load a savegame from a JSON with provided id.
   *
   * @param savegameId The id of the savegame.
   * @return The savegame.
   */
  SaveGame loadGame(String savegameId);

  /**
   * Save a game to a JSON with provided id.
   *
   * @param game The game to save.
   * @param id  The id of the savegame.
   */
  void saveGame(Game game, String id);

  /**
   * Delete a savegame with provided id.
   *
   * @param savegameId The id of the savegame.
   */
  void deleteSavegame(String savegameId);
}
