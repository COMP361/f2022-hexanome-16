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

  /**
   * Add tokens to list.
   *
   * @param list  the list to be added to
   * @param toAdd the list of tokens to add
   */
  void addTokensToList(ArrayList<Token> list, ArrayList<Token> toAdd) {
    for (int i = 0; i < toAdd.size(); i++) {
      list.add(toAdd.remove(i));
      i--;
    }
  }

  /**
   * Remove tokens from list array list.
   *
   * @param list   the list to be removed from
   * @param amount the amount of tokens to remove
   * @return the array list
   */
  ArrayList<Token> removeTokensFromList(ArrayList<Token> list, int amount) {
    ArrayList<Token> removed = new ArrayList<>();


    for (int i = 0; i < amount && !list.isEmpty(); i++) {
      removed.add(list.remove(list.size() - 1));
    }

    return removed;
  }

  /**
   * gets bank. - Imad
   *
   * @return bank. - Imad
   */
  protected abstract Hashtable<Gem, ArrayList<Token>> getBank();



  /**
   * increments Player bank by the amount specified by each parameter for each of their
   * corresponding gem types.
   *
   * @param rubyAmount     amount to increase ruby stack by.
   * @param emeraldAmount  amount to increase emerald stack by.
   * @param sapphireAmount amount to increase sapphire stack by.
   * @param diamondAmount  amount to increase diamond stack by.
   * @param onyxAmount     amount to increase onyx stack by.
   * @param goldAmount     amount to increase gold stack by.
   */
  public void incBank(int rubyAmount, int emeraldAmount, int sapphireAmount,
                      int diamondAmount, int onyxAmount, int goldAmount) {

    // Each gem type has its own amount associated to it
    // (I don't like how this looks neither)
    for (Gem e : Gem.values()) {
      // get list associated to that gem
      ArrayList<Token> tokensForGem = getBank().get(e);

      switch (e) {
        case RUBY -> incSingularTokenList(e, tokensForGem, rubyAmount);
        case EMERALD -> incSingularTokenList(e, tokensForGem, emeraldAmount);
        case SAPPHIRE -> incSingularTokenList(e, tokensForGem, sapphireAmount);
        case DIAMOND -> incSingularTokenList(e, tokensForGem, diamondAmount);
        case ONYX -> incSingularTokenList(e, tokensForGem, onyxAmount);
        case GOLD -> incSingularTokenList(e, tokensForGem, goldAmount);
        default -> {
        }
      }
    }
  }

  // may have off by one error, helper method to incBank
  private void incSingularTokenList(Gem gemType, ArrayList<Token> tokenList, int incrementAmount) {


    // if number is positive, add tokens to list
    if (incrementAmount > 0) {
      ArrayList<Token> additionalTokens = new ArrayList<>();
      for (; incrementAmount != 0; incrementAmount--) {
        additionalTokens.add(new Token(gemType));
      }
      addTokensToList(tokenList, additionalTokens);

      // else remove tokens from list
    } else if (incrementAmount < 0) {
      ArrayList<Token> removedTokens = removeTokensFromList(tokenList, -incrementAmount);
    }
  }


  /**
   * Returns true if bank has at least specified amounts of each gem type in their bank, false
   * otherwise.
   *
   * @param rubyAmount     minimum amount or rubies player should have
   * @param emeraldAmount  minimum amount or emerald player should have
   * @param sapphireAmount minimum amount or sapphire player should have
   * @param diamondAmount  minimum amount or diamond player should have
   * @param onyxAmount     minimum amount or onyx player should have
   * @param goldAmount     minimum amount or gold player should have
   * @return true if bank has at least input amounts of each gem type, false otherwise.
   */
  public boolean hasAtLeast(int rubyAmount, int emeraldAmount, int sapphireAmount,
                            int diamondAmount, int onyxAmount, int goldAmount) {
    boolean checkResult = true;

    // goes through every gem type and makes sure bank contains at least that amount
    // each
    for (Gem e : Gem.values()) {
      ArrayList<Token> tokenListForGem = getBank().get(e);
      switch (e) {
        case RUBY -> checkResult = checkResult && tokenListForGem.size() >= rubyAmount;
        case EMERALD -> checkResult = checkResult && tokenListForGem.size() >= emeraldAmount;
        case SAPPHIRE -> checkResult = checkResult && tokenListForGem.size() >= sapphireAmount;
        case DIAMOND -> checkResult = checkResult && tokenListForGem.size() >= diamondAmount;
        case ONYX -> checkResult = checkResult && tokenListForGem.size() >= onyxAmount;
        case GOLD -> checkResult = checkResult && tokenListForGem.size() >= goldAmount;
        default -> throw new IllegalArgumentException("Something is sussy here");
      }
    }

    return checkResult;
  }

  /**
   * Returns the bank as if it was a Purchase Map. (Use for controllers, to
   * send a bank as a string)
   *
   * @return Purchase map of what the bank looks like.
   */
  public PurchaseMap toPurchaseMap() {
    return new PurchaseMap(getBank().get(Gem.RUBY).size(),
        getBank().get(Gem.EMERALD).size(),
        getBank().get(Gem.SAPPHIRE).size(),
        getBank().get(Gem.DIAMOND).size(),
        getBank().get(Gem.ONYX).size(),
        getBank().get(Gem.GOLD).size());
  }


  /**
   * String representation of a bank.
   */
  @Override
  public String toString() {
    StringBuilder playerBankRepresentation = new StringBuilder();

    playerBankRepresentation.append("---------------------------------\n");
    playerBankRepresentation.append(this.getClass().toString());
    playerBankRepresentation.append(" Bank contains : \n");

    for (Gem e : Gem.values()) {
      playerBankRepresentation.append(e).append(" : ")
          .append(getBank().get(e).size()).append("\n");
    }

    playerBankRepresentation.append("---------------------------------\n");

    return playerBankRepresentation.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj.getClass() != this.getClass()) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    Bank otherBank = (Bank) obj;
    return this.getBank().equals(otherBank.getBank());
  }

  @Override
  public int hashCode() {
    return this.getBank().hashCode();
  }
}
