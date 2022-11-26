package com.hexanome16.server.action;

import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;

/**
 * Purchase action class.
 */
public class PurchaseAction implements Action {
  private final Game game;
  private final Player player;
  private final LevelCard card;
  private final PaymentMap paymentMap; //payment

  /**
   * Creates a new purchase action.
   *
   * @param game game
   * @param player player
   * @param card card
   * @param paymentMap the amount that the player needs to pay
   */
  public PurchaseAction(Game game, Player player, LevelCard card, PaymentMap paymentMap) {
    this.game = game;
    this.player = player;
    this.card = card;
    this.paymentMap = paymentMap;
  }

  public Game getGame() {
    return game;
  }

  public Player getPlayer() {
    return player;
  }

  public LevelCard getCard() {
    return card;
  }

  public PaymentMap getPaymentMap() {
    return paymentMap;
  }
}
