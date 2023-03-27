package com.hexanome16.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.inventory.Inventory;
import java.util.Map;
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
      case EMERALD_ROUTE -> {
        int numOfEmerald = 0;
        for (ServerLevelCard card : inventory.getOwnedCards()) {
          if (card.getGemBonus().getPriceMap().get(Gem.EMERALD) > 0) {
            numOfEmerald++;
          }
        }
        return numOfEmerald >= 5 && inventory.getOwnedNobles().size() >= 0;
      }
      case SAPPHIRE_ROUTE -> {
        int numOfSapphire = 0;
        int numOfOnyx = 0;
        for (ServerLevelCard card : inventory.getOwnedCards()) {
          if (card.getGemBonus().getPriceMap().get(Gem.SAPPHIRE) > 0) {
            numOfSapphire++;
          }
          if (card.getGemBonus().getPriceMap().get(Gem.ONYX) > 0) {
            numOfOnyx++;
          }
        }
        return numOfSapphire >= 3 && numOfOnyx >= 1;
      }
      case DIAMOND_ROUTE -> {
        int numOfDiamond = 0;
        for (ServerLevelCard card : inventory.getOwnedCards()) {
          if (card.getGemBonus().getPriceMap().get(Gem.DIAMOND) > 0) {
            numOfDiamond++;
          }
        }
        return numOfDiamond >= 2;
      }
      case RUBY_ROUTE -> {
        int numOfRuby = 0;
        int numOfDiamond = 0;
        for (ServerLevelCard card : inventory.getOwnedCards()) {
          if (card.getGemBonus().getPriceMap().get(Gem.RUBY) > 0) {
            numOfRuby++;
          }
          if (card.getGemBonus().getPriceMap().get(Gem.DIAMOND) > 0) {
            numOfDiamond++;
          }
        }
        return numOfRuby >= 3 && numOfDiamond >= 1;
      }
      default -> {
        throw new IllegalArgumentException("invalid trade route");
      }
    }
  }

  /**
   * Get the bonus prestige points for the trade post.
   *
   * @param tradePostList the player's trade posts.
   * @return the bonus prestige points.
   */
  @JsonIgnore
  public int getBonusPrestigePoints(Map<RouteType, TradePost> tradePostList) {
    switch (routeType) {
      case ONYX_ROUTE -> {
        return 1 + tradePostList.size();
      }
      case EMERALD_ROUTE -> {
        return 5;
      }
      default -> {
        return 0;
      }
    }
  }
}
