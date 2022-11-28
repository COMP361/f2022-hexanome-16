package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Player Bank for each player.
 */
public class PlayerBank extends Bank {
  private final Hashtable<Gem, ArrayList<Token>> playerBank;

  /**
   * The constructor gives each player two tokens to start with.
   */
  public PlayerBank() {
    playerBank = initBank(3);
  }

  /**
   * Allows the player to take two tokens of the same colour.
   *
   * @param gem = Gem type (RUBY, DIAMOND, etc) associated with the token.
   */
  public void acquireTokenSameColor(Gem gem) {
//    ArrayList<Token> currentList = playerBank.get(gem);
//    addTokensToList(currentList, Game.getGameMap().get(sessionId).
//    playerBank.replace(gem, currentList);
  }

  /**
   * Allows the player to take three tokens of different colours.
   *
   * @param gem1 = Gem type (RUBY, DIAMOND, etc) associated with the token.
   * @param gem2 = Gem type (RUBY, DIAMOND, etc) associated with the token.
   * @param gem3 = Gem type (RUBY, DIAMOND, etc) associated with the token.
   */
  public void acquireTokenDiffColor(Gem gem1, Gem gem2, Gem gem3) {
//    ArrayList<Token> currentList1 = playerBank.get(gem1);
//    currentList1 = initBank(currentList1, gem1, 1);
//    playerBank.replace(gem1, currentList1);
//
//    ArrayList<Token> currentList2 = playerBank.get(gem2);
//    currentList2 = initBank(currentList2, gem2, 1);
//    playerBank.replace(gem2, currentList2);
//
//    ArrayList<Token> currentList3 = playerBank.get(gem3);
//    currentList3 = initBank(currentList3, gem3, 1);
//    playerBank.replace(gem3, currentList3);
  }

  public void purchaseCard() {

  }

  public Hashtable<Gem, ArrayList<Token>> getPlayerBank() {
    return this.playerBank;
  }



  // TODO TEST
  /**
   * increments Player bank by the amount specified by each parameter for each of their
   * corresponding gem types.
   *
   * @param rubyAmount amount to increase ruby stack by.
   * @param emeraldAmount amount to increase emerald stack by.
   * @param sapphireAmount amount to increase sapphire stack by.
   * @param diamondAmount amount to increase diamond stack by.
   * @param onyxAmount amount to increase onyx stack by.
   * @param goldAmount amount to increase gold stack by.
   */
  public void incPlayerBank(int rubyAmount, int emeraldAmount, int sapphireAmount,
                            int diamondAmount, int onyxAmount, int goldAmount) {

    for (Gem e : Gem.values()) {
      TokenStack tokenStackForGem = tokenStackMap.get(e);
      switch (e) {
        case RUBY -> {
          incSingularTokenStack(tokenStackForGem, rubyAmount);
        }
        case EMERALD -> {
          incSingularTokenStack(tokenStackForGem, emeraldAmount);
        }
        case SAPPHIRE -> {
          incSingularTokenStack(tokenStackForGem, sapphireAmount);
        }
        case DIAMOND -> {
          incSingularTokenStack(tokenStackForGem, diamondAmount);
        }
        case ONYX -> {
          incSingularTokenStack(tokenStackForGem, onyxAmount);
        }
        case GOLD -> {
          incSingularTokenStack(tokenStackForGem, goldAmount);
        }
        default -> {
          continue;
        }
      }
    }
  }

  // may have off by one error
  private void incSingularTokenStack(TokenStack tokenStack, int incrementAmount) {
    if (incrementAmount > 0) {
      for (; incrementAmount != 0; incrementAmount--) {
        Token newToken = new Token();
        newToken.gem = tokenStack.getGem();
        tokenStack.addToken(newToken);
      }
    } else if (incrementAmount < 0) {
      for (; incrementAmount != 0 && !tokenStack.isEmpty(); incrementAmount++) {
        tokenStack.removeToken();
      }
    }
  }

  // TODO: TEST CASE
  /**
   * Returns true if bank has at least specified amounts of each gem type in their bank, false
   * otherwise.
   *
   * @param rubyAmount minimum amount or rubies player should have
   * @param emeraldAmount minimum amount or emerald player should have
   * @param sapphireAmount minimum amount or sapphire player should have
   * @param diamondAmount minimum amount or diamond player should have
   * @param onyxAmount minimum amount or onyx player should have
   * @param goldAmount minimum amount or gold player should have
   * @return true if bank has at least input amounts of each gem type, false otherwise.
   */
  public boolean hasAtLeast(int rubyAmount, int emeraldAmount, int sapphireAmount,
                            int diamondAmount, int onyxAmount, int goldAmount) {
    boolean checkResult = true;

    for (Gem e : Gem.values()) {
      TokenStack tokenStackForGem = tokenStackMap.get(e);
      switch (e) {
        case RUBY -> {
          checkResult = checkResult && tokenStackMap.size() <= rubyAmount;
        }
        case EMERALD -> {
          checkResult = checkResult && tokenStackMap.size() <= emeraldAmount;
        }
        case SAPPHIRE -> {
          checkResult = checkResult && tokenStackMap.size() <= sapphireAmount;
        }
        case DIAMOND -> {
          checkResult = checkResult && tokenStackMap.size() <= diamondAmount;
        }
        case ONYX -> {
          checkResult = checkResult && tokenStackMap.size() <= onyxAmount;
        }
        case GOLD -> {
          checkResult = checkResult && tokenStackMap.size() <= goldAmount;
        }
        default -> {
          continue;
        }
      }
    }

    return checkResult;
  }
}


