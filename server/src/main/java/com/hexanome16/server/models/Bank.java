package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Bank class. Used for storing tokens.
 */
public abstract class Bank {
  //  public abstract void acquireTokenSameColor(Gem gem);
  //
  //  public abstract void acquireTokenDiffColor(Gem gem1, Gem gem2, Gem gem3);

  /**
   * Initializes the bank.
   *
   * @param initAmount = default amount of tokens.
   * @return the bank.
   */
  Hashtable<Gem, ArrayList<Token>> initBank(int initAmount) {
    Hashtable<Gem, ArrayList<Token>> bank = new Hashtable<Gem, ArrayList<Token>>();
    ArrayList<Token> rubyList = new ArrayList<>();
    ArrayList<Token> emeraldList = new ArrayList<>();
    ArrayList<Token> sapphireList = new ArrayList<>();
    ArrayList<Token> diamondList = new ArrayList<>();
    ArrayList<Token> onyxList = new ArrayList<>();
    ArrayList<Token> goldList = new ArrayList<>();

    for (int i = 0; i < initAmount; i++) {
      rubyList.add(new Token(Gem.RUBY));
      emeraldList.add(new Token(Gem.EMERALD));
      sapphireList.add(new Token(Gem.SAPPHIRE));
      diamondList.add(new Token(Gem.DIAMOND));
      onyxList.add(new Token(Gem.ONYX));
      goldList.add(new Token(Gem.GOLD));
      if (i < 5) {
        goldList.add(new Token(Gem.GOLD));
      }
    }

    bank.put(Gem.RUBY, rubyList);
    bank.put(Gem.EMERALD, emeraldList);
    bank.put(Gem.SAPPHIRE, sapphireList);
    bank.put(Gem.DIAMOND, diamondList);
    bank.put(Gem.ONYX, onyxList);
    bank.put(Gem.GOLD, goldList);

    return bank;
  }

  void addTokensToList(ArrayList<Token> list, ArrayList<Token> toAdd) {
    for (int i = 0; i < toAdd.size(); i++) {
      list.add(toAdd.remove(i));
      i--;
    }
  }

  ArrayList<Token> removeTokenFromList(ArrayList<Token> list, int amount) {
    ArrayList<Token> removed = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      removed.add(list.remove(0));
    }
    return removed;
  }

}
