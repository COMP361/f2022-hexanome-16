package com.hexanome16.client.screens.game.players;

import static com.hexanome16.client.Config.APP_HEIGHT;
import static com.hexanome16.client.Config.APP_WIDTH;
import static com.hexanome16.client.Config.OPPONENT_SCALE;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import java.util.List;

public class PlayerDecks {
  // there are 4 players hence 4 decks
  private static final double xCardDist = 150;
  // horizontal distance between cards (this is for the current player)
  private static final double yCardDist = 180;
  // vertical distance between cards (this is for opponents)

  // helper
  private void spawnHorizontalPlayer(String name, double xpos, double ypos, double scale) {
    // iteration variable
    int i = 0;
    // spawn the player icon
    FXGL.spawn("Player", new SpawnData(xpos - xCardDist * scale, ypos + 20).put("name", name))
        .setScaleUniform(0.2 * scale);
    // card entities names
    List<String> cards =
        List.of("RedCard", "GreenCard", "BlueCard", "WhiteCard", "BlackCard", "GoldCard");
    // spawn the playing cards deck
    while (i < 6) {
      FXGL.spawn(cards.get(i), xpos + (i++ * xCardDist) * scale, ypos)
          .setScaleUniform(0.25 * scale);
    }
    // spawn the nobles deck
    FXGL.spawn("NobleCard", xpos + (i++ * xCardDist) * scale, ypos + 15)
        .setScaleUniform(0.2 * scale);
    // spawn the player's bank
    FXGL.spawn("PlayerTokens", xpos - (2 * xCardDist + 20) * scale, ypos)
        .setScaleUniform(1.2 * scale);
    // spawn the reserved nobles and cards
    FXGL.spawn("ReservedNobles", xpos + (i * xCardDist + 10) * scale, ypos + 95 * scale)
        .setScaleUniform(0.1 * scale);
    for (int j = 10; j <= 130; j += 60) {
      FXGL.spawn("ReservedCards", xpos + (i * xCardDist + j) * scale, ypos)
          .setScaleUniform(0.07 * scale);
    }
  }

  // helper
  private void spawnLeftPlayer(String name, double yshift, double scale) {
    FXGL.spawn("Player",
            new SpawnData(xCardDist + xCardDist * scale, yshift + 110).put("name", name))
        .setScaleUniform(0.2 * scale);
    FXGL.spawn("RedCard", xCardDist, yshift + 120 + yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("GreenCard", xCardDist + xCardDist * scale, yshift + 120 + yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("BlueCard", xCardDist, yshift + 130 + 2 * yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("WhiteCard", xCardDist + xCardDist * scale, yshift + 130 + 2 * yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("BlackCard", xCardDist, yshift + 140 + 3 * yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("GoldCard", xCardDist + xCardDist * scale, yshift + 140 + 3 * yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("NobleCard", xCardDist + xCardDist * scale / 2.0,
            yshift + 150 + 4 * yCardDist * scale)
        .setScaleUniform(0.2 * scale);
    FXGL.spawn("PlayerTokens", xCardDist, yshift + 100).setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", xCardDist - 60, yshift + 120 + yCardDist * scale)
        .setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards", xCardDist - 50, yshift + 180 + yCardDist * scale)
        .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", xCardDist - 50, yshift + 240 + yCardDist * scale)
        .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", xCardDist - 50, yshift + 300 + yCardDist * scale)
        .setScaleUniform(0.07 * scale);
  }

  private void spawnRightPlayer(String name, double xshift, double yshift, double scale) {
    FXGL.spawn("Player",
            new SpawnData(xshift + xCardDist * scale, yshift + 110).put("name", name))
        .setScaleUniform(0.2 * scale);
    FXGL.spawn("RedCard", xshift, yshift + 120 + yCardDist * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("GreenCard", xshift + xCardDist * scale, yshift + 120 + yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("BlueCard", xshift, yshift + 130 + 2 * yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("WhiteCard", xshift + xCardDist * scale, yshift + 130 + 2 * yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("BlackCard", xshift, yshift + 140 + 3 * yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("GoldCard", xshift + xCardDist * scale, yshift + 140 + 3 * yCardDist * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("NobleCard", xshift + xCardDist * scale / 2.0,
            yshift + 150 + 4 * yCardDist * scale)
        .setScaleUniform(0.2 * scale);
    FXGL.spawn("PlayerTokens", xshift, yshift + 100).setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", xshift + xCardDist * scale + 95,
            yshift + 120 + yCardDist * scale)
        .setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards", xshift + xCardDist * scale + 95, yshift + 180 + yCardDist * scale)
        .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", xshift + xCardDist * scale + 95, yshift + 240 + yCardDist * scale)
        .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", xshift + +xCardDist * scale + 95,
            yshift + 300 + yCardDist * scale)
        .setScaleUniform(0.07 * scale);
  }

  // generates the player decks
  public static void generateAll(int numOfPlayers) {
    // current decks
    PlayerDecks decks = new PlayerDecks();
    // spawn who is playing TODO animation
    FXGL.spawn("PlayersTurn", 95, 60);
    // spawn the players
    int curr = 0;
    // spawn the current player
    if (++curr <= numOfPlayers) {
      decks.spawnHorizontalPlayer("Player " + curr, APP_WIDTH / 4.0, APP_HEIGHT - yCardDist, 1);
    }
    // spawn a player on the left
    if (++curr <= numOfPlayers) {
      decks.spawnLeftPlayer("Player " + curr, 150.0, OPPONENT_SCALE);
    }
    // spawn a player at the top
    if (++curr <= numOfPlayers) {
      decks.spawnHorizontalPlayer("Player " + curr, 140 + APP_WIDTH / 4.0, 1, OPPONENT_SCALE);
    }
    // spawn a player at the right
    if (++curr <= numOfPlayers) {
      decks.spawnRightPlayer("Player " + curr, APP_WIDTH / 4.0 + 7 * xCardDist + 10, 150.0,
          OPPONENT_SCALE);
    }
  }
}
