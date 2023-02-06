package com.hexanome16.server.models.bank;

import com.hexanome16.server.models.price.Gem;
import com.hexanome16.server.models.price.PriceInterface;
import com.hexanome16.server.models.price.PurchaseMap;
import java.util.Hashtable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Bank class. Used for storing tokens.
 */
@ToString
@EqualsAndHashCode
public abstract class Bank implements BankInterface {
  @Getter
  private final Hashtable<Gem, Integer> bank;

  /**
   * The constructor that initializes the bank with given amount of tokens to start with.
   *
   * @param initAmount the amount of tokens to start with
   */
  protected Bank(int initAmount) {
    bank = new Hashtable<>(Gem.values().length);
    for (Gem gem : Gem.values()) {
      bank.put(gem, gem == Gem.GOLD && initAmount > 5 ? 5 : initAmount);
    }
  }

  @Override
  public void addGemsToBank(Gem gem, int amount) {
    bank.put(gem, bank.getOrDefault(gem, 0) + amount);
  }

  @Override
  public void addGemsToBank(PriceInterface gems) {
    for (Gem gem : Gem.values()) {
      addGemsToBank(gem, gems.getGemCost(gem));
    }
  }

  @Override
  public void removeGemsFromBank(Gem gem, int amount) {
    int currentAmount = bank.getOrDefault(gem, 0);
    if (amount > currentAmount) {
      throw new IllegalArgumentException("Cannot remove more gems than are in the bank");
    }
    bank.put(gem, currentAmount - amount);
  }

  @Override
  public void removeGemsFromBank(PriceInterface gems) {
    for (Gem gem : Gem.values()) {
      removeGemsFromBank(gem, gems.getGemCost(gem));
    }
  }

  @Override
  public PurchaseMap toPurchaseMap() {
    return new PurchaseMap(this.bank);
  }
}
