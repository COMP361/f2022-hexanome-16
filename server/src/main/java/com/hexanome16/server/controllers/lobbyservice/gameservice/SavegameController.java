package com.hexanome16.server.controllers.lobbyservice.gameservice;

import com.hexanome16.common.models.auth.TokensInfo;
import com.hexanome16.common.models.sessions.SaveGameJson;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.savegame.SaveGame;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.SavegameServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import com.hexanome16.server.util.UrlUtils;
import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import javax.annotation.PreDestroy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * This controller provides methods for working with savegames in LS.
 */
@RestController
public class SavegameController {
  private final GameManagerServiceInterface gameManagerService;
  private final SavegameServiceInterface savegameService;

  /**
   * Constructor.
   *
   * @param gameManagerService  The GameManagerService.
   * @param savegameService     The SavegameService.
   */
  public SavegameController(@Autowired GameManagerServiceInterface gameManagerService,
                            @Autowired SavegameServiceInterface savegameService) {
    this.savegameService = savegameService;
    this.gameManagerService = gameManagerService;
  }

  /**
   * This method creates a savegame in Lobby Service.
   * This is essentially a proxy for the Lobby Service endpoint, which is unfortunately
   * required since LS expects a service token and not a user token.
   *
   * @param gamename     The name of the game server.
   * @param savegameId   The id of the savegame.
   * @param accessToken  The access token of the player.
   * @param sessionId    The session id of the game to save.
   * @param saveGameJson The savegame json.
   * @return status response
   */
  @PutMapping("/gameservices/{gamename}/savegames/{savegameId}")
  public ResponseEntity<String> createSavegame(@PathVariable String gamename,
                                               @PathVariable String savegameId,
                                               @RequestParam String accessToken,
                                               @RequestParam String sessionId,
                                               @RequestBody SaveGameJson saveGameJson) {
    System.out.println("createSavegame: " + gamename + " " + savegameId + " " + accessToken + " "
        + sessionId + " " + saveGameJson);
    return savegameService.saveGame(gameManagerService.getGame(Long.parseLong(sessionId)),
        savegameId, saveGameJson);
  }

  /**
   * This method loads all present savegames to Lobby Service.
   */
  @EventListener(ApplicationReadyEvent.class)
  @Order(15000)
  @SneakyThrows
  public void initSaveGames() {
    File[] savegameFiles = savegameService.getSavegameFiles();
    if (savegameFiles == null) {
      return;
    }
    for (File savegameFile : savegameFiles) {
      if (savegameFile.isFile()) {
        SaveGame saveGame = savegameService.loadGame(savegameFile.getName().replace(".json", ""));
        savegameService.createSavegameHelper(saveGame.getGamename(), saveGame.getId(),
            new SaveGameJson(saveGame.getId(), saveGame.getGamename(), saveGame.getUsernames()));
      }
    }
  }

  /**
   * This method deletes a savegame in Lobby Service.
   *
   * @param gamename    The name of the game server.
   * @param savegameId  The id of the savegame.
   * @return status response
   */
  @DeleteMapping("/gameservices/{gamename}/savegames/{savegameId}")
  public ResponseEntity<String> deleteSavegame(@PathVariable String gamename,
                                               @PathVariable String savegameId) {
    return savegameService.deleteSavegame(gamename, savegameId);
  }

  /**
   * This method deletes all savegames in Lobby Service associated with the provided game service.
   *
   * @param gamename The name of the game server.
   * @return status response
   */
  @DeleteMapping("/gameservices/{gamename}/savegames")
  public ResponseEntity<String> deleteAllSavegames(@PathVariable String gamename) {
    return savegameService.deleteAllSavegames(gamename);
  }
}
