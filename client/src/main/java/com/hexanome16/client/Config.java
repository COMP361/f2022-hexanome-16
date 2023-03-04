package com.hexanome16.client;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FontFactory;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * This class contains configuration options for the game.
 */
public class Config {
  /**
   * The app version.
   */
  public static final String APP_VERSION = "0.0.1";
  /**
   * The app title.
   */
  public static final String APP_TITLE = "Splendor";
  /**
   * The initial app width.
   */
  public static final int APP_WIDTH = 1920;
  /**
   * The initial app height.
   */
  public static final int APP_HEIGHT = (int) (APP_WIDTH * 9 / 16.0);
  /**
   * The initial cursor position.
   */
  public static final Point2D CURSOR_HOTSPOT = new Point2D(40.0, 10.0);
  /**
   * The primary color used in UI.
   */
  public static final Color PRIMARY_COLOR = Color.rgb(48, 4, 24);
  /**
   * The secondary color used in UI.
   */
  public static final Color SECONDARY_COLOR = Color.rgb(207, 251, 231);
  /**
   * The scaling factor for opponents cards.
   */
  public static final double OPPONENT_SCALE = 2.0 / 3.0;
  /**
   * THE FONT.
   */
  public static final FontFactory CURSIVE_FONT_FACTORY = FXGL.getAssetLoader()
      .loadFont("EnchantedLand.otf");
}
