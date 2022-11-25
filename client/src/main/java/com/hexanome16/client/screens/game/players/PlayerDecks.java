package com.hexanome16.client.screens.game.players;

import com.almasb.fxgl.dsl.FXGL;

import static com.hexanome16.client.Config.APP_HEIGHT;
import static com.hexanome16.client.Config.APP_WIDTH;
import static com.hexanome16.client.Config.OPPONENT_SCALE;

public class PlayerDecks {
  // there are 4 players hence 4 decks
  private static final double horizontalDistBetweenCards = 150; // DISTANCE BETWEEN CARDS
  private static final double verticalDistBetweenCards = 180; // OPPONENT

    // generates the player decks
  public static void generateAll() {
    // spawn who is playing TODO animation
    FXGL.spawn("PlayersTurn", 95, 60);

    // spawn player1 and its hand (bottom)
    FXGL.spawn("Player", APP_WIDTH / 4 - horizontalDistBetweenCards, APP_HEIGHT - verticalDistBetweenCards + 20);
    FXGL.spawn("RedCard", APP_WIDTH / 4, APP_HEIGHT - verticalDistBetweenCards);
    FXGL.spawn("GreenCard", APP_WIDTH / 4 + horizontalDistBetweenCards, APP_HEIGHT - verticalDistBetweenCards);
    FXGL.spawn("BlueCard", APP_WIDTH / 4 + 2 * horizontalDistBetweenCards, APP_HEIGHT - verticalDistBetweenCards);
    FXGL.spawn("WhiteCard", APP_WIDTH / 4 + 3 * horizontalDistBetweenCards, APP_HEIGHT - verticalDistBetweenCards);
    FXGL.spawn("BlackCard", APP_WIDTH / 4 + 4 * horizontalDistBetweenCards, APP_HEIGHT - verticalDistBetweenCards);
    FXGL.spawn("GoldCard", APP_WIDTH / 4 + 5 * horizontalDistBetweenCards, APP_HEIGHT - verticalDistBetweenCards);
    FXGL.spawn("NobleCard", APP_WIDTH / 4 + 6 * horizontalDistBetweenCards, APP_HEIGHT - verticalDistBetweenCards + 15);
    FXGL.spawn("PlayerTokens", APP_WIDTH / 4 - 2 * horizontalDistBetweenCards + 20, APP_HEIGHT - verticalDistBetweenCards);
    FXGL.spawn("ReservedNobles", APP_WIDTH / 4 + 7 * horizontalDistBetweenCards + 10, APP_HEIGHT - 85);
    FXGL.spawn("ReservedCards", APP_WIDTH / 4 + 7 * horizontalDistBetweenCards + 10, APP_HEIGHT - verticalDistBetweenCards);
    FXGL.spawn("ReservedCards", APP_WIDTH / 4 + 7 * horizontalDistBetweenCards + 70, APP_HEIGHT - verticalDistBetweenCards);
    FXGL.spawn("ReservedCards", APP_WIDTH / 4 + 7 * horizontalDistBetweenCards + 130, APP_HEIGHT - verticalDistBetweenCards);

    // spawn player4 and its hand (right)
    double x_shift = APP_WIDTH / 4 + 7 * horizontalDistBetweenCards + 10;
    double y_shift = 150;
    FXGL.spawn("Player", x_shift + horizontalDistBetweenCards * OPPONENT_SCALE, y_shift + 110).setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("RedCard", x_shift, y_shift + 120 + verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GreenCard", x_shift + horizontalDistBetweenCards * OPPONENT_SCALE, y_shift + 120 + verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlueCard", x_shift, y_shift + 130 + 2 * verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("WhiteCard", x_shift + horizontalDistBetweenCards * OPPONENT_SCALE, y_shift + 130 + 2 * verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlackCard", x_shift, y_shift + 140 + 3 * verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GoldCard", x_shift + horizontalDistBetweenCards * OPPONENT_SCALE, y_shift + 140 + 3 * verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("NobleCard", x_shift + horizontalDistBetweenCards * OPPONENT_SCALE / 2.0, y_shift + 150 + 4 * verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("PlayerTokens", x_shift, y_shift + 100).setScaleUniform(1.2 * OPPONENT_SCALE);
    FXGL.spawn("ReservedNobles", x_shift + horizontalDistBetweenCards * OPPONENT_SCALE + 95, y_shift + 120 + verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.1 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", x_shift + horizontalDistBetweenCards * OPPONENT_SCALE + 95, y_shift + 180 + verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", x_shift + horizontalDistBetweenCards * OPPONENT_SCALE + 95, y_shift + 240 + verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", x_shift + +horizontalDistBetweenCards * OPPONENT_SCALE + 95, y_shift + 300 + verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.07 * OPPONENT_SCALE);


    // spawn player3 and its hand (top)
    FXGL.spawn("Player", 140 + APP_WIDTH / 4 - horizontalDistBetweenCards * OPPONENT_SCALE, 21).setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("RedCard", 140 + APP_WIDTH / 4, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GreenCard", 140 + APP_WIDTH / 4 + (horizontalDistBetweenCards) * OPPONENT_SCALE, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlueCard", 140 + APP_WIDTH / 4 + (2 * horizontalDistBetweenCards) * OPPONENT_SCALE, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("WhiteCard", 140 + APP_WIDTH / 4 + (3 * horizontalDistBetweenCards) * OPPONENT_SCALE, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlackCard", 140 + APP_WIDTH / 4 + (4 * horizontalDistBetweenCards) * OPPONENT_SCALE, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GoldCard", 140 + APP_WIDTH / 4 + (5 * horizontalDistBetweenCards) * OPPONENT_SCALE, 1).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("NobleCard", 140 + APP_WIDTH / 4 + (6 * horizontalDistBetweenCards) * OPPONENT_SCALE, 1).setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("PlayerTokens", 140 + APP_WIDTH / 4 - (2 * horizontalDistBetweenCards + 20) * OPPONENT_SCALE, 1)
        .setScaleUniform(1.2 * OPPONENT_SCALE);
    FXGL.spawn("ReservedNobles", 140 + APP_WIDTH / 4 + 10 + (7 * horizontalDistBetweenCards + 10) * OPPONENT_SCALE, 85 * OPPONENT_SCALE)
        .setScaleUniform(0.1 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", 140 + APP_WIDTH / 4 + 10 + (7 * horizontalDistBetweenCards + 10) * OPPONENT_SCALE, 1)
        .setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", 140 + APP_WIDTH / 4 + 10 + (7 * horizontalDistBetweenCards + 70) * OPPONENT_SCALE, 1)
        .setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", 140 + APP_WIDTH / 4 + 10 + (7 * horizontalDistBetweenCards + 130) * OPPONENT_SCALE, 1)
        .setScaleUniform(0.07 * OPPONENT_SCALE);

    // spawn player4 and its hand (left)
    FXGL.spawn("Player", horizontalDistBetweenCards + horizontalDistBetweenCards * OPPONENT_SCALE, y_shift + 110).setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("RedCard", horizontalDistBetweenCards, y_shift + 120 + verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GreenCard", horizontalDistBetweenCards + horizontalDistBetweenCards * OPPONENT_SCALE, y_shift + 120 + verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlueCard", horizontalDistBetweenCards, y_shift + 130 + 2 * verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("WhiteCard", horizontalDistBetweenCards + horizontalDistBetweenCards * OPPONENT_SCALE, y_shift + 130 + 2 * verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("BlackCard", horizontalDistBetweenCards, y_shift + 140 + 3 * verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("GoldCard", horizontalDistBetweenCards + horizontalDistBetweenCards * OPPONENT_SCALE, y_shift + 140 + 3 * verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.25 * OPPONENT_SCALE);
    FXGL.spawn("NobleCard", horizontalDistBetweenCards + horizontalDistBetweenCards * OPPONENT_SCALE / 2.0, y_shift + 150 + 4 * verticalDistBetweenCards * OPPONENT_SCALE)
        .setScaleUniform(0.2 * OPPONENT_SCALE);
    FXGL.spawn("PlayerTokens", horizontalDistBetweenCards, y_shift + 100).setScaleUniform(1.2 * OPPONENT_SCALE);
    FXGL.spawn("ReservedNobles", horizontalDistBetweenCards - 60, y_shift + 120 + verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.1 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", horizontalDistBetweenCards - 50, y_shift + 180 + verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", horizontalDistBetweenCards - 50, y_shift + 240 + verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.07 * OPPONENT_SCALE);
    FXGL.spawn("ReservedCards", horizontalDistBetweenCards - 50, y_shift + 300 + verticalDistBetweenCards * OPPONENT_SCALE).setScaleUniform(0.07 * OPPONENT_SCALE);
  }
}
