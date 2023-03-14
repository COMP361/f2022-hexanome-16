package com.hexanome16.server.models.game;

import static com.hexanome16.server.models.game.GameInitHelpers.createBoardMap;
import static com.hexanome16.server.models.game.GameInitHelpers.createDecks;
import static com.hexanome16.server.models.game.GameInitHelpers.createLevelMap;
import static com.hexanome16.server.models.game.GameInitHelpers.createOnBoardDecks;
import static com.hexanome16.server.models.game.GameInitHelpers.createOnBoardRedDecks;
import static com.hexanome16.server.models.game.GameInitHelpers.createRedMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.TradePost;
import com.hexanome16.server.models.bank.GameBank;
import com.hexanome16.server.models.cards.Deck;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.util.broadcastmap.BroadcastMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private final Map<Level, Deck<ServerLevelCard>> levelDecks;
  private final Map<Level, Deck<ServerLevelCard>> redDecks;
  private final Map<Level, Deck<ServerLevelCard>> onBoardDecks;
  private final long sessionId;
  private final ServerPlayer[] players;
  private final String creator;
  private final String savegame;
  private final GameBank gameBank;
  private final WinCondition[] winConditions;
  private final Map<String, ServerLevelCard> remainingCards;
  /**
   * Remaining nobles to be acquired.
   */
  private final Map<String, ServerNoble> remainingNobles;
  private final Map<RouteType, TradePost> tradePosts;
  private BroadcastMap broadcastContentManagerMap;
  private int currentPlayerIndex = 0;
  /**
   * Deck of all possible nobles.
   */
  Deck<ServerNoble> nobleDeck;
  /**
   * Selected nobles for this game (5 of the 10 possible).
   */
  Deck<ServerNoble> onBoardNobles;

  /**
   * Game constructor, create a new with a unique session id.
   *
   * @param sessionId     session id
   * @param players       a non-null list of players
   * @param creator       the creator
   * @param savegame      the savegame
   * @param winConditions the win conditions
   */
  @SneakyThrows
  Game(long sessionId, @NonNull ServerPlayer[] players, String creator, String savegame,
       WinCondition[] winConditions, boolean isTradeRoute, boolean isCities) {
    this.sessionId = sessionId;
    this.players = players.clone();
    this.creator = creator;
    this.savegame = savegame;
    this.winConditions = winConditions;
    this.gameBank = new GameBank();
    this.levelDecks = createLevelMap();
    this.redDecks = createRedMap();
    this.onBoardDecks = createBoardMap();
    this.nobleDeck = new Deck<>();
    this.onBoardNobles = new Deck<>();
    this.remainingCards = new HashMap<>();
    this.remainingNobles = new HashMap<>();
    this.tradePosts = new HashMap<>();
    if (isTradeRoute) {
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
    this(sessionId, Arrays.stream(payload.getPlayers()).map(player -> new ServerPlayer(
            player.getName(), player.getPreferredColour())).toArray(ServerPlayer[]::new),
        payload.getCreator(), payload.getSavegame(),
        new WinCondition[] {WinCondition.fromServerName(payload.getGame())},
        payload.getGame().contains("TradeRoutes"), payload.getGame().contains("Cities"));
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
    System.out.println(payload);
    Game game = new Game(sessionId, payload);
    game.init();
    return game;
  }

  /**
   * Creates a new game instance.
   *
   * @param sessionId     session id
   * @param players       a non-null list of players
   * @param creator       the creator
   * @param savegame      the savegame
   * @param winConditions the win conditions
   * @param isTradeRoute  if trade route expansion
   * @param isCities      if cities expansion
   * @return the game
   */
  @SneakyThrows
  public static Game create(long sessionId, ServerPlayer[] players, String creator, String savegame,
                            WinCondition[] winConditions, boolean isTradeRoute, boolean isCities) {
    Game game =
        new Game(sessionId, players, creator, savegame, winConditions, isTradeRoute, isCities);
    game.init();
    return game;
  }

  @SneakyThrows
  private void init() {
    createDecks(this);
    createOnBoardDecks(this);
    createOnBoardRedDecks(this);
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
    return remainingCards.get(hash);
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
   * Gets deck.
   *
   * @param level deck level
   * @return the deck
   */
  public Deck<ServerLevelCard> getLevelDeck(Level level) {
    return switch (level) {
      case ONE, TWO, THREE -> levelDecks.get(level);
      case REDONE, REDTWO, REDTHREE -> redDecks.get(level);
    };
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
    remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
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
   * Gets all the token types one can take 2 of.
   *
   * @return An array list of all such token types
   */
  public ArrayList<Gem> availableThreeTokensType() {
    return getGameBank().availableThreeTokensType();
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


}