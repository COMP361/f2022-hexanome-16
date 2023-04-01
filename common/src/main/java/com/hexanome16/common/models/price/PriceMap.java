package com.hexanome16.common.models.price;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.deserializers.PriceMapDeserializer;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import lombok.Data;

/**
 * Holds the token price for a development card.
 */
@Data
@JsonDeserialize(using = PriceMapDeserializer.class)
public class PriceMap implements PriceInterface {
  /**
   * The Price map.
   */
  protected Hashtable<Gem, Integer> priceMap;

  /**
   * Alternative constructor to price map, takes a Map instead.
   *
   * @param priceMap gem map
   */
  public PriceMap(Map<Gem, Integer> priceMap) {
    this();
    if (priceMap == null) {
      throw new IllegalArgumentException("Price map cannot be null");
    }
    for (Map.Entry<Gem, Integer> entry : priceMap.entrySet()) {
      if (entry.getValue() == null) {
        throw new IllegalArgumentException("Price map cannot contain null values");
      }
      if (entry.getKey() == Gem.GOLD) {
        throw new IllegalArgumentException("Price map cannot contain gold");
      }
      this.addGems(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Empty constructor, sets all gems to 0.
   */
  public PriceMap() {
    this.priceMap = new Hashtable<>();
    for (Gem gem : Gem.values()) {
      if (gem != Gem.GOLD) {
        priceMap.put(gem, 0);
      }
    }
  }

  /**
   * Instantiates a new Price map.
   *
   * @param rubyAmount     the ruby amount
   * @param emeraldAmount  the emerald amount
   * @param sapphireAmount the sapphire amount
   * @param diamondAmount  the diamond amount
   * @param onyxAmount     the onyx amount
   */
  public PriceMap(int rubyAmount, int emeraldAmount, int sapphireAmount, int diamondAmount,
                  int onyxAmount) {
    priceMap = new Hashtable<>();
    priceMap.put(Gem.RUBY, rubyAmount);
    priceMap.put(Gem.EMERALD, emeraldAmount);
    priceMap.put(Gem.SAPPHIRE, sapphireAmount);
    priceMap.put(Gem.DIAMOND, diamondAmount);
    priceMap.put(Gem.ONYX, onyxAmount);
  }

  @Override
  public int getGemCost(Gem gem) {
    return priceMap.getOrDefault(gem, 0);
  }

  @Override
  public int getTotalGems() {
    return priceMap.values().stream().mapToInt(Integer::intValue).sum();
  }

  @Override
  public int getTotalNonJokers() {
    return getTotalGems();
  }

  @Override
  public void addGems(Gem gem, int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Cannot add negative amount of gems");
    }
    if (gem == Gem.GOLD) {
      throw new IllegalArgumentException("Cannot add gold to a price map");
    }
    priceMap.put(gem, priceMap.getOrDefault(gem, 0) + amount);
  }

  @Override
  public void addGems(Map<Gem, Integer> gems) {
    if (gems == null) {
      throw new IllegalArgumentException("Cannot add null gems");
    }
    for (Map.Entry<Gem, Integer> entry : gems.entrySet()) {
      if (entry.getValue() == null) {
        throw new IllegalArgumentException("Cannot add null gems");
      }
      this.addGems(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void removeGems(Gem gem, int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Cannot remove negative amount of gems");
    }
    if (gem == Gem.GOLD) {
      throw new IllegalArgumentException("Cannot remove gold from a price map");
    }
    int baseAmount = priceMap.getOrDefault(gem, 0);
    if (baseAmount < amount) {
      throw new IllegalArgumentException("Cannot remove more gems than are present");
    }
    priceMap.put(gem, priceMap.getOrDefault(gem, 0) - amount);
  }

  @Override
  public void removeGems(Map<Gem, Integer> gems) {
    if (gems == null) {
      throw new IllegalArgumentException("Cannot remove null gems");
    }
    for (Map.Entry<Gem, Integer> entry : gems.entrySet()) {
      if (entry.getValue() == null) {
        throw new IllegalArgumentException("Cannot remove null gems");
      }
      this.removeGems(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public boolean hasAtLeastAmountOfGems(PriceInterface priceInterface) {
    for (var entry : priceMap.entrySet()) {
      Gem gem = entry.getKey();
      int value = entry.getValue();
      if (priceInterface.getGemCost(gem) > value) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Set<Gem> getTypesOfGems() {
    return this.priceMap.keySet();
  }

  @Override
  public PriceInterface subtract(PriceInterface priceInterface) {
    Set<Gem> paramTypes = priceInterface.getTypesOfGems();
    Set<Gem> thisTypes = this.getTypesOfGems();
    if (!(thisTypes.containsAll(paramTypes) && paramTypes.containsAll(thisTypes))) {
      throw new IllegalArgumentException("Maps must contains same set of gems");
    }
    var mapToRemoveFrom = this.priceMap;
    PriceMap newPriceMap = new PriceMap();
    for (var entry : mapToRemoveFrom.entrySet()) {
      Gem key = entry.getKey();
      int newValue = entry.getValue() - priceInterface.getGemCost(key);
      if (newValue < 0) {
        newValue = 0;
      }
      newPriceMap.addGems(key, newValue);
    }
    return newPriceMap;
  }
}
