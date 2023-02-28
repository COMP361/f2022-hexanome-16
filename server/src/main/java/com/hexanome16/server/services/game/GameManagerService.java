package com.hexanome16.server.services.game;

import com.hexanome16.server.models.Game;
import dto.SessionJson;
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
      Game game = Game.create(sessionId, payload);
      gameMap.put(sessionId, game);
    } catch (Exception e) {
      // Returns to lobby service,
      // not client so there's nothing we can't do anything to notify client
      e.printStackTrace();
    }
    return "success";
  }
}
