package com.hexanome16.server.services.game;

import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.Game;

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
}
