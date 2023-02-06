package com.hexanome16.server.models.bank;

import com.hexanome16.server.models.price.Gem;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Stream;

/**
 * Game Bank for the whole game.
 */
public class GameBank extends Bank {

  /**
   * The game starts with the default amount of tokens (7).
   */
  public GameBank() {
    super(7);
  }

  /**
   * Gets all the token types one can take 2 of.
   *
   * @return a list of all the token types you can take 2 of.
   */
  public ArrayList<Gem> availableTwoTokensType() {
    ArrayList<Gem> myList = new ArrayList<>(List.of(Gem.values()));
    Stream<Gem> myStream = myList.stream().filter(gem -> getBank().get(gem) >= 4);
    return new ArrayList<>(myStream.toList());
  }

  /**
   * Gets all the token types one can take 3 of.
   *
   * @return a list of all the token types you can take 3 of.
   */
  public ArrayList<Gem> availableThreeTokensType() {
    ArrayList<Gem> myList = new ArrayList<>(List.of(Gem.values()));
    Stream<Gem> myStream = myList.stream().filter(gem -> getBank().get(gem) > 0);
    return new ArrayList<>(myStream.toList());
  }

}
