package com.hexanome16.server.services.game;

import com.hexanome16.common.models.sessions.SaveGameJson;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.savegame.SaveGame;
import java.io.File;
import org.springframework.http.ResponseEntity;

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
   * @param id   The id of the savegame.
   */
  ResponseEntity<String> saveGame(Game game, String id, SaveGameJson saveGameJson);

  /**
   * Delete a savegame with provided id.
   *
   * @param gamename   The game server name.
   * @param savegameId The id of the savegame.
   */
  ResponseEntity<String> deleteSavegame(String gamename, String savegameId);

  /**
   * Delete all savegames of a give game service.
   *
   * @param gamename The game server name.
   * @return The response entity.
   */
  ResponseEntity<String> deleteAllSavegames(String gamename);

  /**
   * Helper that creates a savegame in LS.
   *
   * @param gamename     The game server name.
   * @param savegameId   The savegame id.
   * @param saveGameJson The savegame json.
   * @return The response entity.
   */
  ResponseEntity<String> createSavegameHelper(String gamename, String savegameId,
                                              SaveGameJson saveGameJson);

  /**
   * Get all savegame files.
   *
   * @return The savegame files.
   */
  File[] getSavegameFiles();
}
