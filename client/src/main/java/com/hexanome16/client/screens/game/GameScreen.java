package com.hexanome16.client.screens.game;

import static com.hexanome16.client.screens.game.GameFactory.getLevelCardEntity;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.hexanome16.client.requests.backend.TradePostRequest;
import com.hexanome16.client.requests.backend.cards.GameRequest;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.requests.lobbyservice.savegames.CreateSavegameRequest;
import com.hexanome16.client.requests.lobbyservice.sessions.SessionDetailsRequest;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.components.CityComponent;
import com.hexanome16.client.screens.game.components.NobleComponent;
import com.hexanome16.client.screens.game.players.PlayerDecks;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.WinnerPrompt;
import com.hexanome16.client.screens.lobby.LobbyScreen;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.BackgroundService;
import com.hexanome16.common.dto.PlayerJson;
import com.hexanome16.common.dto.PlayerListJson;
import com.hexanome16.common.dto.TradePostJson;
import com.hexanome16.common.dto.WinJson;
import com.hexanome16.common.dto.cards.CitiesJson;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.dto.cards.NobleDeckJson;
import com.hexanome16.common.models.City;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.util.Pair;
import kong.unirest.core.Headers;

/**
 * GameScreen class spawns all the entities for game board.
 */
public class GameScreen {
  private static final Map<Level, Map<String, LevelCard>> levelCards = new HashMap<>();

  private static final Map<String, Noble> nobles = new HashMap<>();
  private static final Map<String, City> cities = new HashMap<>();

  private static final Map<Level, Pair<String, DeckJson>> levelDecks = new HashMap<>();
  private static final Map<Level, BackgroundService> levelThreads = new HashMap<>();

  private static Pair<String, NobleDeckJson> nobleJson;
  private static Pair<String, CitiesJson> citiesJson;
  private static BackgroundService updateNobles;
  private static BackgroundService updateCities;

  private static Pair<String, PlayerListJson> playersJson;

  private static BackgroundService updateCurrentPlayer;

  private static final Map<Integer, PlayerJson> usernamesMap = new HashMap<>();
  private static String currentPlayer;
  private static final Map<Integer, BackgroundService> updateTradingPosts = new HashMap<>();
  private static final Map<Integer, TradePostJson[]> tradingPosts = new HashMap<>();
  private static long sessionId = -1;
  private static String gameServer;
  private static final AtomicBoolean shouldFetch = new AtomicBoolean(false);
  private static final AtomicReference<Pair<String, WinJson>> winners = new AtomicReference<>();
  private static BackgroundService updateWinners;

  private static BackgroundService createFetchLevelThread(Level level) {
    System.out.println("Creating fetch level thread for level " + level);
    BackgroundService fetchService = new BackgroundService(
        () -> {
          if (shouldFetch.get()) {
            levelDecks.put(level, GameRequest.updateDeck(
                sessionId, level, levelDecks.get(level).getKey()));
          } else {
            levelThreads.get(level).interrupt();
          }
        },
        () -> {
          if (shouldFetch.get()) {
            Platform.runLater(() -> GameScreen.updateLevelDeck(level));
            levelThreads.get(level).start();
          } else {
            levelThreads.get(level).interrupt();
          }
        },
        () -> {
          if (shouldFetch.get()) {
            levelThreads.get(level).interrupt();
          } else {
            levelThreads.get(level).start();
          }
        }
    );
    fetchService.start();
    return fetchService;
  }

  private static void fetchNoblesThread() {
    updateNobles = new BackgroundService(
        () -> {
          if (shouldFetch.get()) {
            nobleJson = GameRequest.updateNoble(sessionId, nobleJson.getKey());
          } else {
            updateNobles.interrupt();
          }
        },
        () -> {
          if (shouldFetch.get()) {
            Platform.runLater(GameScreen::updateNobles);
            updateNobles.start();
          } else {
            updateNobles.interrupt();
          }
        },
        () -> {
          if (shouldFetch.get()) {
            updateNobles.start();
          } else {
            updateNobles.interrupt();
          }
        }
    );
    updateNobles.start();
  }

