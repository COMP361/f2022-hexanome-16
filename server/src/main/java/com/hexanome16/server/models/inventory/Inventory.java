package com.hexanome16.server.models.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.TradePost;
import com.hexanome16.server.models.bank.PlayerBank;
import com.hexanome16.server.models.cards.ServerCity;
import com.hexanome16.server.models.cards.ServerLevelCard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Player inventory class.
 */
@Getter
@Data
@AllArgsConstructor
public class Inventory {
  /* fields *************************************************************************************/
  private final PlayerBank playerBank;
  private final List<Noble> ownedNobles;
  private final List<Noble> reservedNobles;
  private final List<ServerLevelCard> ownedCards;
  private final List<ServerLevelCard> reservedCards;
  private final PurchaseMap gemBonuses;
  private final Map<RouteType, TradePost> tradePosts;
  private final List<ServerCity> ownedCities;
  private int prestigePoints;

  /* Constructor *********************************************************************************/

  /**
   * Creates Inventory and sets up all fields.
   */
  public Inventory() {
    playerBank = new PlayerBank();
    ownedNobles = new ArrayList<>();
    reservedNobles = new ArrayList<>();
    ownedCards = new ArrayList<>();
    reservedCards = new ArrayList<>();
    gemBonuses = new PurchaseMap(0, 0, 0, 0, 0, 0);
    tradePosts = new HashMap<>();
    ownedCities = new ArrayList<>();
    prestigePoints = 0;
  }

  /* add methods ******************************************************************************/

  /**
   * Acquire card and add bonuses to inventory.
   *
   * @param card the card to add
   * @return true if the card was added to inventory
   */
  public boolean acquireCard(ServerLevelCard card) {
    gemBonuses.addGems(card.getGemBonus().getPriceMap());
    prestigePoints += card.getCardInfo().prestigePoint();
    return ownedCards.add(card);
  }

  /**
   * Reserve the given card.
   *
   * @param card card to reserve
   * @return if the card has been reserved successfully
   */
  public boolean reserveCard(ServerLevelCard card) {
    // if the player already has three reserved cards
    if (reservedCards.size() >= 3) {
      return false;
    }
    return reservedCards.add(card);
  }

  /**
   * Acquire noble boolean.
   *
   * @param noble the noble
   * @return the boolean
   */
  public boolean acquireNoble(Noble noble) {
    prestigePoints += noble.getCardInfo().prestigePoint();
    return ownedNobles.add(noble);
  }

  /**
   * Acquire a city.
   *
   * @param city the city
   * @return if added successfully
   */
  public boolean acquireCity(ServerCity city) {
    return ownedCities.add(city);
  }

  /**
   * Reserve noble boolean.
   *
   * @param noble the noble
   * @return the boolean
   */
  public boolean reserveNoble(Noble noble) {
    return reservedNobles.add(noble);
  }

  /**
   * Verifies that inventory contains an amount of bonuses.
   *
   * @param price minimum amount needed in inventory
   * @return true if the inventory has at least enough for the price
   */
  @JsonIgnore
  public boolean hasAtLeastGivenBonuses(PriceInterface price) {
    return gemBonuses.hasAtLeastAmountOfGems(price);
  }

  /**
   * Adds a trade post to the list.
   *
   * @param tradePost the trade post to be added.
   */
  public void addTradePost(TradePost tradePost) {
    if (!tradePosts.containsKey(tradePost.getRouteType())) {
      //add one for each new trade post if the player has onyx route
      if (tradePosts.containsKey(RouteType.ONYX_ROUTE)) {
        prestigePoints += 1;
      }
      prestigePoints += tradePost.getBonusPrestigePoints(this.tradePosts);
      tradePosts.put(tradePost.getRouteType(), tradePost);
    }
  }

  /**
   * Gets prestige points.
   *
   * @return the prestige points
   */
  public int getPrestigePoints() {
    return prestigePoints;
  }


  /**
   * true of there are more than 10 tokens in the inventory.
   *
   * @return true or false.
   */
  public boolean hasMoreThanTenTokens() {
    return playerBank.hasMoreThanKtokens(10);
  }

  /**
   * gets owned token types.
   *
   * @return Array of owned tokens.
   */
  @JsonIgnore
  public Gem[] getOwnedTokenTypes() {
    return playerBank.getOwnedTokenTypes();
  }
}
