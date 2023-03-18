package com.hexanome16.client.screens.game.players;

import static com.hexanome16.client.Config.APP_HEIGHT;
import static com.hexanome16.client.Config.APP_WIDTH;
import static com.hexanome16.client.Config.OPPONENT_SCALE;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.dto.PlayerJson;
import com.hexanome16.common.models.price.Gem;
import java.util.Arrays;
import java.util.List;

/**
 * Player decks.
 *
 * @author Elea Dufresne
 */
public class PlayerDecks {
  // there are 4 players hence 4 decks
  private static final double horizontal = 150; // horizontal distance between cards
  private static final double vertical = 180; // vertical distance between cards
  private static final List<String> cards =
      Arrays.stream(Gem.values()).map(Gem::getBonusType).toList();
  private static final String[] origColors = {"yellow", "black", "red", "blue"};
  private static String[] colors = origColors.clone();

  /**
   * Generates the player decks.
   *
   * @param players players for this session
   */
  public static void generateAll(PlayerJson[] players) {
    String myName = AuthUtils.getPlayer().getName();
    colors = origColors.clone();
    while (!players[0].getUsername().equals(myName)) {
      PlayerJson firstPerson = players[0];
      String firstColor = colors[0];
      for (int i = 1; i < players.length; i++) {
        players[i - 1] = players[i];
        colors[i - 1] = colors[i];
      }
      players[players.length - 1] = firstPerson;
      colors[players.length - 1] = firstColor;
    }

    // number of players for this session
    final int numOfPlayers = players.length;
    // current decks
    final PlayerDecks decks = new PlayerDecks();
    FXGL.getGameWorld().removeEntities(
        FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER_TURN));
    // spawn who is playing
    FXGL.spawn("PlayersTurn", 95, 60);
    FXGL.getGameWorld().removeEntities(
        FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER));
    // spawn the players
    int curr = 0;
    // spawn the current player
    decks.spawnBottomPlayer(players[curr].getUsername(), players[curr].getPrestigePoints(),
        APP_WIDTH / 4.0, APP_HEIGHT - vertical, 1);
    curr++;
    // spawn a player on the left
    if (curr < numOfPlayers) {
      decks.spawnLeftPlayer(players[curr].getUsername(), players[curr].getPrestigePoints(),
          150.0, OPPONENT_SCALE);
      curr++;
    }
    // spawn a player at the top
    if (curr < numOfPlayers) {
      decks.spawnTopPlayer(players[curr].getUsername(), players[curr].getPrestigePoints(),
          140 + APP_WIDTH / 4.0, 1, OPPONENT_SCALE);
      curr++;
    }
    // spawn a player at the right
    if (curr < numOfPlayers) {
      decks.spawnRightPlayer(players[curr].getUsername(), players[curr].getPrestigePoints(),
          APP_WIDTH / 4.0 + 7 * horizontal + 100, 150.0, OPPONENT_SCALE);
    }
  }

  // helper
  private void spawnBottomPlayer(String name, int prestigePoints, double horizontal,
                                 double vertical, double scale) {
    // iteration variable
    int i = 0;
    // spawn the player icon
    FXGL.spawn("Player", new SpawnData(horizontal - PlayerDecks.horizontal * scale, vertical)
        .put("name", name).put("color", colors[0]).put("prestigePoints", prestigePoints))
        .setScaleUniform(0.2 * scale);
    // spawn the playing cards deck
    while (i < 6) {
      FXGL.spawn("Card",
              new SpawnData(horizontal + (i * PlayerDecks.horizontal) * scale, vertical)
                  .put("color", cards.get(i++)).put("player", name))
          .setScaleUniform(0.25 * scale);
    }
    // spawn the nobles deck
    FXGL.spawn("NobleCard", horizontal + (i++ * PlayerDecks.horizontal) * scale, vertical + 15)
        .setScaleUniform(0.2 * scale);
    // spawn the player's bank
    FXGL.spawn("PlayerTokens", new SpawnData(horizontal - (2 * PlayerDecks.horizontal + 20) * scale,
        vertical).put("player", name)).setScaleUniform(1.2 * scale);
    // spawn the reserved nobles and cards
    FXGL.spawn("ReservedNobles", horizontal + (i * PlayerDecks.horizontal + 10) * scale,
            vertical + 95 * scale)
        .setScaleUniform(0.1 * scale);
    for (int j = 10; j <= 130; j += 60) {
      FXGL.spawn("ReservedCards", new SpawnData(horizontal + (i * PlayerDecks.horizontal + j)
          * scale, vertical).put("player", name)).setScaleUniform(0.07 * scale);
    }
  }

  // helper
  private void spawnTopPlayer(String name, int prestigePoints, double horizontal, double vertical,
                              double scale) {
    // iteration variable
    int i = 0;
    // spawn the player icon
    FXGL.spawn("Player", new SpawnData(horizontal - PlayerDecks.horizontal * scale, vertical)
        .put("name", name).put("color", colors[2]).put("prestigePoints", prestigePoints))
        .setScaleUniform(0.3 * scale);
    // spawn the playing cards deck
    while (i < 6) {
      FXGL.spawn("Card",
              new SpawnData(horizontal + (i * PlayerDecks.horizontal) * scale, vertical)
                  .put("color", cards.get(i++)).put("player", name))
          .setScaleUniform(0.25 * scale);
    }
    // spawn the nobles deck
    FXGL.spawn("NobleCard", horizontal + (i++ * PlayerDecks.horizontal) * scale, vertical + 15)
        .setScaleUniform(0.2 * scale);
    // spawn the player's bank
    FXGL.spawn("PlayerTokens", new SpawnData(horizontal - (2 * PlayerDecks.horizontal + 20) * scale,
        vertical).put("player", name)).setScaleUniform(1.2 * scale);
    // spawn the reserved nobles and cards
    FXGL.spawn("ReservedNobles", horizontal + (i * PlayerDecks.horizontal + 10) * scale,
            vertical + 95 * scale)
        .setScaleUniform(0.1 * scale);
    for (int j = 10; j <= 130; j += 60) {
      FXGL.spawn("ReservedCards", new SpawnData(horizontal + (i * PlayerDecks.horizontal + j)
          * scale, vertical).put("player", name)).setScaleUniform(0.07 * scale);
    }
  }

  // helper
  private void spawnLeftPlayer(String name, int prestigePoints, double verticalShift,
                               double scale) {
    int horizontal = 100;
    // spawn the player icon
    FXGL.spawn("Player",
        new SpawnData(horizontal + 100, verticalShift + 110)
            .put("name", name).put("color", colors[1]).put("prestigePoints", prestigePoints))
        .setScaleUniform(0.3 * scale);
    // iterate through and spawn all the cards
    int i = 0;
    FXGL.spawn("Card",
            new SpawnData(horizontal, verticalShift + 120 + vertical * scale)
                .put("color", cards.get(i++)).put("player", name))
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontal + 100, verticalShift + 120 + vertical * scale)
                .put("color", cards.get(i++)).put("player", name))
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontal, verticalShift + 130 + 2 * vertical * scale)
                .put("color", cards.get(i++)).put("player", name))
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontal + 100,
                verticalShift + 130 + 2 * vertical * scale)
                .put("color", cards.get(i++)).put("player", name))
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontal, verticalShift + 140 + 3 * vertical * scale)
                .put("color", cards.get(i++)).put("player", name))
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontal + 100,
                verticalShift + 140 + 3 * vertical * scale)
                .put("color", cards.get(i)).put("player", name))
        .setScaleUniform(0.25 * scale);
    // spawn the nobles, tokens, etc.
    FXGL.spawn("NobleCard", horizontal + horizontal * scale / 2.0,
            verticalShift + 150 + 4 * vertical * scale)
        .setScaleUniform(0.2 * scale);
    FXGL.spawn("PlayerTokens", new SpawnData(horizontal, verticalShift + 100)
        .put("player", name)).setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", horizontal - 60, verticalShift + 120 + vertical * scale)
        .setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards",
        new SpawnData(horizontal - 50, verticalShift + 180 + vertical * scale)
            .put("player", name)).setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards",
        new SpawnData(horizontal - 50, verticalShift + 240 + vertical * scale)
            .put("player", name)).setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards",
        new SpawnData(horizontal - 50, verticalShift + 300 + vertical * scale)
            .put("player", name)).setScaleUniform(0.07 * scale);
  }

  private void spawnRightPlayer(String name, int prestigePoints, double horizontalShift,
                                double verticalShift, double scale) {
    // spawn the player icon
    FXGL.spawn("Player",
            new SpawnData(horizontalShift + horizontal * scale,
                verticalShift + 110).put("name", name).put("color", colors[3])
                .put("prestigePoints", prestigePoints)).setScaleUniform(0.3 * scale);
    // iterate through and spawn all the cards
    int i = 0;
    FXGL.spawn("Card",
            new SpawnData(horizontalShift, verticalShift + 120 + vertical * scale)
                .put("color", cards.get(i++)).put("player", name))
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontalShift + horizontal * scale,
                verticalShift + 120 + vertical * scale)
                .put("color", cards.get(i++)).put("player", name))
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontalShift, verticalShift + 130 + 2 * vertical * scale)
                .put("color", cards.get(i++)).put("player", name))
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontalShift + horizontal * scale,
                verticalShift + 130 + 2 * vertical * scale)
                .put("color", cards.get(i++)).put("player", name))
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontalShift, verticalShift + 140 + 3 * vertical * scale)
                .put("color", cards.get(i++)).put("player", name))
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontalShift + horizontal * scale,
                verticalShift + 140 + 3 * vertical * scale)
                .put("color", cards.get(i)).put("player", name))
        .setScaleUniform(0.25 * scale);
    // spawn the nobles, tokens, etc.
    FXGL.spawn("NobleCard", horizontalShift + horizontal * scale / 2.0,
            verticalShift + 150 + 4 * vertical * scale)
        .setScaleUniform(0.2 * scale);
    FXGL.spawn("PlayerTokens", new SpawnData(horizontalShift, verticalShift + 100
    ).put("player", name)).setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", horizontalShift + horizontal * scale + 95,
            verticalShift + 120 + vertical * scale)
        .setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards", new SpawnData(horizontalShift + horizontal * scale + 95,
        verticalShift + 180 + vertical * scale)
        .put("player", name)).setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", new SpawnData(horizontalShift + horizontal * scale + 95,
        verticalShift + 240 + vertical * scale)
        .put("player", name)).setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", new SpawnData(horizontalShift + horizontal * scale + 95,
        verticalShift + 300 + vertical * scale)
        .put("player", name)).setScaleUniform(0.07 * scale);
  }
}
