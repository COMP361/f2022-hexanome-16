package com.hexanome16.client.screens.game;

import com.almasb.fxgl.dsl.FXGL;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BuyCardPrompt;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Class with a bunch of methods to update various game information.
 */
public class UpdateGameInfo {


  /**
   * In any other Context than Prompts, update game variables.
   *
   * @param sessionId  unique session identifier.
   * @param playerName name of player whose bank will get updated.
   */
  public static void fetchPlayerBank(long sessionId, String playerName) {
    fetchPlayerBank(sessionId, playerName, false);
  }


  /**
   * Fetch player bank and game bank from server and sets those to be visible from the
   * UI, mainly used for prompt.
   *
   * @param sessionId    Unique session identifier.
   * @param playerName   name of player.
   * @param withinPrompt True if call within prompt, (doesn't update same properties),
   *                     false for any other context.
   */
  public static void fetchPlayerBank(long sessionId, String playerName, boolean withinPrompt) {

    // get string bank from server
    PurchaseMap bankPriceMap = PromptsRequests.getPlayerBank(sessionId, playerName);

    if (withinPrompt) {
      // set player info in the prompt to be whatever the server says
      setPlayerBankInfoPrompt(UpdateGameInfo.toGemAmountMap(bankPriceMap));
    } else {
      // set player info in the game to be whatever the server says.
      setPlayerBankInfoGlobal(playerName, UpdateGameInfo.toGemAmountMap(bankPriceMap));
    }
  }

  /**
   * Fetch game bank for that session, modifies game WorldProperty.
   *
   * @param sessionId session identification.
   */
  public static void fetchGameBank(long sessionId) {
    PurchaseMap bankPriceMap = PromptsRequests.getGameBankInfo(sessionId);
    setGameBank(sessionId, bankPriceMap);
  }

  /**
   * Updates Current player text at top right of screen.
   *
   * @param sessionId     unique session identifier for the game.
   * @param currentPlayer username for player we would like to set as new current player.
   */
  public static void setCurrentPlayer(long sessionId, String currentPlayer) {
    FXGL.getWorldProperties().setValue(
        sessionId + "/" + "currentPlayer", currentPlayer);
  }

  // Only related to prompt pwease don't move :3
  private static void setPlayerBankInfoPrompt(Map<CurrencyType, Integer> toGemAmountMap) {
    for (CurrencyType e : CurrencyType.values()) {
      FXGL.getWorldProperties()
          .setValue(BuyCardPrompt.BankType.PLAYER_BANK
              + "/" + e.toString(), toGemAmountMap.get(e));
    }
  }


  ////// Public Helper /////////////////////////////////////////////////////////////////////////////

  /**
   * Transforms String of bank retrieved from server to a Map .
   *
   * @param purchaseMap bank as PurchaseMap
   * @return Map mapping CurrencyType to amount of each currency type in bank
   */
  public static Map<CurrencyType, Integer> toGemAmountMap(PurchaseMap purchaseMap) {
    Map<CurrencyType, Integer> gemPlayerBank = new HashMap<>();

    // put each gem type with its value in the string
    gemPlayerBank.put(CurrencyType.RED_TOKENS, purchaseMap.getGemCost(Gem.RUBY));
    gemPlayerBank.put(CurrencyType.GREEN_TOKENS, purchaseMap.getGemCost(Gem.EMERALD));
    gemPlayerBank.put(CurrencyType.BLUE_TOKENS, purchaseMap.getGemCost(Gem.SAPPHIRE));
    gemPlayerBank.put(CurrencyType.WHITE_TOKENS, purchaseMap.getGemCost(Gem.DIAMOND));
    gemPlayerBank.put(CurrencyType.BLACK_TOKENS, purchaseMap.getGemCost(Gem.ONYX));
    gemPlayerBank.put(CurrencyType.GOLD_TOKENS, purchaseMap.getGemCost(Gem.GOLD));
    gemPlayerBank.put(CurrencyType.BONUS_GOLD_CARDS, 0);

    return gemPlayerBank;
  }

  /**
   * Allows to set the bank information for a specific player.
   *
   * @param playerName username of player we're fetching the information for.
   * @param playerInfo Map between Currency type and the new amount of each currency
   */
  public static void setPlayerBankInfoGlobal(String playerName,
                                             Map<CurrencyType, Integer> playerInfo) {
    for (CurrencyType e : CurrencyType.values()) {
      FXGL.getWorldProperties()
          .setValue(playerName + "/" + e.toString(), playerInfo.get(e));
    }
  }

  private static void setGameBank(long sessionId, PurchaseMap gameBankMap) {
    for (Map.Entry<Gem, Integer> gemEntry : gameBankMap.getPriceMap().entrySet()) {
      FXGL.getWorldProperties().setValue(sessionId + gemEntry.getKey().name(), gemEntry.getValue());
    }
  }


  /**
   * Initialize all tokens for all players.
   *
   * @param sessionId unique session identifier.
   * @param usernames all players in the game.
   */
  public static void fetchAllPlayer(long sessionId, String[] usernames) {
    for (String username : usernames) {
      fetchPlayerBank(sessionId, username);
    }
  }

  /**
   * To avoid null in that value when it is first asked for when it is bound.
   */
  public static void initPlayerTurn() {
    FXGL.getWorldProperties()
        .setValue(GameScreen.getSessionId() + "/" + "currentPlayer", "Initializing");

  }
}
