package com.hexanome16.client.screens.game;

import static com.hexanome16.client.requests.RequestClient.objectMapper;
import static com.hexanome16.client.screens.game.GameFactory.getLevelCardEntity;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.hexanome16.client.requests.backend.TradePostRequest;
import com.hexanome16.client.requests.backend.cards.GameRequest;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.requests.lobbyservice.sessions.SessionDetailsRequest;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.components.NobleComponent;
import com.hexanome16.client.screens.game.players.DeckFactory;
import com.hexanome16.client.screens.game.players.PlayerDecks;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.ChooseNoble;
import com.hexanome16.common.dto.PlayerJson;
import com.hexanome16.common.dto.PlayerListJson;
import com.hexanome16.common.dto.TradePostJson;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.dto.cards.NobleDeckJson;
import com.hexanome16.common.models.CardInfo;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.util.Pair;
import org.checkerframework.checker.units.qual.N;

/**
 * GameScreen class spawns all the entities for game board.
 */
public class GameScreen {
  private static final Map<Level, Map<String, LevelCard>> levelCards = new HashMap<>();

  private static final Map<String, Noble> nobles = new HashMap<>();

  private static final Map<Level, Pair<String, DeckJson>> levelDecks = new HashMap<>();
  private static final Map<Level, Thread> levelThreads = new HashMap<>();

  private static Pair<String, NobleDeckJson> nobleJson;
  private static Thread updateNobles;

  private static Pair<String, PlayerListJson> playersJson;

  private static Thread updateCurrentPlayer;

