package com.hexanome16.server.models.savegame;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.cards.ServerCity;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to handle loading and creating savegames.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveGame {
  private String gamename;
  private String id;
  private String currentPlayer;
  private String[] usernames;
  private String creator;
  private Map<Level, ServerLevelCard[]> onBoardDecks;
  private ServerNoble[] onBoardNobles;
  private ServerCity[] onBoardCities;
  private Map<Level, ServerLevelCard[]> remainingDecks;
  private ServerNoble[] remainingNobles;
  private ServerCity[] remainingCities;
  private PurchaseMap bank;
  private ServerPlayer[] players;
}
