package com.hexanome16.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.inventory.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Class for each trade post.
 */
@Data
@AllArgsConstructor
public class TradePost {
  @Getter
  private RouteType routeType;

  /**
   * To determine if the trade post can be granted to the player.
   *
   * @param inventory player's inventory.
   * @return if the player can earn this trade post.
   */
  @JsonIgnore
  public boolean canBeTakenByPlayerWith(Inventory inventory) {
    switch (routeType) {
      case ONYX_ROUTE -> {
        int numOfOnyx = 0;
        for (ServerLevelCard card : inventory.getOwnedCards()) {
          if (card.getGemBonus().getPriceMap().get(Gem.ONYX) > 0) {
            numOfOnyx++;
          }
        }
        return numOfOnyx >= 3;
      }
      default -> {
        //todo other routes
        return false;
      }
    }
  }

  /**
   * Get the bonus prestige points for the trade post.
   *
   * @return the bonus prestige points.
   */
  @JsonIgnore
  public int getBonusPrestigePoints() {
    switch (routeType) {
      case ONYX_ROUTE -> {
        return 1;
      }
      default -> {
        return 0;
      }
    }
  }
}
