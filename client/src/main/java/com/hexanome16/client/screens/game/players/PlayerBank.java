package com.hexanome16.client.screens.game.players;

import com.hexanome16.client.screens.game.prompts.components.prompttypes.BuyCardPrompt.CurrencyType;
import java.util.Hashtable;

/**
 * Player Bank for each player.
 */
public class PlayerBank {
  public Hashtable<CurrencyType, Integer> playerBank;

  /**
   * The constructor gives each player two tokens to start with.
   */
  public PlayerBank() {
    playerBank = new Hashtable<CurrencyType, Integer>();
    playerBank.put(CurrencyType.RED_TOKENS, 2);
    playerBank.put(CurrencyType.GREEN_TOKENS, 2);
    playerBank.put(CurrencyType.BLUE_TOKENS, 2);
    playerBank.put(CurrencyType.WHITE_TOKENS, 2);
    playerBank.put(CurrencyType.BLACK_TOKENS, 2);
    playerBank.put(CurrencyType.GOLD_TOKENS, 2);
  }

  /**
   * Allows the player to take two tokens of the same colour.
   *
   * @param tokenType = Type (ruby, diamond, etc) associated with the token.
   */
  public void acquireTokenSameColor(CurrencyType tokenType) {
    Integer current = playerBank.get(tokenType);
    playerBank.replace(tokenType, current + 2);
  }

  public void acquireTokenDiffColor(CurrencyType tokenType1, CurrencyType tokenType2, CurrencyType tokenType3) {
    Integer current1 = playerBank.get(tokenType1);
    Integer current2 = playerBank.get(tokenType2);
    Integer current3 = playerBank.get(tokenType3);
    playerBank.replace(tokenType1, current1 + 1);
    playerBank.replace(tokenType2, current2 + 1);
    playerBank.replace(tokenType3, current3 + 1);
  }

  public void purchaseCard() {

  }


}
