package com.hexanome16.client.screens.game.players;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;

import java.util.List;

import static com.hexanome16.client.Config.APP_HEIGHT;
import static com.hexanome16.client.Config.APP_WIDTH;
import static com.hexanome16.client.Config.OPPONENT_SCALE;

public class PlayerDecks {
  // there are 4 players hence 4 decks
  private static final double xCardDist = 150; // horizontal distance between cards (this is for the current player)
  private static final double yCardDist = 180; // vertical distance between cards (this is for opponents)

  // helper
  private void spawnPlayer(String name, double x_pos, double y_pos, double scale){
    // spawn the player icon
    FXGL.spawn("Player", new SpawnData(x_pos - xCardDist, y_pos + 20).put("name", name));
    // card entities names
    List<String> cards = List.of("RedCard", "GreenCard", "BlueCard",  "WhiteCard", "BlackCard", "GoldCard");
    // spawn the playing cards deck
    for(int i=0; i < 6 ; i++) FXGL.spawn(cards.get(i), x_pos + i * xCardDist, y_pos);
    // spawn the nobles deck
    FXGL.spawn("NobleCard", x_pos + 6 * xCardDist, y_pos + 15);
    // spawn the player's bank
    FXGL.spawn("PlayerTokens", x_pos - 2 * xCardDist + 20, y_pos);
    // spawn the reserved nobles and cards
    FXGL.spawn("ReservedNobles", x_pos + 7 * xCardDist + 10, APP_HEIGHT - 85);
    FXGL.spawn("ReservedCards", x_pos + 7 * xCardDist + 10, y_pos);
    FXGL.spawn("ReservedCards", x_pos + 7 * xCardDist + 70, y_pos);
    FXGL.spawn("ReservedCards", x_pos + 7 * xCardDist + 130, y_pos);
  }

  // generates the player decks
  public static void generateAll(int numOfPlayers) {
    // current decks
    PlayerDecks decks = new PlayerDecks();
    // spawn who is playing TODO animation
    FXGL.spawn("PlayersTurn", 95, 60);
    // spawn the current player
    decks.spawnPlayer("Player 1", APP_WIDTH / 4.0, APP_HEIGHT - yCardDist, 1);
    // TODO test this

    // spawn player4 and its hand (right)
    double x_shift = APP_WIDTH / 4 + 7 * xCardDist + 10;
    double y_shift = 150;
    FXGL.spawn("Player", x_shift + xCardDist * OPPONENT_SCALE, y_shift + 110).setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("RedCard", x_shift, y_shift + 120 + yCardDist * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GreenCard", x_shift + xCardDist * OPPONENT_SCALE, y_shift + 120 + yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlueCard", x_shift, y_shift + 130 + 2 * yCardDist * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("WhiteCard", x_shift + xCardDist * OPPONENT_SCALE, y_shift + 130 + 2 * yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlackCard", x_shift, y_shift + 140 + 3 * yCardDist * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GoldCard", x_shift + xCardDist * OPPONENT_SCALE, y_shift + 140 + 3 * yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("NobleCard", x_shift + xCardDist * OPPONENT_SCALE / 2.0, y_shift + 150 + 4 * yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("PlayerTokens", x_shift, y_shift + 100).setScaleUniform(1.2 * OPPONENT_SCALE);
    FXGL.spawn("ReservedNobles", x_shift + xCardDist * OPPONENT_SCALE + 95, y_shift + 120 + yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.1 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", x_shift + xCardDist * OPPONENT_SCALE + 95, y_shift + 180 + yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", x_shift + xCardDist * OPPONENT_SCALE + 95, y_shift + 240 + yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", x_shift + +xCardDist * OPPONENT_SCALE + 95, y_shift + 300 + yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.07 * OPPONENT_SCALE);


    // spawn player3 and its hand (top)
    FXGL.spawn("Player", 140 + APP_WIDTH / 4 - xCardDist * OPPONENT_SCALE, 21).setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("RedCard", 140 + APP_WIDTH / 4, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GreenCard", 140 + APP_WIDTH / 4 + (xCardDist) * OPPONENT_SCALE, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlueCard", 140 + APP_WIDTH / 4 + (2 * xCardDist) * OPPONENT_SCALE, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("WhiteCard", 140 + APP_WIDTH / 4 + (3 * xCardDist) * OPPONENT_SCALE, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlackCard", 140 + APP_WIDTH / 4 + (4 * xCardDist) * OPPONENT_SCALE, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GoldCard", 140 + APP_WIDTH / 4 + (5 * xCardDist) * OPPONENT_SCALE, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("NobleCard", 140 + APP_WIDTH / 4 + (6 * xCardDist) * OPPONENT_SCALE, 1).setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("PlayerTokens", 140 + APP_WIDTH / 4 - (2 * xCardDist + 20) * OPPONENT_SCALE, 1)
        .setScaleUniform(1.2 * OPPONENT_SCALE);
    FXGL.spawn("ReservedNobles", 140 + APP_WIDTH / 4 + 10 + (7 * xCardDist + 10) * OPPONENT_SCALE, 85 * OPPONENT_SCALE)
        .setScaleUniform(0.1 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", 140 + APP_WIDTH / 4 + 10 + (7 * xCardDist + 10) * OPPONENT_SCALE, 1)
        .setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", 140 + APP_WIDTH / 4 + 10 + (7 * xCardDist + 70) * OPPONENT_SCALE, 1)
        .setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", 140 + APP_WIDTH / 4 + 10 + (7 * xCardDist + 130) * OPPONENT_SCALE, 1)
        .setScaleUniform(0.07 * OPPONENT_SCALE);

    // spawn player4 and its hand (left)
    FXGL.spawn("Player", xCardDist + xCardDist * OPPONENT_SCALE, y_shift + 110).setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("RedCard", xCardDist, y_shift + 120 + yCardDist * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GreenCard", xCardDist + xCardDist * OPPONENT_SCALE, y_shift + 120 + yCardDist * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlueCard", xCardDist, y_shift + 130 + 2 * yCardDist * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("WhiteCard", xCardDist + xCardDist * OPPONENT_SCALE, y_shift + 130 + 2 * yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlackCard", xCardDist, y_shift + 140 + 3 * yCardDist * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GoldCard", xCardDist + xCardDist * OPPONENT_SCALE, y_shift + 140 + 3 * yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("NobleCard", xCardDist + xCardDist * OPPONENT_SCALE / 2.0, y_shift + 150 + 4 * yCardDist * OPPONENT_SCALE)
        .setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("PlayerTokens", xCardDist, y_shift + 100).setScaleUniform(1.2 * OPPONENT_SCALE);
    FXGL.spawn("ReservedNobles", xCardDist - 60, y_shift + 120 + yCardDist * OPPONENT_SCALE).setScaleUniform(0.1 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", xCardDist - 50, y_shift + 180 + yCardDist * OPPONENT_SCALE).setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", xCardDist - 50, y_shift + 240 + yCardDist * OPPONENT_SCALE).setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", xCardDist - 50, y_shift + 300 + yCardDist * OPPONENT_SCALE).setScaleUniform(0.07 * OPPONENT_SCALE);
  }
}
