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
      ArrayList<Token> tokenStackForGem = playerBank.get(e);
      switch (e) {
        case RUBY -> {
          incSingularTokenStack(e, tokenStackForGem, rubyAmount);
        }
        case EMERALD -> {
          incSingularTokenStack(e, tokenStackForGem, emeraldAmount);
        }
        case SAPPHIRE -> {
          incSingularTokenStack(e, tokenStackForGem, sapphireAmount);
        }
        case DIAMOND -> {
          incSingularTokenStack(e, tokenStackForGem, diamondAmount);
        }
        case ONYX -> {
          incSingularTokenStack(e, tokenStackForGem, onyxAmount);
        }
        case GOLD -> {
          incSingularTokenStack(e, tokenStackForGem, goldAmount);
        }
        default -> {
          continue;
        }
      }
    }
  }

  // may have off by one error
  private void incSingularTokenStack(Gem gemType, ArrayList<Token> tokenList, int incrementAmount) {
    if (incrementAmount > 0) {
      ArrayList<Token> additionalTokens = new ArrayList<>();
      for (; incrementAmount != 0; incrementAmount--) {
        Token newToken = new Token(gemType);
        additionalTokens.add(new Token(gemType));
      }
      addTokensToList(tokenList, additionalTokens);
    } else if (incrementAmount < 0) {
      for (; incrementAmount != 0 && !tokenList.isEmpty(); incrementAmount++) {
        removeTokenFromList(tokenList, 1);
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

    // goes through every gem type and makes sure bank contains at least that amount
    // each
    for (Gem e : Gem.values()) {
      ArrayList<Token> tokenListForGem = playerBank.get(e);
      switch (e) {
        case RUBY -> {
          checkResult = checkResult && tokenListForGem.size() >= rubyAmount;
        }
        case EMERALD -> {
          checkResult = checkResult && tokenListForGem.size() >= emeraldAmount;
        }
        case SAPPHIRE -> {
          checkResult = checkResult && tokenListForGem.size() >= sapphireAmount;
        }
        case DIAMOND -> {
          checkResult = checkResult && tokenListForGem.size() >= diamondAmount;
        }
        case ONYX -> {
          checkResult = checkResult && tokenListForGem.size() >= onyxAmount;
        }
        case GOLD -> {
          checkResult = checkResult && tokenListForGem.size() >= goldAmount;
        }
        default -> {
          continue;
        }
      }
    }

    return checkResult;
  }

  /**
   * Returns the player bank as if it was a Purchase Map.
   *
   * @return Purchase map of what the player bank looks like.
   */
  public PurchaseMap toPurchaseMap() {
    return new PurchaseMap(playerBank.get(Gem.RUBY).size(),
        playerBank.get(Gem.EMERALD).size(),
        playerBank.get(Gem.SAPPHIRE).size(),
        playerBank.get(Gem.DIAMOND).size(),
        playerBank.get(Gem.ONYX).size(),
        playerBank.get(Gem.GOLD).size());
  }


  /**
   * String representation of a player bank.
   *
   * @return a String of the bank.
   */
  @Override
  public String toString() {
    StringBuilder playerBankRepresentation = new StringBuilder();

    playerBankRepresentation.append("---------------------------------\n");
    playerBankRepresentation.append("Player Bank contains : \n");

    for (Gem e : Gem.values()) {
      playerBankRepresentation.append(e).append(" : ")
          .append(getPlayerBank().get(e).size()).append("\n");
    }

    playerBankRepresentation.append("---------------------------------\n");

    return playerBankRepresentation.toString();
  }
}


