package com.hexanome16.client;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * This class contains configuration options for the game.
 */
public class Config {
  public static final String APP_VERSION = "0.0.1";
  public static final String APP_TITLE = "Splendor";
  public static final int APP_WIDTH = 1920;
  public static final int APP_HEIGHT = (int) (APP_WIDTH * 9 / 16.0);
  public static final Point2D CURSOR_HOTSPOT = new Point2D(40.0, 10.0);
  public static final Color PRIMARY_COLOR = Color.rgb(78, 147, 180);
  public static final Color SECONDARY_COLOR = Color.rgb(249, 161, 89);
  public static final double OPPONENT_SCALE = 2.0 / 3.0;


}