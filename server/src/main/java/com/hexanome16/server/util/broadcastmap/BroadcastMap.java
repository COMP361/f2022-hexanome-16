package com.hexanome16.server.util.broadcastmap;

import com.hexanome16.common.dto.PlayerJson;
import com.hexanome16.common.dto.PlayerListJson;
import com.hexanome16.common.dto.WinJson;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.dto.cards.NobleDeckJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.server.models.game.Game;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to manage the broadcast content.
 */
public class BroadcastMap {
  private final Map<BroadcastMapKey, BroadcastContentManager<BroadcastContent>> broadcastMap;

  /**
   * Default constructor.
   *
   * @param game The game.
   */
  public BroadcastMap(Game game) {
    Map<BroadcastMapKey, BroadcastContentManager<BroadcastContent>> map = new HashMap<>();
    try {
      for (Level level : Level.values()) {
        BroadcastContentManager<BroadcastContent> broadcastContentManager =
            new BroadcastContentManager<>(new DeckJson(
                game.getOnBoardDeck(level).getCardList(), level));
        map.put(BroadcastMapKey.fromLevel(level), broadcastContentManager);
      }
      BroadcastContentManager<BroadcastContent> broadcastContentManagerPlayer =
          new BroadcastContentManager<>(new PlayerListJson(Arrays.stream(game.getPlayers()).map(
              player -> new PlayerJson(player.getName(), !game.isNotPlayersTurn(player),
                  player.getInventory().getPrestigePoints(), player.getPlayerOrder())
          ).toArray(PlayerJson[]::new)));
      BroadcastContentManager<BroadcastContent> broadcastContentManagerWinners =
          new BroadcastContentManager<>(new WinJson(new String[game.getPlayers().length]));
      BroadcastContentManager<BroadcastContent> broadcastContentManagerNoble =
          new BroadcastContentManager<>(new NobleDeckJson(game.getOnBoardNobles().getCardList()));
      map.put(BroadcastMapKey.PLAYERS, broadcastContentManagerPlayer);
      map.put(BroadcastMapKey.WINNERS, broadcastContentManagerWinners);
      map.put(BroadcastMapKey.NOBLES, broadcastContentManagerNoble);
    } catch (Exception e) {
      e.printStackTrace();
    }
    broadcastMap = Collections.unmodifiableMap(map);
  }

  /**
   * Gets the broadcast content manager for the given key.
   *
   * @param key The key to get the broadcast content manager for.
   * @return The broadcast content manager for the given key.
   */
  public BroadcastContentManager<BroadcastContent> getManager(BroadcastMapKey key) {
    return broadcastMap.get(key);
  }

  /**
   * Updates the broadcast content for the given key.
   *
   * @param key   The key to update the broadcast content for.
   * @param value The new broadcast content.
   */
  public void updateValue(BroadcastMapKey key, BroadcastContent value) {
    Class<? extends BroadcastContent> valueClass = key.getAssocClass();
    if (!valueClass.equals(value.getClass())) {
      throw new IllegalArgumentException("Invalid update value");
    }
    broadcastMap.get(key).updateBroadcastContent(value);
  }
}
