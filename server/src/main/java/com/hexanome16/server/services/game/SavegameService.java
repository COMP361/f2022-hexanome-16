package com.hexanome16.server.services.game;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.savegame.SaveGame;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This class is used to handle loading and creating savegames.
 */
@Service
public class SavegameService implements SavegameServiceInterface {
  private final ObjectWriter objectWriter;
  private final ObjectReader objectReader;
  @Value("${path.savegames}")
  private String savegamesPath;

  /**
   * Constructor.
   */
  public SavegameService() {
    ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(
            JsonInclude.Include.NON_NULL)
        .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
    objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    objectReader = objectMapper.readerFor(SaveGame.class);
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
  public void saveGame(Game game, String id) {
    String gamename = game.getWinCondition().getGameServiceJson().getName();
    String[] usernames = Arrays.stream(game.getPlayers()).sorted(Comparator.comparingInt(
        ServerPlayer::getPlayerOrder)).map(ServerPlayer::getName).toArray(String[]::new);
    String currentPlayer = game.getCurrentPlayer().getName();
    Map<Level, ServerLevelCard[]> onBoardDecks = game.getOnBoardDecks().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            entry -> entry.getValue().getCardList().toArray(ServerLevelCard[]::new)));
    ServerNoble[] onBoardNobles = game.getOnBoardNobles().getCardList().toArray(ServerNoble[]::new);
    Map<Level, ServerLevelCard[]> remainingDecks = Arrays.stream(Level.values())
        .collect(Collectors.toMap(level -> level, level -> game.getLevelDeck(level)
            .getCardList().toArray(ServerLevelCard[]::new)));
    ServerNoble[] remainingNobles = game.getRemainingNobles().values().toArray(ServerNoble[]::new);
    PurchaseMap gameBank = game.getGameBank().toPurchaseMap();
    ServerPlayer[] serverPlayers = game.getPlayers();
    SaveGame saveGame = new SaveGame(gamename, id, currentPlayer, usernames, game.getCreator(),
        onBoardDecks, onBoardNobles, remainingDecks, remainingNobles, gameBank, serverPlayers);
    objectWriter.writeValue(new File(savegamesPath + "/" + id + ".json"), saveGame);
  }

  /**
   * Deletes a savegame.
   *
   * @param id The id of the savegame.
   */
  @SneakyThrows
  @Override
  public void deleteSavegame(String id) {
    File file = new File(savegamesPath + "/" + id + ".json");
    if (file.exists()) {
      file.delete();
    }
  }
}
