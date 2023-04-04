package com.hexanome16.server.models.game;

import static com.hexanome16.server.models.game.GameInitHelpers.createBoardMap;
import static com.hexanome16.server.models.game.GameInitHelpers.createLevelMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.models.sessions.SaveGameJson;
import com.hexanome16.common.util.ObjectMapperUtils;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.TradePost;
import com.hexanome16.server.models.bank.GameBank;
import com.hexanome16.server.models.cards.Deck;
import com.hexanome16.server.models.cards.ServerCity;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.savegame.SaveGame;
import com.hexanome16.server.services.winconditions.WinCondition;
import com.hexanome16.server.util.broadcastmap.BroadcastMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Game class that holds all the information.
 */
@Getter
@ToString
public class Game {

  private static final ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

  private final long sessionId;
  private final String creator;
  private final ServerPlayer[] players;


  private final Map<Level, Deck<ServerLevelCard>> remainingCards;

  private final Map<Level, Deck<ServerLevelCard>> onBoardDecks;
  /**
   * Selected nobles for this game (5 of the 10 possible).
   */
  Deck<ServerNoble> onBoardNobles;

  /**
   * Selected cities for this game (3 of the 10 possible).
   */
  Deck<ServerCity> onBoardCities;
  private final Map<String, ServerCity> remainingCities;
  private final String savegame;
  private final GameBank gameBank;
  private final WinCondition winCondition;
  private final Map<String, ServerLevelCard> hashToCardMap;
  /**
   * Remaining nobles to be acquired.
   */
  private final Map<String, ServerNoble> remainingNobles;
  private final Map<RouteType, TradePost> tradePosts;
  private BroadcastMap broadcastContentManagerMap;
  private int currentPlayerIndex;

  /**
   * Game constructor, create a new with a unique session id.
   *
   * @param sessionId    session id
   * @param players      a non-null list of players
   * @param creator      the creator
   * @param savegame     the savegame
   * @param winCondition the win condition
   */
  @SneakyThrows
  Game(long sessionId, @NonNull ServerPlayer[] players, String creator, String savegame,
       WinCondition winCondition) {
    this.sessionId = sessionId;
    this.players = players.clone();
    this.currentPlayerIndex = 0;
    this.creator = creator;
    this.savegame = savegame;
    this.winCondition = winCondition;
    this.gameBank = new GameBank();
    this.remainingCards = createLevelMap();
    this.onBoardDecks = createBoardMap();
    this.onBoardNobles = new Deck<>();
    this.onBoardCities = new Deck<>();
    this.hashToCardMap = new HashMap<>();
    this.remainingNobles = new HashMap<>();
    this.remainingCities = new HashMap<>();
    this.tradePosts = new HashMap<>();
    if (winCondition == WinCondition.TRADEROUTES) {
      for (RouteType route : RouteType.values()) {
        tradePosts.put(route, new TradePost(route));
      }
    }
  }

  /**
   * Game constructor, create a new with a unique session id.
   *
   * @param sessionId session id
   * @param payload   the payload
   */
  Game(long sessionId, SessionJson payload) {
    this(sessionId, IntStream.range(0, payload.getPlayers().length)
            .mapToObj(i -> new ServerPlayer(payload.getPlayers()[i].getName(),
                payload.getPlayers()[i].getPreferredColour(), i)).toArray(ServerPlayer[]::new),
        payload.getCreator(), payload.getSavegame(),
        WinCondition.fromServerName(payload.getGame()));
  }

