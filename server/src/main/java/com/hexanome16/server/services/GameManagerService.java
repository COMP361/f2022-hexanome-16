package com.hexanome16.server.services;

import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.Game;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Service to manage all games.
 */
@Service
public class GameManagerService implements GameManagerServiceInterface {
  private final Map<Long, Game> gameMap = new HashMap<>();

  @Override
  public Game getGame(long sessionId) {
    return gameMap.get(sessionId);
  }

  @Override
  public String createGame(long sessionId, SessionJson payload) {
    try {
      Game game = new Game(sessionId, payload);
      gameMap.put(sessionId, game);
    } catch (Exception e) {
      // TODO: make this return an error to the client (server error or something)
      //  return CustomResponseFactory.getErrorResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
      e.printStackTrace();
    }
    return "success";
  }
}
