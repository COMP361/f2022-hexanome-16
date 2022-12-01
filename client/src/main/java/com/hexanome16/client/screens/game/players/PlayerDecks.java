package com.hexanome16.client.screens.game.players;

import static com.hexanome16.client.Config.APP_HEIGHT;
import static com.hexanome16.client.Config.APP_WIDTH;
import static com.hexanome16.client.Config.OPPONENT_SCALE;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
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
          List.of("red", "green", "blue", "white", "black", "gold");

  /** Generates the player decks.
   *
   * @param players players for this session
   */
  public static void generateAll(String[] players) {
    // number of players for this session
    int numOfPlayers = players.length;
    // current decks
    PlayerDecks decks = new PlayerDecks();
    // spawn who is playing TODO animation
    FXGL.spawn("PlayersTurn", 95, 60);
    // spawn the players
    int curr = 0;
    // spawn the current player TODO here array of username
    if (curr < numOfPlayers) {
      decks.spawnHorizontalPlayer(players[curr++], APP_WIDTH / 4.0, APP_HEIGHT - vertical, 1);
    }
    // spawn a player on the left
    if (curr < numOfPlayers) {
      decks.spawnLeftPlayer(players[curr++], 150.0, OPPONENT_SCALE);
    }
    // spawn a player at the top
    if (curr < numOfPlayers) {
      decks.spawnHorizontalPlayer(players[curr++], 140 + APP_WIDTH / 4.0, 1, OPPONENT_SCALE);
    }
    // spawn a player at the right
    if (curr < numOfPlayers) {
      decks.spawnRightPlayer(players[curr], APP_WIDTH / 4.0 + 7 * horizontal + 10,
          150.0, OPPONENT_SCALE);
    }
  }

  // helper
  private void spawnHorizontalPlayer(String name, double horizontal, double vertical,
                                     double scale) {
    // iteration variable
    int i = 0;
    // spawn the player icon
    FXGL.spawn("Player", new SpawnData(horizontal - PlayerDecks.horizontal * scale, vertical)
            .put("name", name)).setScaleUniform(0.2 * scale);
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
    FXGL.spawn("PlayerTokens", horizontal - (2 * PlayerDecks.horizontal + 20) * scale, vertical)
            .setScaleUniform(1.2 * scale);
    // spawn the reserved nobles and cards
    FXGL.spawn("ReservedNobles", horizontal + (i * PlayerDecks.horizontal + 10) * scale,
                    vertical + 95 * scale)
            .setScaleUniform(0.1 * scale);
    for (int j = 10; j <= 130; j += 60) {
      FXGL.spawn("ReservedCards", horizontal + (i * PlayerDecks.horizontal + j) * scale, vertical)
              .setScaleUniform(0.07 * scale);
    }
  }

  // helper
  private void spawnLeftPlayer(String name, double verticalShift, double scale) {
    // spawn the player icon
    FXGL.spawn("Player",
            new SpawnData(horizontal + horizontal * scale, verticalShift + 110)
                    .put("name", name)).setScaleUniform(0.2 * scale);
    // iterate through and spawn all the cards
    int i = 0;
    FXGL.spawn("Card",
            new SpawnData(horizontal, verticalShift + 120 + vertical * scale)
                    .put("color", cards.get(i++)).put("player", name))
            .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontal + horizontal * scale, verticalShift + 120 + vertical * scale)
                    .put("color", cards.get(i++)).put("player", name))
            .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontal, verticalShift + 130 + 2 * vertical * scale)
                    .put("color", cards.get(i++)).put("player", name))
            .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontal + horizontal * scale,
                    verticalShift + 130 + 2 * vertical * scale)
                    .put("color", cards.get(i++)).put("player", name))
            .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontal, verticalShift + 140 + 3 * vertical * scale)
                    .put("color", cards.get(i++)).put("player", name))
            .setScaleUniform(0.25 * scale);
    FXGL.spawn("Card",
            new SpawnData(horizontal + horizontal * scale,
                    verticalShift + 140 + 3 * vertical * scale)
                    .put("color", cards.get(i)).put("player", name))
            .setScaleUniform(0.25 * scale);
    // spawn the nobles, tokens, etc.
    FXGL.spawn("NobleCard", horizontal + horizontal * scale / 2.0,
                    verticalShift + 150 + 4 * vertical * scale)
            .setScaleUniform(0.2 * scale);
    FXGL.spawn("PlayerTokens", horizontal,
            verticalShift + 100).setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", horizontal - 60, verticalShift + 120 + vertical * scale)
            .setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards", horizontal - 50, verticalShift + 180 + vertical * scale)
            .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", horizontal - 50, verticalShift + 240 + vertical * scale)
            .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", horizontal - 50, verticalShift + 300 + vertical * scale)
            .setScaleUniform(0.07 * scale);
  }

  private void spawnRightPlayer(String name, double horizontalShift,
                                double verticalShift, double scale) {
    // spawn the player icon
    FXGL.spawn("Player",
                    new SpawnData(horizontalShift + horizontal * scale,
                            verticalShift + 110).put("name", name))
            .setScaleUniform(0.2 * scale);
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
    FXGL.spawn("PlayerTokens", horizontalShift, verticalShift + 100)
            .setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", horizontalShift + horizontal * scale + 95,
                    verticalShift + 120 + vertical * scale)
            .setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards", horizontalShift + horizontal * scale + 95,
                    verticalShift + 180 + vertical * scale)
            .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", horizontalShift + horizontal * scale + 95,
                    verticalShift + 240 + vertical * scale)
            .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", horizontalShift + horizontal * scale + 95,
                    verticalShift + 300 + vertical * scale)
            .setScaleUniform(0.07 * scale);
  }
}