  private static void fetchCitiesThread() {
    updateCities = new BackgroundService(
        () -> {
          if (shouldFetch.get()) {
            citiesJson = GameRequest.updateCities(sessionId, citiesJson.getKey());
          } else {
            updateCities.start();
          }
        },
        () -> {
          if (shouldFetch.get()) {
            Platform.runLater(GameScreen::updateCities);
            updateCities.start();
          } else {
            updateCities.interrupt();
          }
        },
        () -> {
          if (shouldFetch.get()) {
            updateCities.start();
          } else {
            updateCities.interrupt();
          }
        }
    );
    updateCities.start();
  }

  private static void fetchPlayersThread() {
    updateCurrentPlayer = new BackgroundService(
        () -> {
          if (shouldFetch.get()) {
            playersJson = GameRequest.updatePlayers(sessionId, playersJson.getKey());
          } else {
            updateCurrentPlayer.interrupt();
          }
        },
        () -> {
          if (shouldFetch.get()) {
            Platform.runLater(() -> {
              if (playersJson == null || playersJson.getValue() == null) {
                return;
              }
              PlayerJson[] players = playersJson.getValue().getPlayers();
              Optional<PlayerJson> current = Arrays.stream(players)
                  .filter(PlayerJson::isCurrent).findFirst();
              if (current.isPresent()) {
                Arrays.stream(players).forEach(playerJson -> {
                  usernamesMap.put(playerJson.getPlayerOrder(), playerJson);
                });
                currentPlayer = current.get().getUsername();
                UpdateGameInfo.fetchGameBank(getSessionId());
                UpdateGameInfo.fetchAllPlayer(getSessionId(), players);
                UpdateGameInfo.setCurrentPlayer(getSessionId(), currentPlayer);
                PlayerDecks.generateAll(Arrays.stream(players).sorted(Comparator.comparingInt(
                    PlayerJson::getPlayerOrder)).toArray(PlayerJson[]::new));
              }
            });

            // update trade routes
            String[] usernames = FXGL.getWorldProperties().getValue("players");
            PlayerJson[] players = IntStream.range(0, usernames.length).mapToObj(
                i -> new PlayerJson(usernames[i], Objects.equals(currentPlayer, usernames[i]), 0, i)
            ).toArray(PlayerJson[]::new);
            for (int i = 0; i < usernames.length; i++) {
              usernamesMap.put(i, players[i]);
              tradingPosts.put(i, new TradePostJson[0]);
              fetchTradePostsThread(i);
            }
            updateCurrentPlayer.start();
          } else {
            updateCurrentPlayer.interrupt();
          }
        },
        () -> {
          if (shouldFetch.get()) {
            updateCurrentPlayer.start();
          } else {
            updateCurrentPlayer.interrupt();
          }
        }
    );
    updateCurrentPlayer.start();
  }

  private static void fetchTradePostsThread(int index) {
    String[] colors = {"Yellow", "Black", "Red", "Blue"};
    if (shouldFetch.get()) {
      tradingPosts.put(index, TradePostRequest.getTradePosts(sessionId,
          usernamesMap.get(index).getUsername()));
      Platform.runLater(() -> {
        for (TradePostJson tradePost :
            tradingPosts.getOrDefault(index, new TradePostJson[0])) {
          switch (tradePost.getRouteType()) {
            case ONYX_ROUTE -> {
              FXGL.spawn(colors[index] + "Marker", new SpawnData().put("index", 0));
            }
            case EMERALD_ROUTE -> {
              FXGL.spawn(colors[index] + "Marker", new SpawnData().put("index", 1));
            }
            case SAPPHIRE_ROUTE -> {
              FXGL.spawn(colors[index] + "Marker", new SpawnData().put("index", 2));
            }
            case DIAMOND_ROUTE -> {
              FXGL.spawn(colors[index] + "Marker", new SpawnData().put("index", 3));
            }
            case RUBY_ROUTE -> {
              FXGL.spawn(colors[index] + "Marker", new SpawnData().put("index", 4));
            }
            default -> {
            }
          }
        }
      });
    }
  }

