package com.hexanome16.server.services.game;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.auth.TokensInfo;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.models.sessions.SaveGameJson;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.cards.ServerCity;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.savegame.SaveGame;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.CustomSerializerModifier;
import com.hexanome16.server.util.UrlUtils;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
    String gamename = game.getWinCondition().getGameServiceJson().getName();
    String[] usernames = Arrays.stream(game.getPlayers()).sorted(Comparator.comparingInt(
        ServerPlayer::getPlayerOrder)).map(ServerPlayer::getName).toArray(String[]::new);
    String currentPlayer = game.getCurrentPlayer().getName();
    Map<Level, ServerLevelCard[]> onBoardDecks = game.getOnBoardDecks().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            entry -> entry.getValue().getCardList().toArray(ServerLevelCard[]::new)));
    ServerNoble[] onBoardNobles = game.getOnBoardNobles().getCardList().toArray(ServerNoble[]::new);
    ServerCity[] onBoardCities = game.getOnBoardCities().getCardList().toArray(ServerCity[]::new);
    Map<Level, ServerLevelCard[]> remainingDecks = Arrays.stream(Level.values())
        .collect(Collectors.toMap(level -> level, level -> game.getLevelDeck(level)
            .getCardList().toArray(ServerLevelCard[]::new)));
    ServerNoble[] remainingNobles = game.getRemainingNobles().values().toArray(ServerNoble[]::new);
    ServerCity[] remainingCities = game.getRemainingCities().values().toArray(ServerCity[]::new);
    PurchaseMap gameBank = game.getGameBank().toPurchaseMap();
    ServerPlayer[] serverPlayers = game.getPlayers();
    SaveGame saveGame = new SaveGame(gamename, id, currentPlayer, usernames, game.getCreator(),
        onBoardDecks, onBoardNobles, onBoardCities, remainingDecks, remainingNobles,
        remainingCities, gameBank, serverPlayers);
    objectWriter.writeValue(new File(savegamesPath + "/" + id + ".json"), saveGame);
    return createSavegameHelper(gamename, id, saveGameJson);
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
      File[] savegames = getSavegameFiles();
      for (File savegame : savegames) {
        if (!savegame.exists()) {
          return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
        }
        SaveGame saveGame = loadGame(savegame.getName().replace(".json", ""));
        if (!saveGame.getGamename().equals(gamename)) {
          continue;
        }
        if (!savegame.delete()) {
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

  @Override
  public File[] getSavegameFiles() {
    return new File(savegamesPath).listFiles();
  }
}
