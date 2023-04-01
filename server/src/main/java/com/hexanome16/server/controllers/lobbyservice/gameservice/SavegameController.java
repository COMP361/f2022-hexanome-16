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
  private final RestTemplate restTemplate;

  private final AuthServiceInterface authService;
  private final ServiceUtils serviceUtils;
  private final GameManagerServiceInterface gameManagerService;
  private final SavegameServiceInterface savegameService;
  private final UrlUtils urlUtils;

  @Value("${gs.username}")
  private String gsUsername;
  @Value("${gs.password}")
  private String gsPassword;
  @Value("${path.savegames}")
  private String savegamesPath;

  /**
   * Constructor.
   *
   * @param restTemplateBuilder The RestTemplateBuilder.
   * @param authService         The AuthService.
   * @param gameManagerService  The GameManagerService.
   * @param savegameService     The SavegameService.
   * @param serviceUtils        The ServiceUtils.
   * @param urlUtils            The UrlUtils.
   */
  public SavegameController(@Autowired RestTemplateBuilder restTemplateBuilder,
                            @Autowired AuthServiceInterface authService,
                            @Autowired GameManagerServiceInterface gameManagerService,
                            @Autowired SavegameServiceInterface savegameService,
                            @Autowired ServiceUtils serviceUtils,
                            @Autowired UrlUtils urlUtils) {
    this.restTemplate = restTemplateBuilder.build();
    this.urlUtils = urlUtils;
    this.authService = authService;
    this.serviceUtils = serviceUtils;
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
    Pair<ResponseEntity<String>, Pair<Game, ServerPlayer>> verifiedPlayer =
        serviceUtils.validRequest(Long.parseLong(sessionId), accessToken);
    if (verifiedPlayer.getLeft().getStatusCodeValue() != 200) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_ACCESS_TOKEN);
    }
    savegameService.saveGame(gameManagerService.getGame(Long.parseLong(sessionId)), savegameId);
    return createSavegameHelper(gamename, savegameId, saveGameJson);
  }

  /**
   * This method loads all present savegames to Lobby Service.
   */
  @EventListener(ApplicationReadyEvent.class)
  @Order(15000)
  @SneakyThrows
  public void initSaveGames() {
    File[] savegameFiles = new File(savegamesPath).listFiles();
    if (savegameFiles == null) {
      return;
    }
    for (File savegameFile : savegameFiles) {
      if (savegameFile.isFile()) {
        SaveGame saveGame = savegameService.loadGame(savegameFile.getName().replace(".json", ""));
        createSavegameHelper(saveGame.getGamename(), saveGame.getId(),
            new SaveGameJson(saveGame.getId(), saveGame.getGamename(), saveGame.getUsernames()));
      }
    }
  }

  private ResponseEntity<String> createSavegameHelper(String gamename, String savegameId,
                                                      SaveGameJson saveGameJson) {
    ResponseEntity<TokensInfo> tokensInfo = authService.login(gsUsername, gsPassword);
    URI url = urlUtils.createLobbyServiceUri(
        "/api/gameservices/" + gamename + "/savegames/" + savegameId,
        "access_token=" + Objects.requireNonNull(tokensInfo.getBody()).getAccessToken());
    assert url != null;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    HttpEntity<SaveGameJson> entity = new HttpEntity<>(saveGameJson, headers);
    try {
      restTemplate.put(url, entity);
    } catch (Exception e) {
      e.printStackTrace();
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
    return CustomResponseFactory.getResponse(CustomHttpResponses.OK);
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
    ResponseEntity<TokensInfo> tokensInfo = authService.login(gsUsername, gsPassword);
    URI url = urlUtils.createLobbyServiceUri(
        "/api/gameservices/" + gamename + "/savegames/" + savegameId,
        "access_token=" + Objects.requireNonNull(tokensInfo.getBody()).getAccessToken());
    assert url != null;
    try {
      restTemplate.delete(url);
      return CustomResponseFactory.getResponse(CustomHttpResponses.OK);
    } catch (Exception e) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
  }

  /**
   * This method deletes all savegames in Lobby Service associated with the provided game service.
   *
   * @param gamename The name of the game server.
   * @return status response
   */
  @DeleteMapping("/gameservices/{gamename}/savegames")
  public ResponseEntity<String> deleteAllSavegames(@PathVariable String gamename) {
    ResponseEntity<TokensInfo> tokensInfo = authService.login(gsUsername, gsPassword);
    URI url = urlUtils.createLobbyServiceUri(
        "/api/gameservices/" + gamename + "/savegames",
        "access_token=" + Objects.requireNonNull(tokensInfo.getBody()).getAccessToken());
    assert url != null;
    try {
      restTemplate.delete(url);
      return CustomResponseFactory.getResponse(CustomHttpResponses.OK);
    } catch (Exception e) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
  }
}
