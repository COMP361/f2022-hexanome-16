package com.hexanome16.server.services.game;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hexanome16.common.models.auth.TokensInfo;
import com.hexanome16.common.models.sessions.SaveGameJson;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.savegame.SaveGame;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.CustomSerializerModifier;
import com.hexanome16.server.util.UrlUtils;
import java.io.File;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * This class is used to handle loading and creating savegames.
 */
@Service
public class SavegameService implements SavegameServiceInterface {
  private final ObjectWriter objectWriter;
  private final ObjectReader objectReader;
  private final UrlUtils urlUtils;
  private final RestTemplate restTemplate;

  private final AuthServiceInterface authService;
  @Value("${gs.username}")
  private String gsUsername;
  @Value("${gs.password}")
  private String gsPassword;
  @Value("${path.savegames}")
  private String savegamesPath;

  /**
   * Constructor.
   *
   * @param urlUtils            the url utils
   * @param restTemplateBuilder the rest template builder
   * @param authService         the auth service
   */
  public SavegameService(@Autowired UrlUtils urlUtils,
                         @Autowired RestTemplateBuilder restTemplateBuilder,
                         @Autowired AuthServiceInterface authService) {
    ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(
            JsonInclude.Include.NON_NULL)
        .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
        .registerModule(new SimpleModule() {
          @Override
          public void setupModule(Module.SetupContext context) {
            super.setupModule(context);
            context.addBeanSerializerModifier(new CustomSerializerModifier());
          }
        });
    objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    objectReader = objectMapper.readerFor(SaveGame.class);
    this.urlUtils = urlUtils;
    this.restTemplate = restTemplateBuilder.build();
    this.authService = authService;
  }

  /**
   * Loads a game from a savegame JSON file.
   *
   * @param savegameId the id of the savegame (name of the JSON file)
   * @return the parsed game
   */
  @SneakyThrows
  @Override
  public SaveGame loadGame(String savegameId) {
    return objectReader.readValue(new File(savegamesPath + "/" + savegameId + ".json"),
        SaveGame.class);
  }

  /**
   * Saves a game to a savegame JSON file.
   *
   * @param game the game to save
   * @param id   the id of the savegame (name of the JSON file)
   */
  @SneakyThrows
  @Override
  public ResponseEntity<String> saveGame(Game game, String id, SaveGameJson saveGameJson) {
    SaveGame saveGame = new SaveGame(game, id);
    File saveGameFile = new File(savegamesPath + "/" + id + ".json");
    objectWriter.writeValue(saveGameFile, saveGame);
    return createSavegameHelper(saveGameJson.getGameName(), id, saveGameJson);
  }

  /**
   * Deletes a savegame.
   *
   * @param gamename   The name of the game server.
   * @param savegameId The id of the savegame.
   */
  @SneakyThrows
  @Override
  public ResponseEntity<String> deleteSavegame(String gamename, String savegameId) {
    ResponseEntity<TokensInfo> tokensInfo = authService.login(gsUsername, gsPassword);
    URI url = urlUtils.createLobbyServiceUri(
        "/api/gameservices/" + gamename + "/savegames/" + savegameId,
        "access_token=" + Objects.requireNonNull(tokensInfo.getBody()).getAccessToken());
    assert url != null;
    try {
      restTemplate.delete(url);
      File file = new File(savegamesPath + "/" + savegameId + ".json");
      if (file.exists() && file.delete()) {
        return CustomResponseFactory.getResponse(CustomHttpResponses.OK);
      }
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    } catch (Exception e) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
  }

  @SneakyThrows
  @Override
  public ResponseEntity<String> deleteAllSavegames(String gamename) {
    ResponseEntity<TokensInfo> tokensInfo = authService.login(gsUsername, gsPassword);
    URI url = urlUtils.createLobbyServiceUri(
        "/api/gameservices/" + gamename + "/savegames",
        "access_token=" + Objects.requireNonNull(tokensInfo.getBody()).getAccessToken());
    assert url != null;
    try {
      restTemplate.delete(url);
      DirectoryStream<Path> savegames = getSavegameFiles();
      for (Path savegame : savegames) {
        SaveGame saveGame = loadGame(savegame.toFile().getName().replace(".json", ""));
        if (saveGame.getGamename().equals(gamename) && !savegame.toFile().delete()) {
          return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
        }
      }
      return CustomResponseFactory.getResponse(CustomHttpResponses.OK);
    } catch (Exception e) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
  }

  /**
   * Creates a savegame in LS.
   *
   * @param gamename     The game server name.
   * @param savegameId   The savegame id.
   * @param saveGameJson The savegame json.
   * @return The response entity.
   */
  public ResponseEntity<String> createSavegameHelper(String gamename, String savegameId,
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

  @SneakyThrows
  @Override
  public DirectoryStream<Path> getSavegameFiles() {
    return Files.newDirectoryStream(Path.of(savegamesPath), "*.json");
  }
}
