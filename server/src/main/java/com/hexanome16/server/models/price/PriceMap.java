package com.hexanome16.server.models.price;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.server.util.deserializers.PriceMapDeserializer;
import java.util.Hashtable;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Holds the token price for a development card.
 */
@Data
@NoArgsConstructor
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
    this.priceMap = new Hashtable<>();
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
}