package com.hexanome16.client.screens.game;

import static com.hexanome16.client.screens.game.GameFactory.getLevelCardEntity;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.hexanome16.client.requests.backend.cards.GameRequest;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.components.NobleComponent;
import com.hexanome16.client.screens.game.players.PlayerDecks;
import com.hexanome16.common.dto.DeckJson;
import com.hexanome16.common.dto.NobleDeckJson;
import com.hexanome16.common.dto.PlayerJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.util.Pair;

/**
 * GameScreen class spawns all the entities for game board.
 */
public class GameScreen {
  private static final Map<Level, Map<String, LevelCard>> levelCards = new HashMap<>();

  private static final Map<String, Noble> nobles = new HashMap<>();

  private static final Map<Level, Pair<String, DeckJson>> levelDecks = new HashMap<>();
  private static final Map<Level, Thread> levelThreads = new HashMap<>();

  private static Pair<String, NobleDeckJson> nobleJson = new Pair<>("", new NobleDeckJson());
  private static Thread updateNobles;

  private static Pair<String, PlayerJson> currentPlayerJson = new Pair<>("", new PlayerJson(""));

  private static Thread updateCurrentPlayer;

  private static String[] usernames;
  private static String currentPlayer;
  private static long sessionId = -1;

  private static Thread createFetchLevelThread(Level level) {
    Task<Void> updateDeckTask = new Task<>() {
      @Override
      protected Void call() {
        levelDecks.put(level, GameRequest.updateDeck(
            sessionId, level, levelDecks.get(level).getKey()));
        return null;
      }
    };
    updateDeckTask.setOnSucceeded(e -> {
      levelThreads.get(level).interrupt();
      Platform.runLater(() -> GameScreen.updateLevelDeck(level));
      levelThreads.put(level, createFetchLevelThread(level));
    });
    Thread updateDeck = new Thread(updateDeckTask);
    updateDeck.setDaemon(true);
    updateDeck.start();
    return updateDeck;
  }

  private static void fetchNoblesThread() {
    Task<Void> updateDeckTask = new Task<>() {
      @Override
      protected Void call() {
        nobleJson = GameRequest.updateNoble(sessionId, nobleJson.getKey());
        return null;
      }
    };
    updateDeckTask.setOnSucceeded(e -> {
      updateNobles.interrupt();
      Platform.runLater(GameScreen::updateNobles);
      fetchNoblesThread();
    });
    updateNobles = new Thread(updateDeckTask);
    updateNobles.setDaemon(true);
    updateNobles.start();
  }


  private static void fetchCurrentPlayerThread() {
    Task<Void> updateCurrentPlayerTask = new Task<>() {
      @Override
      protected Void call() {
        currentPlayerJson = GameRequest.updateCurrentPlayer(sessionId, currentPlayerJson.getKey());
        return null;
      }
    };
    updateCurrentPlayerTask.setOnSucceeded(e -> {
      updateCurrentPlayer.interrupt();
      Platform.runLater(() -> {
        currentPlayer = currentPlayerJson.getValue().getUsername();
        UpdateGameInfo.fetchGameBank(getSessionId());
        UpdateGameInfo.fetchAllPlayer(getSessionId(), usernames);
        UpdateGameInfo.setCurrentPlayer(getSessionId(), currentPlayer);
      });
      fetchCurrentPlayerThread();
    });
    updateCurrentPlayer = new Thread(updateCurrentPlayerTask);
    updateCurrentPlayer.setDaemon(true);
    updateCurrentPlayer.start();
  }


