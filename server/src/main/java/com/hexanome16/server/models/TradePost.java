package com.hexanome16.server.models;

import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class for each trade post.
 */
@Data
@AllArgsConstructor
public class TradePost {
  RouteType routeType;

  /**
   * To determine if the trade post can be granted to the player.
   *
   * @param inventory player's inventory.
   * @return if the player can earn this trade post.
   */
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
}
