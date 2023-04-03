package com.hexanome16.server.models.savegame;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.cards.ServerCity;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.game.Game;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to handle loading and creating savegames.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveGame {
  private String gamename;
  private String id;
  private int currentPlayerIndex;
  private String[] usernames;
  private String creator;
  private Map<Level, ServerLevelCard[]> onBoardDecks;
  private ServerNoble[] onBoardNobles;
  private ServerCity[] onBoardCities;
  private Map<Level, ServerLevelCard[]> remainingDecks;
  private ServerNoble[] remainingNobles;
  private ServerCity[] remainingCities;
  private PurchaseMap bank;
  private ServerPlayer[] players;

  /**
   * Creates a SaveGame from a Game.
   *
   * @param game the game
   * @param id  the savegame id
   */
  public SaveGame(Game game, String id) {
    gamename = game.getWinCondition().getGameServiceJson().getName();
    this.id = id;
    currentPlayerIndex = game.getCurrentPlayerIndex();
    usernames = Arrays.stream(game.getPlayers()).sorted(Comparator.comparingInt(
        ServerPlayer::getPlayerOrder)).map(ServerPlayer::getName).toArray(String[]::new);
    creator = game.getCreator();
    onBoardDecks = game.getOnBoardDecks().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            entry -> entry.getValue().getCardList().toArray(ServerLevelCard[]::new)));
    onBoardNobles = game.getOnBoardNobles().getCardList().toArray(ServerNoble[]::new);
    onBoardCities = game.getOnBoardCities().getCardList().toArray(ServerCity[]::new);
    remainingDecks = Arrays.stream(Level.values())
        .collect(Collectors.toMap(level -> level, level -> game.getLevelDeck(level)
            .getCardList().toArray(ServerLevelCard[]::new)));
    remainingNobles = game.getRemainingNobles().values().toArray(ServerNoble[]::new);
    remainingCities = game.getRemainingCities().values().toArray(ServerCity[]::new);
    bank = game.getGameBank().toPurchaseMap();
    players = game.getPlayers();
  }
}
