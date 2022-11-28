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
  private void spawnHorizontalPlayer(String name, double x_pos, double y_pos, double scale){
    // iteration variable
    int i = 0;
    // spawn the player icon
    FXGL.spawn("Player", new SpawnData(x_pos - xCardDist * scale, y_pos + 20).put("name", name)).setScaleUniform(0.2 * scale);
    // card entities names
    List<String> cards = List.of("RedCard", "GreenCard", "BlueCard",  "WhiteCard", "BlackCard", "GoldCard");
    // spawn the playing cards deck
    while(i < 6) FXGL.spawn(cards.get(i), x_pos + (i++ * xCardDist)*scale, y_pos).setScaleUniform(0.25 * scale);
    // spawn the nobles deck
    FXGL.spawn("NobleCard", x_pos + (i++ * xCardDist)*scale, y_pos + 15).setScaleUniform(0.2 * scale);
    // spawn the player's bank
    FXGL.spawn("PlayerTokens", x_pos - (2 * xCardDist + 20)*scale, y_pos).setScaleUniform(1.2 * scale);
    // spawn the reserved nobles and cards
    FXGL.spawn("ReservedNobles", x_pos + (i * xCardDist + 10)*scale, y_pos + 95* scale).setScaleUniform(0.1 * scale);
    for(int j=10; j<=130; j+=60) FXGL.spawn("ReservedCards", x_pos + (i * xCardDist + j)*scale, y_pos).setScaleUniform(0.07 * scale);
  }

  // helper
  private void spawnLeftPlayer(String name, double y_shift, double scale){
    FXGL.spawn("Player", new SpawnData(xCardDist + xCardDist * scale, y_shift + 110).put("name", name)).setScaleUniform(0.2 * scale);
    FXGL.spawn("RedCard", xCardDist, y_shift + 120 + yCardDist * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("GreenCard", xCardDist + xCardDist * scale, y_shift + 120 + yCardDist * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("BlueCard", xCardDist, y_shift + 130 + 2 * yCardDist * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("WhiteCard", xCardDist + xCardDist * scale, y_shift + 130 + 2 * yCardDist * scale)
            .setScaleUniform(0.25 * scale);
    FXGL.spawn("BlackCard", xCardDist, y_shift + 140 + 3 * yCardDist * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("GoldCard", xCardDist + xCardDist * scale, y_shift + 140 + 3 * yCardDist * scale)
            .setScaleUniform(0.25 * scale);
    FXGL.spawn("NobleCard", xCardDist + xCardDist * scale / 2.0, y_shift + 150 + 4 * yCardDist * scale)
            .setScaleUniform(0.2 * scale);
    FXGL.spawn("PlayerTokens", xCardDist, y_shift + 100).setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", xCardDist - 60, y_shift + 120 + yCardDist * scale).setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards", xCardDist - 50, y_shift + 180 + yCardDist * scale).setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", xCardDist - 50, y_shift + 240 + yCardDist * scale).setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", xCardDist - 50, y_shift + 300 + yCardDist * scale).setScaleUniform(0.07 * scale);
  }
  private void spawnRightPlayer(String name, double x_shift, double y_shift, double scale){
    FXGL.spawn("Player", new SpawnData(x_shift + xCardDist * scale, y_shift + 110).put("name", name)).setScaleUniform(0.2 * scale);
    FXGL.spawn("RedCard", x_shift, y_shift + 120 + yCardDist * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("GreenCard", x_shift + xCardDist * scale, y_shift + 120 + yCardDist * scale)
            .setScaleUniform(0.25 * scale);
    FXGL.spawn("BlueCard", x_shift, y_shift + 130 + 2 * yCardDist * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("WhiteCard", x_shift + xCardDist * scale, y_shift + 130 + 2 * yCardDist * scale)
            .setScaleUniform(0.25 * scale);
    FXGL.spawn("BlackCard", x_shift, y_shift + 140 + 3 * yCardDist * scale).setScaleUniform(0.25 * scale);
    FXGL.spawn("GoldCard", x_shift + xCardDist * scale, y_shift + 140 + 3 * yCardDist * scale)
            .setScaleUniform(0.25 * scale);
    FXGL.spawn("NobleCard", x_shift + xCardDist * scale / 2.0, y_shift + 150 + 4 * yCardDist * scale)
            .setScaleUniform(0.2 * scale);
    FXGL.spawn("PlayerTokens", x_shift, y_shift + 100).setScaleUniform(1.2 * scale);
    FXGL.spawn("ReservedNobles", x_shift + xCardDist * scale + 95, y_shift + 120 + yCardDist * scale)
            .setScaleUniform(0.1 * scale);
    FXGL.spawn("ReservedCards", x_shift + xCardDist * scale + 95, y_shift + 180 + yCardDist * scale)
            .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", x_shift + xCardDist * scale + 95, y_shift + 240 + yCardDist * scale)
            .setScaleUniform(0.07 * scale);
    FXGL.spawn("ReservedCards", x_shift + + xCardDist * scale + 95, y_shift + 300 + yCardDist * scale)
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
    if( ++curr <= numOfPlayers ) decks.spawnHorizontalPlayer("Player "+curr, APP_WIDTH / 4.0, APP_HEIGHT - yCardDist, 1);
    // spawn a player on the left
    if( ++curr <= numOfPlayers ) decks.spawnLeftPlayer("Player "+curr, 150.0, OPPONENT_SCALE);
    // spawn a player at the top
    if( ++curr <= numOfPlayers ) decks.spawnHorizontalPlayer("Player "+curr, 140 + APP_WIDTH / 4.0, 1, OPPONENT_SCALE);
    // spawn a player at the right
    if( ++curr <= numOfPlayers ) decks.spawnRightPlayer("Player "+curr, APP_WIDTH / 4.0 + 7 * xCardDist + 10, 150.0 , OPPONENT_SCALE);
  }
}