  Game(long sessionId, SaveGame saveGame, SessionJson payload) {
    this.sessionId = sessionId;
    this.creator = payload.getCreator();
    for (int i = 0; i < payload.getPlayers().length; i++) {
      for (int j = 0; j < saveGame.getPlayers().length; j++) {
        if (Objects.equals(saveGame.getPlayers()[j].getName(), payload.getPlayers()[i].getName())) {
          String temp = saveGame.getPlayers()[j].getName();
          saveGame.getPlayers()[j].setName(payload.getPlayers()[i].getName());
          payload.getPlayers()[i].setName(temp);
        }
      }
    }
    this.players = IntStream.range(0, payload.getPlayers().length)
        .mapToObj(i -> {
          ServerPlayer player = saveGame.getPlayers()[i];
          player.setName(payload.getPlayers()[i].getName());
          return player;
        }).toArray(ServerPlayer[]::new);
    this.currentPlayerIndex = saveGame.getCurrentPlayerIndex();
    this.savegame = saveGame.getId();
    this.winCondition = WinCondition.fromServerName(saveGame.getGamename());
    this.gameBank = new GameBank(saveGame.getBank());
    this.remainingCards = saveGame.getRemainingDecks().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> new Deck<>(e.getValue())));
    this.onBoardDecks = saveGame.getOnBoardDecks().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> new Deck<>(e.getValue())));
    Map<Level, ServerLevelCard[]> remainingCards = saveGame.getRemainingDecks();
    Map<Level, ServerLevelCard[]> playerCards = Arrays.stream(saveGame.getPlayers())
        .flatMap(serverPlayer -> serverPlayer.getInventory().getOwnedCards().stream())
        .collect(Collectors.groupingBy(ServerLevelCard::getLevel)).entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            e -> e.getValue().toArray(new ServerLevelCard[0])));
    remainingCards.forEach((level, cards) -> {
      remainingCards.put(level, Stream.of(cards, playerCards.get(level))
          .filter(Objects::nonNull).flatMap(Arrays::stream).toArray(ServerLevelCard[]::new));
    });
    this.hashToCardMap = remainingCards.values().stream().flatMap(Arrays::stream)
        .collect(Collectors.toMap(card -> {
          try {
            return DigestUtils.md5Hex(objectMapper.writeValueAsString(card));
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
        }, card -> card));
    this.onBoardNobles = new Deck<>(saveGame.getOnBoardNobles());
    this.remainingNobles = Arrays.stream(saveGame.getRemainingNobles())
        .collect(Collectors.toMap(noble -> {
          try {
            return DigestUtils.md5Hex(objectMapper.writeValueAsString(noble));
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
        }, noble -> noble));
    this.remainingCities = new HashMap<>();
    this.onBoardCities = new Deck<>();
    if (winCondition == WinCondition.CITIES) {
      remainingCities.putAll(Arrays.stream(saveGame.getRemainingCities())
          .collect(Collectors.toMap(city -> {
            try {
              return DigestUtils.md5Hex(objectMapper.writeValueAsString(city));
            } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
            }
          }, city -> city)));
      onBoardCities = new Deck<>(saveGame.getOnBoardCities());
    }
    this.tradePosts = new HashMap<>();
    if (winCondition == WinCondition.TRADEROUTES) {
      for (RouteType route : RouteType.values()) {
        tradePosts.put(route, new TradePost(route));
      }
    }
  }

  /**
   * Creates a new game instance from a session payload.
   *
   * @param sessionId session id
   * @param payload   the payload
   * @return the game
   */
  @SneakyThrows
  public static Game create(long sessionId, SessionJson payload) {
    Game game = new Game(sessionId, payload);
    game.initDecks();
    game.initBroadcast();
    return game;
  }

  /**
   * Creates a new game instance from a savegame.
   *
   * @param sessionId session id
   * @param saveGame  the savegame
   * @param payload the session json
   * @return the game
   */
  public static Game create(long sessionId, SaveGame saveGame, SessionJson payload) {
    Game game = new Game(sessionId, saveGame, payload);
    game.initBroadcast();
    return game;
  }

  /**
   * Creates a new game instance.
   *
   * @param sessionId    session id
   * @param players      a non-null list of players
   * @param creator      the creator
   * @param savegame     the savegame
   * @param winCondition the win condition
   * @return the game
   */
  @SneakyThrows
  public static Game create(long sessionId, ServerPlayer[] players, String creator, String savegame,
                            WinCondition winCondition) {
    Game game = new Game(sessionId, players, creator, savegame, winCondition);
    game.initDecks();
    game.initBroadcast();
    return game;
  }

  private void initDecks() {
    GameInitHelpers helper = new GameInitHelpers(this);
    helper.createDecks();
    helper.createOnBoardDecks();
    helper.createOnBoardRedDecks();
  }

  private void initBroadcast() {
    this.broadcastContentManagerMap = new BroadcastMap(this);
  }

  /**
   * Gets current player.
   *
   * @return the current player
   */
  public ServerPlayer getCurrentPlayer() {
    return players[currentPlayerIndex];
  }

  /**
   * Get copy of array of players.
   *
   * @return a cloned copy of the internal array of players.
   */
  public ServerPlayer[] getPlayers() {
    return players.clone();
  }

  /**
   * Start next player's turn.
   */
  public void goToNextPlayer() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
  }

  /**
   * Gets card by hash.
   *
   * @param hash hash of the card
   * @return the card
   */
  public ServerLevelCard getCardByHash(String hash) {
    return hashToCardMap.get(hash);
  }

  /**
   * Gets noble by hash.
   *
   * @param hash hash of the noble
   * @return the noble
   */
  public ServerNoble getNobleByHash(String hash) {
    return remainingNobles.get(hash);
  }

  /**
   * Gets city by hash.
   *
   * @param hash hash of the city
   * @return the city
   */
  public ServerCity getCityByHash(String hash) {
    return remainingCities.get(hash);
  }

  /**
   * Gets deck.
   *
   * @param level deck level
   * @return the deck
   */
  public Deck<ServerLevelCard> getLevelDeck(Level level) {
    return remainingCards.get(level);
  }

  /**
   * Gets on board deck.
   *
   * @param level deck level
   * @return the deck
   */
  public Deck<ServerLevelCard> getOnBoardDeck(Level level) {
    return onBoardDecks.get(level);
  }


  /**
   * Adds a new card from deck to game board.
   *
   * @param level level of the deck
   */
  @SneakyThrows
  public void addOnBoardCard(Level level) {
    ServerLevelCard card = this.getLevelDeck(level).removeNextCard();
    card.setFaceDown(false);
    this.onBoardDecks.get(level).addCard(card);
    hashToCardMap.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
  }

  /**
   * Removes a card from game board.
   *
   * @param card card to be removed
   * @return if the card was in the deck.
   */
  public boolean removeOnBoardCard(ServerLevelCard card) {
    return onBoardDecks.get(card.getLevel()).removeCard(card);
  }


  /**
   * Checks if is player's turn.
   *
   * @param player player we want to check.
   * @return true if is player's turn, false otherwise
   */
  public boolean isNotPlayersTurn(ServerPlayer player) {
    return findPlayerIndex(player) != currentPlayerIndex;
  }

  /**
   * increments game bank by the amount specified by each parameter for each of their
   * corresponding gem types.
   *
   * @param rubyAmount     amount to increase ruby stack by.
   * @param emeraldAmount  amount to increase emerald stack by.
   * @param sapphireAmount amount to increase sapphire stack by.
   * @param diamondAmount  amount to increase diamond stack by.
   * @param onyxAmount     amount to increase onyx stack by.
   * @param goldAmount     amount to increase gold stack by.
   */
  public void incGameBank(int rubyAmount, int emeraldAmount, int sapphireAmount,
                          int diamondAmount, int onyxAmount, int goldAmount) {
    getGameBank().addGemsToBank(new PurchaseMap(rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount));
  }

  /**
   * Increments the game bank by the amount specified in the purchase map.
   *
   * @param purchaseMap purchase map representation of how much we want to increment
   *                    the game bank.
   */
  public void incGameBank(PurchaseMap purchaseMap) {
    getGameBank().addGemsToBank(purchaseMap);
  }

  /**
   * Decrements game bank by the amount specified by each parameter for each of their
   * corresponding gem types.
   *
   * @param rubyAmount     amount to decrease ruby stack by.
   * @param emeraldAmount  amount to decrease emerald stack by.
   * @param sapphireAmount amount to decrease sapphire stack by.
   * @param diamondAmount  amount to decrease diamond stack by.
   * @param onyxAmount     amount to decrease onyx stack by.
   * @param goldAmount     amount to decrease gold stack by.
   */
  public void decGameBank(int rubyAmount, int emeraldAmount, int sapphireAmount,
                          int diamondAmount, int onyxAmount, int goldAmount) {
    getGameBank().removeGemsFromBank(new PurchaseMap(rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount));
  }

  /**
   * Decrements the game bank by the amount specified in the purchase map.
   *
   * @param purchaseMap purchase map representation of how much we want to decrement
   *                    the game bank.
   */
  public void decGameBank(PurchaseMap purchaseMap) {
    getGameBank().removeGemsFromBank(purchaseMap);
  }

  /**
   * Increase game bank and decrease player bank by specified amount. (works the opposite for
   * negative number)
   *
   * @param player         player whose funds will get decreased.
   * @param rubyAmount     amount to increase ruby stack by.
   * @param emeraldAmount  amount to increase emerald stack by.
   * @param sapphireAmount amount to increase sapphire stack by.
   * @param diamondAmount  amount to increase diamond stack by.
   * @param onyxAmount     amount to increase onyx stack by.
   * @param goldAmount     amount to increase gold stack by.
   */
  public void incGameBankFromPlayer(ServerPlayer player, int rubyAmount, int emeraldAmount,
                                    int sapphireAmount, int diamondAmount, int onyxAmount,
                                    int goldAmount) {
    player.getBank().removeGemsFromBank(new PurchaseMap(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount));

    incGameBank(rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount);
  }


  /**
   * Checks if game bank has at least x amount of each gem type.
   *
   * @param rubyAmount     minimum amount or rubies player should have
   * @param emeraldAmount  minimum amount or emerald player should have
   * @param sapphireAmount minimum amount or sapphire player should have
   * @param diamondAmount  minimum amount or diamond player should have
   * @param onyxAmount     minimum amount or onyx player should have
   * @param goldAmount     minimum amount or gold player should have
   * @return true if bank has at least input amounts of each gem type, false otherwise.
   */
  public boolean gameBankHasAtLeast(int rubyAmount, int emeraldAmount, int sapphireAmount,
                                    int diamondAmount, int onyxAmount, int goldAmount) {
    return getGameBank().toPurchaseMap().canBeUsedToBuy(
        new PurchaseMap(rubyAmount, emeraldAmount, sapphireAmount,
            diamondAmount, onyxAmount, goldAmount));
  }

  /**
   * Gets all the token types one can take 2 of. (Gold gems are also part of the list
   * (shouldn't really be the case but just saying))
   *
   * @return An array list of all such token types
   */
  public ArrayList<Gem> availableTwoTokensType() {
    return getGameBank().availableTwoTokensType();
  }

  /**
   * Returns true if one can take 2 tokens of a given gem type. False otherwise.
   *
   * @param gem gem we want to take 2 of.
   * @return True if one can take 2 tokens of a given gem type. False otherwise.
   */
  public boolean allowedTakeTwoOf(Gem gem) {
    return availableTwoTokensType().contains(gem);
  }

  /**
   * Gives 2 tokens of type gem to player.
   *
   * @param gem    Gem we want to give 2 of.
   * @param player player who will receive the gems.
   */
  public void giveTwoOf(Gem gem, ServerPlayer player) {

    Map<Gem, Integer> gemIntegerMapGame = new HashMap<>();
    gemIntegerMapGame.put(gem, 2);
    gameBank.removeGemsFromBank(new PurchaseMap(gemIntegerMapGame));
    Map<Gem, Integer> gemIntegerMapPlayer = new HashMap<>();
    gemIntegerMapPlayer.put(gem, 2);
    player.incPlayerBank(new PurchaseMap(gemIntegerMapPlayer));
  }


  /**
   * Gets all the token types one can take 1 of.
   *
   * @return An array list of all such token types
   */
  public ArrayList<Gem> availableThreeTokensType() {
    return getGameBank().availableThreeTokensType();
  }

  /**
   * Returns true if one can take 1 token of the given gem types. False otherwise.
   *
   * @param gem first gem type we want.
   * @return True if one can take 1 token of the given gem types. False otherwise.
   */
  public boolean allowedTakeOneOf(Gem gem) {
    ArrayList<Gem> available = availableThreeTokensType();
    return available.contains(gem);
  }


  /**
   * Returns true if one can take 3 tokens of the given gem types. False otherwise.
   *
   * @param gem1 first gem type we want.
   * @param gem2 second gem type we want.
   * @param gem3 third gem type we want.
   * @return True if one can take 3 tokens of the given gem types. False otherwise.
   */
  public boolean allowedTakeThreeOf(Gem gem1, Gem gem2, Gem gem3) {
    ArrayList<Gem> available = availableThreeTokensType();
    return available.contains(gem1)
        && available.contains(gem2)
        && available.contains(gem3)
        && Gem.areDistinct(gem1, gem2, gem3);
  }

  /**
   * Gives 3 tokens of 3 different types to player.
   *
   * @param desiredGemOne   First gem we want to take one of.
   * @param desiredGemTwo   Second gem we want to take one of.
   * @param desiredGemThree Third gem we want to take one of.
   * @param player          player who will receive the gems.
   */
  public void giveThreeOf(Gem desiredGemOne, Gem desiredGemTwo, Gem desiredGemThree,
                          ServerPlayer player) {
    // Remove from game bank
    Map<Gem, Integer> gemIntegerMapGame = new HashMap<>();
    gemIntegerMapGame.put(desiredGemOne, 1);
    gemIntegerMapGame.put(desiredGemTwo, 1);
    gemIntegerMapGame.put(desiredGemThree, 1);
    gameBank.removeGemsFromBank(new PurchaseMap(gemIntegerMapGame));

    // Give to player bank
    Map<Gem, Integer> gemIntegerMapPlayer = new HashMap<>();
    gemIntegerMapPlayer.put(desiredGemOne, 1);
    gemIntegerMapPlayer.put(desiredGemTwo, 1);
    gemIntegerMapPlayer.put(desiredGemThree, 1);
    player.incPlayerBank(new PurchaseMap(gemIntegerMapPlayer));
  }

  /**
   * Gives 3 tokens of 3 different types to player.
   *
   * @param gem   First gem we want to take one of.
   * @param player          player who will receive the gems.
   */
  public void giveOneOf(Gem gem, ServerPlayer player) {
    // Remove from game bank
    Map<Gem, Integer> gemIntegerMapGame = new HashMap<>();
    gemIntegerMapGame.put(gem, 1);
    gameBank.removeGemsFromBank(new PurchaseMap(gemIntegerMapGame));

    // Give to player bank
    Map<Gem, Integer> gemIntegerMapPlayer = new HashMap<>();
    gemIntegerMapPlayer.put(gem, 1);
    player.incPlayerBank(new PurchaseMap(gemIntegerMapPlayer));
  }


  /**
   * takes back a token from the player.
   *
   * @param gem type of token game is taking back.
   * @param player player whose funds are being taken.
   */
  public void takeBackToken(Gem gem, ServerPlayer player) {
    Map<Gem, Integer> gemIntegerMapGame = new HashMap<>();
    gemIntegerMapGame.put(gem, 1);
    gameBank.addGemsToBank(new PurchaseMap(gemIntegerMapGame));
    Map<Gem, Integer> gemIntegerMapPlayer = new HashMap<>();
    gemIntegerMapPlayer.put(gem, 1);
    player.decPlayerBank(new PurchaseMap(gemIntegerMapPlayer));
  }


  // HELPERS ///////////////////////////////////////////////////////////////////////////////////////

  private int findPlayerIndex(ServerPlayer player) {
    int i = 0;
    for (ServerPlayer e : getPlayers()) {
      if (e == player) {
        return i;
      }
      i++;
    }
    return -1;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof final Game other)) {
      return false;
    }
    final Object this$remainingCards = this.getRemainingCards();
    final Object other$remainingCards = other.getRemainingCards();
    final Object this$onBoardDecks = this.getOnBoardDecks();
    final Object other$onBoardDecks = other.getOnBoardDecks();
    final Object this$onBoardNobles = this.getOnBoardNobles();
    final Object other$onBoardNobles = other.getOnBoardNobles();
    final Object this$onBoardCities = this.getOnBoardCities();
    final Object other$onBoardCities = other.getOnBoardCities();
    final Object this$remainingCities = this.getRemainingCities();
    final Object other$remainingCities = other.getRemainingCities();
    final Object this$gameBank = this.getGameBank();
    final Object other$gameBank = other.getGameBank();
    final Object this$winCondition = this.getWinCondition();
    final Object other$winCondition = other.getWinCondition();
    final Object this$remainingNobles = this.getRemainingNobles();
    final Object other$remainingNobles = other.getRemainingNobles();
    final Object this$tradePosts = this.getTradePosts();
    final Object other$tradePosts = other.getTradePosts();
    return Objects.equals(this$remainingCards, other$remainingCards)
        && Objects.equals(this$onBoardDecks, other$onBoardDecks)
        && Objects.equals(this$onBoardNobles, other$onBoardNobles)
        && Objects.equals(this$onBoardCities, other$onBoardCities)
        && Objects.equals(this$remainingCities, other$remainingCities)
        && Objects.equals(this$gameBank, other$gameBank)
        && Objects.equals(this$winCondition, other$winCondition)
        && Objects.equals(this$remainingNobles, other$remainingNobles)
        && Objects.equals(this$tradePosts, other$tradePosts)
        && this.getCurrentPlayerIndex() == other.getCurrentPlayerIndex();
  }
}
