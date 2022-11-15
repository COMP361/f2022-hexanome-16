package com.hexanome16.client.screens.game.players;

import com.almasb.fxgl.dsl.FXGL;

public class PlayerDecks {
  // there are 4 players hence 4 decks
  private static final double width = 1920.0;
  private static final double height = 1080.0;
  private static final double x = 150;
  private static final double y = 180;
  private static final double scale = 2.0 / 3.0;

    // generates the player decks
  public static void generateAll() {
    // spawn who is playing TODO animation
    FXGL.spawn("PlayersTurn", 95, 60);

    // spawn player1 and its hand (bottom)
    FXGL.spawn("Player", width / 4 - x, height - y + 20);
    FXGL.spawn("RedCard", width / 4, height - y);
    FXGL.spawn("GreenCard", width / 4 + x, height - y);
    FXGL.spawn("BlueCard", width / 4 + 2 * x, height - y);
    FXGL.spawn("WhiteCard", width / 4 + 3 * x, height - y);
    FXGL.spawn("BlackCard", width / 4 + 4 * x, height - y);
    FXGL.spawn("GoldCard", width / 4 + 5 * x, height - y);
    FXGL.spawn("NobleCard", width / 4 + 6 * x, height - y + 15);
    FXGL.spawn("PlayerTokens", width / 4 - 2 * x + 20, height - y);
    FXGL.spawn("ReservedNobles", width / 4 + 7 * x + 10, height - 85);
    FXGL.spawn("ReservedCards", width / 4 + 7 * x + 10, height - y);
    FXGL.spawn("ReservedCards", width / 4 + 7 * x + 70, height - y);
    FXGL.spawn("ReservedCards", width / 4 + 7 * x + 130, height - y);

    // spawn player4 and its hand (right)
    double x_shift = width / 4 + 7 * x + 10;
    double y_shift = 150;
    FXGL.spawn("Player", x_shift + x * scale, y_shift + 110).setScaleUniform(0.2 * scale);
    FXGL.spawn("RedCard", x_shift, y_shift + 120 + y * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("GreenCard", x_shift + x * scale, y_shift + 120 + y * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("BlueCard", x_shift, y_shift + 130 + 2 * y * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("WhiteCard", x_shift + x * scale, y_shift + 130 + 2 * y * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("BlackCard", x_shift, y_shift + 140 + 3 * y * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("GoldCard", x_shift + x * scale, y_shift + 140 + 3 * y * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("NobleCard", x_shift + x * scale / 2.0, y_shift + 150 + 4 * y * scale)
        .setScaleUniform(0.2 * scale);
    FXGL.spawn("PlayerTokens", x_shift, y_shift + 100).setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", x_shift + x * scale + 95, y_shift + 120 + y * scale)
        .setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards", x_shift + x * scale + 95, y_shift + 180 + y * scale)
        .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", x_shift + x * scale + 95, y_shift + 240 + y * scale)
        .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", x_shift + +x * scale + 95, y_shift + 300 + y * scale)
        .setScaleUniform(0.07 * scale);


    // spawn player3 and its hand (top)
    FXGL.spawn("Player", 140 + width / 4 - x * scale, 21).setScaleUniform(0.2 * scale);
    FXGL.spawn("RedCard", 140 + width / 4, 1).setScaleUniform(0.25 * scale);
    FXGL.spawn("GreenCard", 140 + width / 4 + (x) * scale, 1).setScaleUniform(0.25 * scale);
    FXGL.spawn("BlueCard", 140 + width / 4 + (2 * x) * scale, 1).setScaleUniform(0.25 * scale);
    FXGL.spawn("WhiteCard", 140 + width / 4 + (3 * x) * scale, 1).setScaleUniform(0.25 * scale);
    FXGL.spawn("BlackCard", 140 + width / 4 + (4 * x) * scale, 1).setScaleUniform(0.25 * scale);
    FXGL.spawn("GoldCard", 140 + width / 4 + (5 * x) * scale, 1).setScaleUniform(0.25 * scale);
    FXGL.spawn("NobleCard", 140 + width / 4 + (6 * x) * scale, 1).setScaleUniform(0.2 * scale);
    FXGL.spawn("PlayerTokens", 140 + width / 4 - (2 * x + 20) * scale, 1)
        .setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", 140 + width / 4 + 10 + (7 * x + 10) * scale, 85 * scale)
        .setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards", 140 + width / 4 + 10 + (7 * x + 10) * scale, 1)
        .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", 140 + width / 4 + 10 + (7 * x + 70) * scale, 1)
        .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", 140 + width / 4 + 10 + (7 * x + 130) * scale, 1)
        .setScaleUniform(0.07 * scale);

    // spawn player4 and its hand (left)
    FXGL.spawn("Player", x + x * scale, y_shift + 110).setScaleUniform(0.2 * scale);
    FXGL.spawn("RedCard", x, y_shift + 120 + y * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("GreenCard", x + x * scale, y_shift + 120 + y * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("BlueCard", x, y_shift + 130 + 2 * y * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("WhiteCard", x + x * scale, y_shift + 130 + 2 * y * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("BlackCard", x, y_shift + 140 + 3 * y * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("GoldCard", x + x * scale, y_shift + 140 + 3 * y * scale)
        .setScaleUniform(0.25 * scale);
    FXGL.spawn("NobleCard", x + x * scale / 2.0, y_shift + 150 + 4 * y * scale)
        .setScaleUniform(0.2 * scale);
    FXGL.spawn("PlayerTokens", x, y_shift + 100).setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", x - 60, y_shift + 120 + y * scale).setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards", x - 50, y_shift + 180 + y * scale).setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", x - 50, y_shift + 240 + y * scale).setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", x - 50, y_shift + 300 + y * scale).setScaleUniform(0.07 * scale);
  }
}
