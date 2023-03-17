package com.hexanome16.server.services.game;

import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.savegame.SaveGame;
import java.util.Arrays;
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
      // TODO: remove this after testing
      if (Arrays.stream(game.getPlayers())
          .noneMatch(player -> player.getName().equals("tristan"))) {
        SaveGame.saveGame(game);
      }
    } catch (Exception e) {
      // Returns to lobby service,
      // not client so there's nothing we can't do anything to notify client
      e.printStackTrace();
    }
    return "success";
  }

  @Override
  public void deleteGame(long sessionId) {
    gameMap.remove(sessionId);
  }
}
