package com.hexanome16.server.services.game;

import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.savegame.SaveGame;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to manage all games.
 */
@Service
public class GameManagerService implements GameManagerServiceInterface {
  private final Map<Long, Game> gameMap = new HashMap<>();
  private final SavegameServiceInterface savegameService;

  /**
   * Constructor.
   *
   * @param savegameService the savegame service
   */
  public GameManagerService(@Autowired SavegameServiceInterface savegameService) {
    this.savegameService = savegameService;
  }

  @Override
  public Game getGame(long sessionId) {
    return gameMap.get(sessionId);
  }

  @Override
  public String createGame(long sessionId, SessionJson payload) {
    try {
      Game game;
      if (payload.getSavegame() == null || payload.getSavegame().isBlank()) {
        game = Game.create(sessionId, payload);
      } else {
        game = Game.create(sessionId, savegameService.loadGame(payload.getSavegame()));
      }
      gameMap.put(sessionId, game);
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
