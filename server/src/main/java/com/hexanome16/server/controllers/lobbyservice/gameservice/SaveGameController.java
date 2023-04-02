package com.hexanome16.server.controllers.lobbyservice.gameservice;

import com.hexanome16.common.models.sessions.SaveGameJson;
import com.hexanome16.server.models.savegame.SaveGame;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.SavegameServiceInterface;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides methods for working with savegames in LS.
 */
@RestController
public class SaveGameController {
  private final GameManagerServiceInterface gameManagerService;
  private final SavegameServiceInterface saveGameService;

  /**
   * Constructor.
   *
   * @param gameManagerService  The GameManagerService.
   * @param saveGameService     The SavegameService.
   */
  public SaveGameController(@Autowired GameManagerServiceInterface gameManagerService,
                            @Autowired SavegameServiceInterface saveGameService) {
    this.saveGameService = saveGameService;
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
    return saveGameService.saveGame(gameManagerService.getGame(Long.parseLong(sessionId)),
        savegameId, saveGameJson);
  }

  /**
   * This method loads all present savegames to Lobby Service.
   */
  @EventListener(ApplicationReadyEvent.class)
  @Order(15000)
  @SneakyThrows
  public void initSaveGames() {
    DirectoryStream<Path> savegameFiles = saveGameService.getSavegameFiles();
    if (savegameFiles == null) {
      return;
    }
    for (Path savegameFile : savegameFiles) {
      if (savegameFile.toFile().canRead()) {
        SaveGame saveGame = saveGameService.loadGame(
            savegameFile.toFile().getName().replace(".json", ""));
        saveGameService.createSavegameHelper(saveGame.getGamename(), saveGame.getId(),
            new SaveGameJson(saveGame.getId(), saveGame.getGamename(), saveGame.getUsernames()));
      }
    }
    savegameFiles.close();
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
    return saveGameService.deleteSavegame(gamename, savegameId);
  }

  /**
   * This method deletes all savegames in Lobby Service associated with the provided game service.
   *
   * @param gamename The name of the game server.
   * @return status response
   */
  @DeleteMapping("/gameservices/{gamename}/savegames")
  public ResponseEntity<String> deleteAllSavegames(@PathVariable String gamename) {
    return saveGameService.deleteAllSavegames(gamename);
  }
}