  /**
   * Adds background, mat, cards, nobles, game bank,
   * player inventory and settings button to the game screen.
   *
   * @param id game id
   */
  public static void initGame(long id) {
    initializeBankGameVars(id);

    sessionId = id;

    FXGL.spawn("Background");
    FXGL.spawn("Mat");
    FXGL.spawn("LevelOneDeck");
    FXGL.spawn("LevelTwoDeck");
    FXGL.spawn("LevelThreeDeck");
    FXGL.spawn("RedLevelOneDeck");
    FXGL.spawn("RedLevelTwoDeck");
    FXGL.spawn("RedLevelThreeDeck");
    FXGL.spawn("TokenBank");
    FXGL.spawn("Setting");

    for (Level level : Level.values()) {
      levelCards.put(level, new HashMap<>());
      levelDecks.put(level, new Pair<>("", new DeckJson(level)));
      levelThreads.put(level, createFetchLevelThread(level));
    }

    if (updateNobles == null) {
      fetchNoblesThread();
    }
    if (updateCurrentPlayer == null) {
      fetchCurrentPlayerThread();
    }
    UpdateGameInfo.initPlayerTurn();
    usernames = FXGL.getWorldProperties().getValue("players");
    UpdateGameInfo.fetchAllPlayer(getSessionId(), usernames);
    // spawn the player's hands
    PlayerDecks.generateAll(usernames);
  }

  // puts values necessary for game bank in the world properties
  private static void initializeBankGameVars(long id) {
    PurchaseMap gameBank = PromptsRequests.getGameBankInfo(id);
    for (Map.Entry<Gem, Integer> gemEntry : gameBank.getPriceMap().entrySet()) {
      FXGL.getWorldProperties().setValue(id + gemEntry.getKey().name(), gemEntry.getValue());
    }
  }

  /**
   * Updates on board nobles.
   */
  private static void updateNobles() {
    Map<String, Noble> nobleMap = nobleJson.getValue().getNobles();
    for (Map.Entry<String, Noble> entry : nobleMap.entrySet()) {
      if (!nobles.containsKey(entry.getKey())) {
        nobles.put(entry.getKey(), entry.getValue());
        Noble noble = entry.getValue();
        PriceInterface pm = noble.getCardInfo().price();
        FXGL.spawn("Noble",
            new SpawnData().put("id", noble.getCardInfo().id())
                .put("texture", noble.getCardInfo().texturePath())
                .put("price", pm).put("MD5", entry.getKey()));
      }
    }
  }

  /**
   * Updates on board decks.
   *
   * @param level level of the deck
   */
  private static void updateLevelDeck(Level level) {
    Map<String, LevelCard> cardHashList = levelDecks.get(level).getValue().getCards();
    Map<String, LevelCard> cardMap = levelCards.get(level);
    CardComponent[] grid = CardComponent.getGrid(level);
    String cardName = getLevelCardEntity(level);
    String hashToRemove = "";
    for (Map.Entry<String, LevelCard> entry : cardMap.entrySet()) {
      String hash = entry.getKey();
      if (!cardHashList.containsKey(hash)) {
        hashToRemove = hash;
        for (int i = 0; i < 4; i++) {
          if (hash.equals(grid[i].getCardHash())) {
            grid[i].removeFromMat();
          }
        }
        break;
      }
    }
    cardMap.remove(hashToRemove);
    System.out.println("number in deck: " + cardHashList.size());
    for (Map.Entry<String, LevelCard> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      LevelCard card = entry.getValue();
      if (!cardMap.containsKey(hash)) {
        cardMap.put(hash, card);
        System.out.println("adding -- " + card.getLevel().name() + " : " + card.getCardInfo().id());
        FXGL.spawn(cardName,
            new SpawnData().put("id", card.getCardInfo().id())
                .put("texture", card.getCardInfo().texturePath())
                .put("price", card.getCardInfo().price())
                .put("level", level)
                .put("MD5", hash));
      }
    }
  }

  /**
   * Resets every component and clears the game board when exit the game.
   */
  public static void exitGame() {
    for (Level level : Level.values()) {
      levelCards.get(level).clear();
      levelThreads.get(level).interrupt();
    }
    levelDecks.clear();
    levelThreads.clear();
    nobleJson = null;
    currentPlayerJson = null;
    updateNobles = null;
    updateCurrentPlayer = null;
    nobles.clear();
    CardComponent.reset();
    NobleComponent.reset();
    FXGL.getGameWorld()
        .removeEntities(FXGL.getGameWorld().getEntitiesByComponent(CardComponent.class));
    FXGL.getGameWorld()
        .removeEntities(FXGL.getGameWorld().getEntitiesByComponent(ViewComponent.class));
  }

  /**
   * Returns session Id.
   *
   * @return returns session Id.
   */
  public static long getSessionId() {
    return sessionId;
  }
}