  private static void fetchWinnersThread() {
    updateWinners = new BackgroundService(
        () -> {
          if (shouldFetch.get()) {
            winners.set(PromptsRequests.getWinners(sessionId,
                AuthUtils.getAuth().getAccessToken(), winners.get().getKey()));
          } else {
            updateWinners.interrupt();
          }
        },
        () -> {
          if (winners.get() != null && winners.get().getValue() != null
              && winners.get().getValue().getWinners() != null
              && winners.get().getValue().getWinners().length > 0
              && winners.get().getValue().getWinners()[0] != null) {
            WinnerPrompt.winners = winners.get().getValue().getWinners();
            GameScreen.exitGame();
            LobbyScreen.initLobby();
            Platform.runLater(() -> {
              FXGL.spawn("PromptBox", new SpawnData()
                  .put("promptType", PromptTypeInterface.PromptType.WINNERS));
            });
          } else if (shouldFetch.get()) {
            updateWinners.start();
          } else {
            updateWinners.interrupt();
          }
        },
        () -> {
          if (shouldFetch.get()) {
            updateWinners.start();
          } else {
            updateWinners.interrupt();
          }
        }
    );
    updateWinners.start();
  }

  /**
   * Adds background, mat, cards, nobles, game bank,
   * player inventory and settings button to the game screen.
   *
   * @param id game id
   */
  public static void initGame(long id) {
    // This is a hack to make sure that the game on server is initialized before we try to fetch
    shouldFetch.set(true);
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

    gameServer = SessionDetailsRequest.execute(sessionId, "none").getValue()
        .getGameParameters().getName();
    if (gameServer.contains("TradeRoutes")) {
      FXGL.spawn("TradeRoutes");
    }
    for (Level level : Level.values()) {
      levelCards.put(level, new HashMap<>());
      levelDecks.put(level, new Pair<>("none", new DeckJson(level)));
      levelThreads.put(level, createFetchLevelThread(level));
    }

    nobleJson = new Pair<>("none", new NobleDeckJson());
    fetchNoblesThread();

    if (gameServer.contains("Cities")) {
      citiesJson = new Pair<>("none", new CitiesJson());
      fetchCitiesThread();
    }

    winners.set(new Pair<>("none", new WinJson()));
    fetchWinnersThread();

    UpdateGameInfo.initPlayerTurn();

    String[] usernames = FXGL.getWorldProperties().getValue("players");
    currentPlayer = usernames[0];
    PlayerJson[] players = IntStream.range(0, usernames.length).mapToObj(
        i -> new PlayerJson(usernames[i], Objects.equals(currentPlayer, usernames[i]), 0, i)
    ).toArray(PlayerJson[]::new);
    playersJson = new Pair<>("none", new PlayerListJson(players));
    fetchPlayersThread();

    UpdateGameInfo.fetchAllPlayer(getSessionId(), players);
    // spawn the player's hands
    PlayerDecks.generateAll(playersJson.getValue().getPlayers().clone());

    // open action prompt if needed.
    Pair<Headers, String> serverResponse = PromptsRequests.getActionForPlayer(sessionId,
        AuthUtils.getPlayer().getName(),
        AuthUtils.getAuth().getAccessToken());
    PromptUtils.actionResponseSpawner(serverResponse);
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
    if (nobleJson == null || nobleJson.getValue() == null) {
      return;
    }
    Map<String, Noble> nobleMap = nobleJson.getValue().getNobles();
    NobleComponent[] grid = NobleComponent.getGrid();
    //remove nobles
    for (NobleComponent noble : grid) {
      if (noble != null && !nobleMap.containsKey(noble.getNobleHash())) {
        noble.removeFromMat();
      }
    }

    //add nobles
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
   * Updates on board cities.
   */
  private static void updateCities() {
    if (citiesJson == null || citiesJson.getValue() == null) {
      return;
    }
    Map<String, City> cityMap = citiesJson.getValue().getCities();
    CityComponent[] grid = CityComponent.getGrid();
    //remove cities
    for (CityComponent city : grid) {
      if (city != null && !cityMap.containsKey(city.getCityHash())) {
        city.removeFromMat();
      }
    }

    Map<String, City> citiesMap = citiesJson.getValue().getCities();
    for (Map.Entry<String, City> entry : citiesMap.entrySet()) {
      if (!cities.containsKey(entry.getKey())) {
        cities.put(entry.getKey(), entry.getValue());
        City city = entry.getValue();
        PriceInterface pm = city.getCardInfo().price();
        FXGL.spawn("City",
            new SpawnData().put("id", city.getCardInfo().id())
                .put("texture", city.getCardInfo().texturePath())
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
    if (levelDecks.get(level) == null || levelDecks.get(level).getValue() == null) {
      return;
    }
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
   * Saves this game in Lobby Service.
   */
  public static void saveGame() {
    CreateSavegameRequest.execute(gameServer, usernamesMap.entrySet().stream().sorted(
        Comparator.comparingInt(Map.Entry::getKey)).map(e -> e.getValue().getUsername()).toArray(
        String[]::new), sessionId);
  }

  /**
   * Resets every component and clears the game board when exit the game.
   */
  public static void exitGame() {
    shouldFetch.set(false);
    for (Level level : Level.values()) {
      levelThreads.get(level).interrupt();
      levelCards.get(level).clear();
    }
    levelCards.clear();
    levelDecks.clear();
    levelThreads.clear();
    if (gameServer.contains("TradeRoutes")) {
      for (BackgroundService service : updateTradingPosts.values()) {
        service.interrupt();
      }
      updateTradingPosts.clear();
      tradingPosts.clear();
    }
    if (gameServer.contains("Cities")) {
      updateCities.interrupt();
      updateCities = null;
      cities.clear();
      citiesJson = null;
    }
    nobleJson = null;
    playersJson = null;
    currentPlayer = null;
    updateNobles.interrupt();
    updateNobles = null;
    updateCurrentPlayer.interrupt();
    updateCurrentPlayer = null;
    usernamesMap.clear();
    nobles.clear();
    winners.set(null);
    updateWinners.interrupt();
    updateWinners = null;
    CardComponent.reset();
    NobleComponent.reset();
    CityComponent.reset();
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
    if (levelCard != null) {
      LevelCard card;
      for (Level level : Level.values()) {
        for (Map.Entry<String, LevelCard> entry : levelCards.get(level).entrySet()) {
          card = entry.getValue();
          if (levelCard.getCardInfo().texturePath().equals(card.getCardInfo().texturePath())) {
            return entry.getKey();
          }
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
    if (noble != null) {
      Noble n;
      for (Map.Entry<String, Noble> entry : nobles.entrySet()) {
        n = entry.getValue();
        if (noble.getCardInfo().texturePath().equals(n.getCardInfo().texturePath())) {
          return entry.getKey();
        }
      }
    }
    return null;
  }

  /**
   * Returns Hash of a given City. (ON BOARD)
   *
   * @param cityToFind city whose hash we want.
   * @return Hash of city, null of no such city.
   */
  public static String getCityHash(City cityToFind) {
    City city;
    for (Map.Entry<String, City> entry : cities.entrySet()) {
      city = entry.getValue();
      if (city.getCardInfo().texturePath().equals(cityToFind.getCardInfo().texturePath())) {
        return entry.getKey();
      }
    }
    return null;
  }

  /**
   * Whether the player has the sapphire route. Used by BuyCardPrompt.
   *
   * @return if the current player has the sapphire route.
   */
  public static boolean hasSapphireRoute() {
    if (!tradingPosts.isEmpty()) {
      for (TradePostJson tradePost : tradingPosts.get(0)) {
        if (tradePost.getRouteType() == RouteType.SAPPHIRE_ROUTE) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * returns true if it is the client's turn, false otherwise.
   *
   * @return true or false.
   */
  public static boolean isClientsTurn() {
    return currentPlayer != null && currentPlayer.equals(AuthUtils.getPlayer().getName());
  }
}
