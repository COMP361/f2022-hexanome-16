package com.hexanome16.server.models.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;

/**
 * Player Bank for each player.
 */
public class PlayerBank extends Bank {

  /**
   * The constructor gives each player three tokens to start with.
   */
  public PlayerBank() {
    super(3);
  }

  /**
   * The constructor gives each player the given amount of tokens (used by savegame).
   *
   * @param initMap the amount of tokens to start with
   */
  public PlayerBank(PurchaseMap initMap) {
    super(initMap);
  }

  /**
   * true if bank has more than K tokens, false otherwise.
   *
   * @param k int of max size (return false if k)
   * @return true or false.
   */
  @JsonIgnore
  public boolean hasMoreThanKtokens(int k) {
    return getBank().values().stream().mapToInt(e -> e).sum() > k;
  }

  /**
   * gets owned token types.
   *
   * @return array of owned token types.
   */
  @JsonIgnore
  public Gem[] getOwnedTokenTypes() {
    return getBank().keySet().stream().filter(gem -> getBank().get(gem) > 0).toArray(Gem[]::new);
  }
}