  private static String[] usernames;
  private static final Map<Integer, PlayerJson> usernamesMap = new HashMap<>();
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
      if (updateNobles != null) {
        updateNobles.interrupt();
        Platform.runLater(GameScreen::updateNobles);
        fetchNoblesThread();
      }
    });
    updateNobles = new Thread(updateDeckTask);
    updateNobles.setDaemon(true);
    updateNobles.start();
  }


  private static void fetchPlayersThread() {
    Task<Void> updateCurrentPlayerTask = new Task<>() {
      @Override
      protected Void call() {
        playersJson = GameRequest.updatePlayers(sessionId, playersJson.getKey());
        return null;
      }
    };
    updateCurrentPlayerTask.setOnSucceeded(e -> {
      updateCurrentPlayer.interrupt();
      fetchTradePosts();
      Platform.runLater(() -> {
        System.out.println(playersJson);
        PlayerListJson playerListJson = playersJson.getValue();
        if (playerListJson == null) {
          return;
        }
        PlayerJson[] players = playersJson.getValue().getPlayers();
        usernamesMap.forEach((k, v) -> {
          for (int i = 0; i < players.length; i++) {
            if (players[i].getUsername().equals(v.getUsername())) {
              usernamesMap.replace(i, players[i]);
            }
          }
        });
        Optional<PlayerJson> current = Arrays.stream(players)
            .filter(PlayerJson::isCurrent).findFirst();
        if (current.isPresent()) {
          currentPlayer = current.get().getUsername();
          System.out.println(currentPlayer);
          UpdateGameInfo.fetchGameBank(getSessionId());
          UpdateGameInfo.fetchAllPlayer(getSessionId(), players);
          UpdateGameInfo.setCurrentPlayer(getSessionId(), currentPlayer);
          PlayerDecks.generateAll(usernamesMap.entrySet().stream().sorted(Comparator.comparingInt(
              Map.Entry::getKey)).map(Map.Entry::getValue).toArray(PlayerJson[]::new));
        }
      });
      fetchPlayersThread();
    });
    updateCurrentPlayerTask.setOnFailed(e -> {
      if (updateCurrentPlayer != null) {
        updateCurrentPlayer.interrupt();
        System.out.println(e);
        fetchPlayersThread();
      }
    });
    updateCurrentPlayer = new Thread(updateCurrentPlayerTask);
    updateCurrentPlayer.setDaemon(true);
    updateCurrentPlayer.start();
  }

  private static void fetchTradePosts() {
    String[] colors = {"Yellow", "Black", "Red", "Blue"};
    // add trade post for each player
    for (Map.Entry<Integer, PlayerJson> user : usernamesMap.entrySet()) {
      for (TradePostJson tradePost : TradePostRequest.getTradePosts(sessionId,
          user.getValue().getUsername())) {
        switch (tradePost.getRouteType()) {
          case ONYX_ROUTE -> {
            FXGL.spawn(colors[user.getKey()] + "Marker");
          }
          default -> {
            //todo add other routes
          }
        }
      }
    }
  }

  /**
   * Adds background, mat, cards, nobles, game bank,
   * player inventory and settings button to the game screen.
   *
   * @param id game id
   */
  public static void initGame(long id) {
    // This is a hack to make sure that the game on server is initialized before we try to fetch
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    initializeBankGameVars(id);

    sessionId = id;

    FXGL.spawn("Background");
    FXGL.spawn("Mat");
    FXGL.spawn("TradeRoutesPlaceholder");
    FXGL.spawn("LevelOneDeck");
    FXGL.spawn("LevelTwoDeck");
    FXGL.spawn("LevelThreeDeck");
    FXGL.spawn("RedLevelOneDeck");
    FXGL.spawn("RedLevelTwoDeck");
    FXGL.spawn("RedLevelThreeDeck");
    FXGL.spawn("TokenBank");
    FXGL.spawn("Setting");

    String name =
        SessionDetailsRequest.execute(sessionId, "").getValue().getGameParameters().getName();
    if (name.contains("TradeRoutes")) {
      FXGL.spawn("TradeRoutes");
    }
    for (Level level : Level.values()) {
      levelCards.put(level, new HashMap<>());
      levelDecks.put(level, new Pair<>("", new DeckJson(level)));
      levelThreads.put(level, createFetchLevelThread(level));
    }

    if (updateNobles == null) {
      nobleJson = new Pair<>("", new NobleDeckJson());
      fetchNoblesThread();
    }
    UpdateGameInfo.initPlayerTurn();
    usernames = FXGL.getWorldProperties().getValue("players");
    currentPlayer = usernames[0];
    PlayerJson[] players = Arrays.stream(usernames).map(
        username -> new PlayerJson(username, Objects.equals(currentPlayer, username), 0)
    ).toArray(PlayerJson[]::new);
    if (updateCurrentPlayer == null) {
      playersJson = new Pair<>("", new PlayerListJson(players));
      fetchPlayersThread();
    }

    // build user map
    for (int i = 0; i < usernames.length; i++) {
      usernamesMap.put(i, players[i]);
    }

    UpdateGameInfo.fetchAllPlayer(getSessionId(), players);
    // spawn the player's hands
    PlayerDecks.generateAll(playersJson.getValue().getPlayers());
  }

  // puts values necessary for game bank in the world properties
  private static void initializeBankGameVars(long id) {
    PurchaseMap gameBank = null;
    while (gameBank == null) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      gameBank = PromptsRequests.getGameBankInfo(id);
    }
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
        switch (level) {
          case ONE, TWO, THREE -> {
            for (int i = 0; i < 4; i++) {
              if (hash.equals(grid[i].getCardHash())) {
                grid[i].removeFromMat();
              }
            }
          }
          case REDONE, REDTWO, REDTHREE -> {
            for (int i = 0; i < 2; i++) {
              if (hash.equals(grid[i].getCardHash())) {
                grid[i].removeFromMat();
              }
            }
          }
          default -> throw new IllegalStateException("Unexpected value: " + level);
        }

        break;
      }
    }
    cardMap.remove(hashToRemove);
    for (Map.Entry<String, LevelCard> entry : cardHashList.entrySet()) {
      String hash = entry.getKey();
      LevelCard card = entry.getValue();
      if (!cardMap.containsKey(hash)) {
        cardMap.put(hash, card);
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
    playersJson = null;
    currentPlayer = null;
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

  /**
   * Returns Hash of a given LevelCard. (ON BOARD)
   *
   * @param levelCard card whose hash we want.
   * @return Hash of card, null of no such card.
   */
  public static String getCardHash(LevelCard levelCard) {
    LevelCard card;
    for (Level level : Level.values()) {
      for (Map.Entry<String, LevelCard> entry : levelCards.get(level).entrySet()) {
        card = entry.getValue();
        if (levelCard.getCardInfo().texturePath().equals(card.getCardInfo().texturePath())) {
          return entry.getKey();
        }
      }
    }
    return null;
  }


  /**
   * Returns Hash of a given Noble. (ON BOARD)
   *
   * @param noble noble whose hash we want.
   * @return Hash of noble, null of no such noble.
   */
  public static String getNobleHash(Noble noble) {
    Noble n;
    for (Map.Entry<String, Noble> entry : nobles.entrySet()) {
      n = entry.getValue();
      if (noble.getCardInfo().texturePath().equals(n.getCardInfo().texturePath())) {
        return entry.getKey();
      }
    }
    return null;
  }
}
