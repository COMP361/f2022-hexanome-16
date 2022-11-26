package com.hexanome16.server.action.generator;

import com.hexanome16.server.action.Action;
import com.hexanome16.server.action.PaymentMap;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Gem;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.PriceMap;
import com.hexanome16.server.models.TokenPrice;
import com.hexanome16.server.models.TokenStack;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Generator for purchase action.
 */
public class PurchaseActionGenerator implements ActionGenerator {

  /**
   * Generates possible price combinations for client.
   *
   * @param game   game
   * @param player player
   * @param card   card to generate actions
   * @return a map of available actions
   */
  public Map<String, Action> generateActions(Game game, Player player, LevelCard card) {
    Map<String, Action> actionMap = new LinkedHashMap();
    PriceMap priceMap = ((TokenPrice) card.getPrice()).getPriceMap();

    //possible combinations of gem substituted with virtual token
    for (int a = 0; a < priceMap.getRubyAmount(); a++) {
      for (int b = 0; b < priceMap.getEmeraldAmount(); b++) {
        for (int c = 0; c < priceMap.getSapphireAmount(); c++) {
          for (int d = 0; d < priceMap.getDiamondAmount(); d++) {
            for (int e = 0; e < priceMap.getOnyxAmount(); e++) {
              //check if there's enough virtual token to cover the payment
              if (a + b + c + d + e
                  <=
                  player.getBank().getTokenStackMap().get(Gem.GOLD).getTokenList().size()) {
                //price after discount
                int discountRuby = priceMap.getRubyAmount() - a;
                int discountEmerald = priceMap.getEmeraldAmount() - b;
                int discountSapphire = priceMap.getSapphireAmount() - c;
                int discountDiamond = priceMap.getDiamondAmount() - d;
                int discountOnyx = priceMap.getOnyxAmount() - e;
                //check if player has enough tokens
                if (validPayment(player, discountRuby, discountEmerald, discountSapphire,
                    discountDiamond, discountOnyx)) {
                  PaymentMap paymentMap =
                      new PaymentMap(discountRuby, discountEmerald, discountSapphire,
                          discountDiamond, discountOnyx, a + b + c + d + e);
                }
              }
            }
          }
        }
      }
    }
    return actionMap;
  }

  /**
   * Helper method to check if player has enough token to pay.
   *
   * @param player         player
   * @param rubyAmount     ruby price
   * @param emeraldAmount  emerald price
   * @param sapphireAmount sapphire price
   * @param diamondAmount  diamond price
   * @param onyxAmount     onyx price
   * @return whether the player is able to pay
   */
  private boolean validPayment(Player player, int rubyAmount, int emeraldAmount, int sapphireAmount,
                               int diamondAmount, int onyxAmount) {
    Map<Gem, TokenStack> tokenStackMap = player.getBank().getTokenStackMap();
    return tokenStackMap.get(Gem.RUBY).getTokenList().size() >= rubyAmount
        &&
        tokenStackMap.get(Gem.EMERALD).getTokenList().size() >= emeraldAmount
        &&
        tokenStackMap.get(Gem.SAPPHIRE).getTokenList().size() >= sapphireAmount
        &&
        tokenStackMap.get(Gem.DIAMOND).getTokenList().size() >= diamondAmount
        &&
        tokenStackMap.get(Gem.ONYX).getTokenList().size() >= onyxAmount;
  }
}
