package com.hexanome16.server.models.price;

import static java.util.Objects.hash;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.server.util.deserializers.PurchaseMapDeserializer;
import java.util.Hashtable;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;


/**
 * Class responsible for representing the gems the player decided to put down
 * to buy a card.
 */
@Getter
@JsonDeserialize(using = PurchaseMapDeserializer.class)
public class PurchaseMap extends PriceMap implements PriceInterface {

  /**
   * Instantiates a new Purchase map.
   *
   * @param rubyAmount     the ruby amount
   * @param emeraldAmount  the emerald amount
   * @param sapphireAmount the sapphire amount
   * @param diamondAmount  the diamond amount
   * @param onyxAmount     the onyx amount
   * @param goldAmount     the gold amount
   */
  public PurchaseMap(int rubyAmount, int emeraldAmount, int sapphireAmount, int diamondAmount,
                     int onyxAmount, int goldAmount) {
    super(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount, onyxAmount);
    this.priceMap.put(Gem.GOLD, goldAmount);
  }

  /**
   * Alternative constructor to purchase map, takes a Map from Gems to Integer instead.
   *
   * @param purchaseMap A map between each gem and their corresponding amount as an
   *                    Integer.
   */
  public PurchaseMap(Map<Gem, Integer> purchaseMap) {
    this.priceMap = new Hashtable<>();
    if (purchaseMap == null) {
      throw new IllegalArgumentException("Price map cannot be null");
    }
    for (Map.Entry<Gem, Integer> entry : purchaseMap.entrySet()) {
      if (entry.getValue() == null) {
        throw new IllegalArgumentException("Price map cannot contain null values");
      }
      this.addGems(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Alternative constructor to purchase map, takes a PriceMap instead.
   *
   * @param priceMap   A map between each gem and their corresponding amount as an
   *                   Integer.
   * @param goldAmount The amount of gold to add.
   */
  public PurchaseMap(PriceMap priceMap, int goldAmount) {
    this(priceMap.priceMap);
    this.priceMap.put(Gem.GOLD, goldAmount);
  }

  /**
   * Makes a purchase map out of the input price map, sets gold amount in the new purchase
   * map to be 0.
   *
   * @param paPriceMap PriceMap we want to transform.
   * @return a PurchaseMap with the same amount of each gem as the inputted price map.
   */
  public static PurchaseMap toPurchaseMap(PriceInterface paPriceMap) {
    return new PurchaseMap(paPriceMap.getGemCost(Gem.RUBY),
        paPriceMap.getGemCost(Gem.EMERALD),
        paPriceMap.getGemCost(Gem.SAPPHIRE),
        paPriceMap.getGemCost(Gem.DIAMOND),
        paPriceMap.getGemCost(Gem.ONYX),
        paPriceMap.getGemCost(Gem.GOLD));
  }

  /**
   * Sum of non-gold tokens.
   *
   * @return total amount
   */
  public int getTotalGemsNonJokers() {
    return this.getTotalGems() - this.getGemCost(Gem.GOLD);
  }

  @Override
  public void addGems(Gem gem, int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Cannot add negative amount of gems");
    }
    priceMap.put(gem, priceMap.getOrDefault(gem, 0) + amount);
  }

  @Override
  public void removeGems(Gem gem, int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Cannot remove negative amount of gems");
    }
    int baseAmount = priceMap.getOrDefault(gem, 0);
    if (baseAmount < amount) {
      throw new IllegalArgumentException("Cannot remove more gems than are present");
    }
    priceMap.put(gem, priceMap.getOrDefault(gem, 0) - amount);
  }

  // TODO: TEST CASE

  /**
   * Checks if implied argument can be used instead of the parameter to commit a purchase.
   *
   * @param otherPriceMap priceMap we want to compare to.
   * @return true if the implied can be used to commit the purchase, false otherwise.
   */
  public boolean canBeUsedToBuy(PriceInterface otherPriceMap) {
    if (otherPriceMap == null) {
      return false;
    }
    if (this == otherPriceMap) {
      return true;
    }
    int goldAmount = this.getGemCost(Gem.GOLD);
    for (Gem gem : Gem.values()) {
      if (this.getGemCost(gem) < otherPriceMap.getGemCost(gem)) {
        goldAmount -= otherPriceMap.getGemCost(gem) - this.getGemCost(gem);
        if (goldAmount < otherPriceMap.getGemCost(Gem.GOLD)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean equals(Object otherPriceMap) {
    if (otherPriceMap == null) {
      return false;
    }
    if (this == otherPriceMap) {
      return true;
    }
    if (this.getClass() != otherPriceMap.getClass()) {
      return false;
    }
    PurchaseMap confirmedOtherPriceMap = (PurchaseMap) otherPriceMap;
    return this.getGemCost(Gem.RUBY) == confirmedOtherPriceMap.getGemCost(Gem.RUBY)
        && this.getGemCost(Gem.EMERALD) == confirmedOtherPriceMap.getGemCost(Gem.EMERALD)
        && this.getGemCost(Gem.SAPPHIRE) == confirmedOtherPriceMap.getGemCost(Gem.SAPPHIRE)
        && this.getGemCost(Gem.DIAMOND) == confirmedOtherPriceMap.getGemCost(Gem.DIAMOND)
        && this.getGemCost(Gem.ONYX) == confirmedOtherPriceMap.getGemCost(Gem.ONYX)
        && this.getGemCost(Gem.GOLD) == confirmedOtherPriceMap.getGemCost(Gem.GOLD);
  }

  @Override
  public int hashCode() {
    return hash(this.getGemCost(Gem.RUBY), this.getGemCost(Gem.EMERALD),
        this.getGemCost(Gem.SAPPHIRE), this.getGemCost(Gem.DIAMOND),
        this.getGemCost(Gem.ONYX), this.getGemCost(Gem.GOLD));
  }
}
