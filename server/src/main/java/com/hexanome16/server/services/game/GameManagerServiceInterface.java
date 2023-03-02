package com.hexanome16.server.services.game;

import com.hexanome16.server.models.Game;
import dto.SessionJson;

/**
 * Interface for managing games.
 */
public interface GameManagerServiceInterface {

  /**
   * Get game associated with sessionId.
   *
   * @param sessionId id of game session
   * @return game or null if no game associated with sessionId
   */
  Game getGame(long sessionId);

  /**
   * Create a new game as client requested.
   *
   * @param sessionId sessionId
   * @param payload   the payload
   * @return error if present
   */
  String createGame(long sessionId, SessionJson payload);

  /**
   * Deletes a game from the game server.
   *
   * @param sessionId the id of the game to delete
   */
  void deleteGame(long sessionId);
}
