package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Stream;

/**
 * Game Bank for the whole game.
 */
public class GameBank extends Bank {

  private final Hashtable<Gem, ArrayList<Token>> gameBank;

  /**
   * The game starts with the default amount of tokens.
   */
  public GameBank() {
    gameBank = initBank(7);
  }

  @Override
  protected Hashtable<Gem, ArrayList<Token>> getBank() {
    return this.gameBank;
  }


  /**
   * Gets all the token types one can take 2 of.
   *
   * @return a list of all the token types you can take 2 of.
   */
  public ArrayList<Gem> availableTwoTokensType() {
    ArrayList<Gem> myList = new ArrayList<>(List.of(Gem.values()));
    Stream<Gem> myStream = myList.stream().filter(gem -> getBank().get(gem).size() >= 4);
    return new ArrayList<>(myStream.toList());
  }

  /**
   * Gets all the token types one can take 3 of.
   *
   * @return a list of all the token types you can take 3 of.
   */
  public ArrayList<Gem> availableThreeTokensType() {
    ArrayList<Gem> myList = new ArrayList<>(List.of(Gem.values()));
    Stream<Gem> myStream = myList.stream().filter(gem -> getBank().get(gem).size() > 0);
    return new ArrayList<>(myStream.toList());
  }

}
