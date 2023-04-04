package com.hexanome16.common.models.price;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.deserializers.OrientPurchaseMapDeserializer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A class for representing a purchase map plus Orient extension golden cards.
 */
@Data
@ToString(callSuper = true)
@JsonDeserialize(using = OrientPurchaseMapDeserializer.class)
@NoArgsConstructor
public class OrientPurchaseMap extends PurchaseMap {

  @Getter
  private int goldenCardsAmount;

  /**
   * Instantiates a new OrientPurchase map.
   *
   * @param rubyAmount        The ruby amount
   * @param emeraldAmount     The emerald amount
   * @param sapphireAmount    The sapphire amount
   * @param diamondAmount     The diamond amount
   * @param onyxAmount        The onyx amount
   * @param goldAmount        The gold amount
   * @param goldenCardsAmount The amount of golden cards
   */
  public OrientPurchaseMap(int rubyAmount, int emeraldAmount, int sapphireAmount, int diamondAmount,
                           int onyxAmount, int goldAmount, int goldenCardsAmount) {
    super(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount, onyxAmount, goldAmount);
    this.goldenCardsAmount = goldenCardsAmount;
  }

  /**
   * Alternative constructor to OrientPurchase map, takes a Map from Gems to Integer instead
   * and an int for the golden cards amount.
   *
   * @param purchaseMap       A map between each gem and their corresponding amount as an
   *                          Integer.
   * @param goldenCardsAmount The amount of golden cards
   */
  public OrientPurchaseMap(Map<Gem, Integer> purchaseMap, int goldenCardsAmount) {
    super(purchaseMap);
    this.goldenCardsAmount = goldenCardsAmount;
  }


  /**
   * Alternative constructor to OrientPurchase map, takes a PriceMap instead and integers for
   * the remaining information.
   *
   * @param priceMap          A map between each gem and their corresponding amount as an
   *                          Integer.
   * @param goldAmount        The amount of gold to add.
   * @param goldenCardsAmount The amount of golden cards.
   */
  public OrientPurchaseMap(PriceMap priceMap, int goldAmount, int goldenCardsAmount) {
    super(priceMap, goldAmount);
    this.goldenCardsAmount = goldenCardsAmount;
  }

  /**
   * Alternative constructor, takes in a purchase map instead.
   *
   * @param purchaseMap purchase map.
   * @param goldenCardsAmount golden token amount.
   */
  public OrientPurchaseMap(PurchaseMap purchaseMap, int goldenCardsAmount) {
    this(purchaseMap.priceMap, goldenCardsAmount);
  }

  /**
   * Compares OrientPurchaseMap to a price map and returns true if OrientPurchaseMap has exactly
   * enough funds to buy the PriceMap (not that golden cards allow for only one of their golden
   * tokens to be used and this will return true).
   *
   * @param otherPriceMap priceMap we want to compare to.
   * @return true if it can be used to buy price map false otherwise.
   */
  @Override
  public boolean canBeUsedToBuy(PriceInterface otherPriceMap) {
    if (otherPriceMap == null) {
      return false;
    }
    if (this == otherPriceMap) {
      return true;
    }

    if (this.getTotalNonJokers() <= otherPriceMap.getTotalNonJokers()
        && this.getGemCost(Gem.RUBY) <= otherPriceMap.getGemCost(Gem.RUBY)
        && this.getGemCost(Gem.EMERALD) <= otherPriceMap.getGemCost(Gem.EMERALD)
        && this.getGemCost(Gem.SAPPHIRE) <= otherPriceMap.getGemCost(Gem.SAPPHIRE)
        && this.getGemCost(Gem.DIAMOND) <= otherPriceMap.getGemCost(Gem.DIAMOND)
        && this.getGemCost(Gem.ONYX) <= otherPriceMap.getGemCost(Gem.ONYX)
    ) {
      int diffNonJokers = otherPriceMap.getTotalNonJokers() - this.getTotalNonJokers();
      return (diffNonJokers - this.getGemCost(Gem.GOLD))
          == (this.goldenCardsAmount) * 2
          || (this.goldenCardsAmount != 0 && (diffNonJokers - this.getGemCost(Gem.GOLD))
          == ((this.goldenCardsAmount) * 2) - 1);
    }
    return false;
  }


  // TODO :: VERIFY IF THIS WOULD WORK IN GAME

  /**
   * alternative checker for can be used to buy, takes into consideration SAPPHIRE trade route,
   * DOESN'T check for exact price.
   *
   * @param otherPriceMap priceMap we want to compare to.
   * @return true if it can be used to buy with trade route active, false otherwise.
   */
  @Override
  public boolean canBeUsedToBuyAlt(PriceInterface otherPriceMap) {
    Hashtable<Gem, Integer> map = new Hashtable<>();

    for (Gem gem : Gem.values()) {
      if (gem == Gem.GOLD) {
        continue;
      }
      map.put(gem, this.priceMap.get(gem));
    }

    PriceMap pm = new PriceMap(map);
    PurchaseMap fullyUsedGoldenCard = new PurchaseMap(pm,
        getGemCost(Gem.GOLD) + goldenCardsAmount * 2);

    if (goldenCardsAmount == 0) {
      return fullyUsedGoldenCard.canBeUsedToBuyAlt(otherPriceMap);
    }

    PurchaseMap halfUsedGoldenCard = new PurchaseMap(pm,
        getGemCost(Gem.GOLD) + (goldenCardsAmount * 2) - 1);
    return fullyUsedGoldenCard.canBeUsedToBuyAlt(otherPriceMap)
        || halfUsedGoldenCard.canBeUsedToBuyAlt(otherPriceMap);
  }

  // FOR SUBTRACTING PRICE MAPS ONLY
  @Override
  public PriceInterface subtract(PriceInterface priceInterface) {
    assert priceInterface instanceof PriceMap;
    Map<Gem, Integer> map = new HashMap<>();
    Gem[] gems = Gem.values();
    List<Gem> gemList = Arrays.asList(gems);
    ArrayList<Gem> gemsMinusGold = new ArrayList<>(gemList);
    gemsMinusGold.remove(Gem.GOLD);

    for (Gem gem : gemsMinusGold) {
      map.put(gem, this.getGemCost(gem) - priceInterface.getGemCost(gem));
    }
    return new OrientPurchaseMap(new PriceMap(map), this.getGemCost(Gem.GOLD),
        this.goldenCardsAmount);
  }
}
