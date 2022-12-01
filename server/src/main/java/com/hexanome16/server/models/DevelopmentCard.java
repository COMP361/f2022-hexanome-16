package com.hexanome16.server.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Development card class.
 */
@Data
public abstract class DevelopmentCard {
  private final int id;
  private final int prestigePoint;

  private final String texturePath;
  private final Price price;

  /**
   * Add this card to the player's inventory.
   *
   * @param inventory the inventory
   * @return true on success
   */
  abstract boolean addToInventory(Inventory inventory);

  /**
   * Reserve this card.
   *
   * @param inventory the inventory
   * @return true on success
   */
  abstract boolean reserveCard(Inventory inventory);
}
