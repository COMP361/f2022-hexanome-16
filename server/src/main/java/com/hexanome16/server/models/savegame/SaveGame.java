package com.hexanome16.server.models.savegame;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.game.Game;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * This class is used to handle loading and creating savegames.
 */
@Getter
public class SaveGame {
  private static final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(
          JsonInclude.Include.NON_NULL)
      .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
  private static final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
  private static final ObjectReader objectReader = objectMapper.readerFor(SaveGame.class);
  private static final String savegamesPath;

  static {
    try {
      savegamesPath =
          PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"))
              .getProperty("path.savegames");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private final String gamename;
  private final String id;
  private final String[] usernames;
  private final String creator;
  private final Map<Level, ServerLevelCard[]> onBoardDecks;
  private final ServerNoble[] onBoardNobles;
  private final Map<Level, ServerLevelCard[]> remainingDecks;
  private final ServerNoble[] remainingNobles;
  private final PurchaseMap bank;
  private final ServerPlayer[] players;

  /**
   * Constructor.
   *
   * @param gamename        the name of the game server
   * @param id              the id of the game
   * @param usernames       the usernames of the players
   * @param creator         the username of the creator
   * @param onBoardDecks    the decks on the board
   * @param onBoardNobles   the nobles on the board
   * @param remainingDecks  the remaining decks
   * @param remainingNobles the remaining nobles
   * @param bank            the game bank
   * @param players         the players
   */
  @SneakyThrows
  public SaveGame(String gamename, String id, String[] usernames, String creator,
                  Map<Level, ServerLevelCard[]> onBoardDecks, ServerNoble[] onBoardNobles,
                  Map<Level, ServerLevelCard[]> remainingDecks,
                  ServerNoble[] remainingNobles, PurchaseMap bank, ServerPlayer[] players) {
    this.gamename = gamename;
    this.id = id;
    this.usernames = usernames;
    this.onBoardDecks = onBoardDecks;
    this.onBoardNobles = onBoardNobles;
    this.remainingDecks = remainingDecks;
    this.remainingNobles = remainingNobles;
    this.bank = bank;
    this.players = players;
    this.creator = creator;
  }

  /**
   * Loads a game from a savegame JSON file.
   *
   * @param sessionId  the id of the session
   * @param savegameId the id of the savegame (name of the JSON file)
   * @return the parsed game
   */
  @SneakyThrows
  public static Game loadGame(long sessionId, String savegameId) {
    SaveGame saveGame = objectReader.readValue(new File(savegamesPath + "/" + savegameId + ".json"),
        SaveGame.class);
    return Game.create(sessionId, saveGame);
  }

  /**
   * Saves a game to a savegame JSON file.
   *
   * @param game the game to save
   * @param id   the id of the savegame (name of the JSON file)
   */
  @SneakyThrows
  public static void saveGame(Game game, String id) {
    String gamename = game.getWinCondition().getGameServiceJson().getName();
    String[] usernames = Arrays.stream(game.getPlayers()).sorted(Comparator.comparingInt(
        ServerPlayer::getPlayerOrder)).map(ServerPlayer::getName).toArray(String[]::new);
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
    SaveGame saveGame = new SaveGame(gamename, id, usernames, game.getCreator(), onBoardDecks,
        onBoardNobles, remainingDecks, remainingNobles, gameBank, serverPlayers);
    objectWriter.writeValue(new File(savegamesPath + "/" + id + ".json"), saveGame);
  }
}
