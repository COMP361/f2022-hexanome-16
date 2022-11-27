package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Player Bank for each player.
 */
public class PlayerBank extends Bank {
  public Hashtable<Gem, ArrayList<Token>> playerBank;

  /**
   * The constructor gives each player two tokens to start with.
   */
  public PlayerBank() {
    playerBank = new Hashtable<Gem, ArrayList<Token>>();

    playerBank.put(Gem.RUBY, addTokenToList(null, Gem.RUBY, 2));
    playerBank.put(Gem.EMERALD, addTokenToList(null, Gem.EMERALD, 2));
    playerBank.put(Gem.SAPPHIRE, addTokenToList(null, Gem.SAPPHIRE, 2));
    playerBank.put(Gem.DIAMOND, addTokenToList(null, Gem.DIAMOND, 2));
    playerBank.put(Gem.ONYX, addTokenToList(null, Gem.ONYX, 2));
    playerBank.put(Gem.GOLD, addTokenToList(null, Gem.GOLD, 2));

  }

  /**
   * Allows the player to take two tokens of the same colour.
   *
   * @param gem = Gem type (RUBY, DIAMOND, etc) associated with the token.
   */
  public void acquireTokenSameColor(Gem gem) {
    ArrayList<Token> currentList = playerBank.get(gem);
    currentList = addTokenToList(currentList, gem, 2);
    playerBank.replace(gem, currentList);
  }

  /**
   * Allows the player to take three tokens of different colours.
   *
   * @param gem1 = Gem type (RUBY, DIAMOND, etc) associated with the token.
   * @param gem2 = Gem type (RUBY, DIAMOND, etc) associated with the token.
   * @param gem3 = Gem type (RUBY, DIAMOND, etc) associated with the token.
   */
  public void acquireTokenDiffColor(Gem gem1, Gem gem2, Gem gem3) {
    ArrayList<Token> currentList1 = playerBank.get(gem1);
    currentList1 = addTokenToList(currentList1, gem1, 1);
    ArrayList<Token> currentList2 = playerBank.get(gem2);
    currentList2 = addTokenToList(currentList2, gem2, 1);
    ArrayList<Token> currentList3 = playerBank.get(gem3);
    currentList3 = addTokenToList(currentList3, gem3, 1);
    playerBank.replace(gem1, currentList1);
    playerBank.replace(gem2, currentList2);
    playerBank.replace(gem3, currentList3);
  }

  public void purchaseCard() {

  }

  private ArrayList<Token> addTokenToList(ArrayList<Token> list, Gem gem, int amount) {
    // Initialize an ArrayList if no list was provided
    if (list == null) {
      list = new ArrayList<>();
    }
    for (int i = 0; i < amount; i++) {
      list.add(new Token(gem));
    }
    return list;
  }

}
