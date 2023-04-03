package com.hexanome16.server.services.game;

import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.common.models.auth.TokensInfo;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.savegame.SaveGame;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.UrlUtils;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service to manage all games.
 */
@Service
public class GameManagerService implements GameManagerServiceInterface {
  private final Map<Long, Game> gameMap = new HashMap<>();
  private final SavegameServiceInterface savegameService;
  private final RestTemplate restTemplate;
  private final UrlUtils urlUtils;
  private final AuthServiceInterface authService;

  @Value("${gs.username}")
  private String gsUsername;
  @Value("${gs.password}")
  private String gsPassword;

  /**
   * Constructor.
   *
   * @param savegameService the savegame service
   * @param restTemplateBuilder the rest template builder
   * @param urlUtils the url utils
   * @param authService the auth service
   */
  public GameManagerService(@Autowired SavegameServiceInterface savegameService,
                            @Autowired RestTemplateBuilder restTemplateBuilder,
                            @Autowired UrlUtils urlUtils,
                            @Autowired AuthServiceInterface authService) {
    this.savegameService = savegameService;
    this.restTemplate = restTemplateBuilder.build();
    this.urlUtils = urlUtils;
    this.authService = authService;
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
        game = Game.create(sessionId, savegameService.loadGame(payload.getSavegame()), payload);
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
    ResponseEntity<TokensInfo> tokensInfo = authService.login(gsUsername, gsPassword);
    URI uri = urlUtils.createLobbyServiceUri("/api/sessions/" + sessionId,
        "access_token=" + Objects.requireNonNull(tokensInfo.getBody()).getAccessToken());
    restTemplate.delete(uri);
    gameMap.remove(sessionId);
  }
}
