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
  //      ArrayList<Token> currentList = playerBank.get(gem);
  //      addTokensToList(currentList, Game.getGameMap().get(sessionId).
  //      playerBank.replace(gem, currentList);
  }

  /**
   * Allows the player to take three tokens of different colours.
   *
   * @param gem1 = Gem type (RUBY, DIAMOND, etc) associated with the token.
   * @param gem2 = Gem type (RUBY, DIAMOND, etc) associated with the token.
   * @param gem3 = Gem type (RUBY, DIAMOND, etc) associated with the token.
   */
  public void acquireTokenDiffColor(Gem gem1, Gem gem2, Gem gem3) {
  //        ArrayList<Token> currentList1 = playerBank.get(gem1);
  //        currentList1 = initBank(currentList1, gem1, 1);
  //        playerBank.replace(gem1, currentList1);
  //
  //        ArrayList<Token> currentList2 = playerBank.get(gem2);
  //        currentList2 = initBank(currentList2, gem2, 1);
  //        playerBank.replace(gem2, currentList2);
  //
  //        ArrayList<Token> currentList3 = playerBank.get(gem3);
  //        currentList3 = initBank(currentList3, gem3, 1);
  //        playerBank.replace(gem3, currentList3);
  }

  public void purchaseCard() {

  }

  @Override
  protected Hashtable<Gem, ArrayList<Token>> getBank() {
    return this.playerBank;
  }
}


