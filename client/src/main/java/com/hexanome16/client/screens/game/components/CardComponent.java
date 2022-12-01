package com.hexanome16.client.screens.game.components;

import static com.hexanome16.client.screens.game.GameFactory.matCoordsX;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.hexanome16.client.screens.game.Level;
import com.hexanome16.client.screens.game.PriceMap;
import com.hexanome16.client.screens.game.prompts.OpenPrompt;
import javafx.scene.input.MouseEvent;

/**
 * FXGL component for development cards on board.
 */
public class CardComponent extends Component {
  /**
   * The constant level_one_grid.
   */
  public static CardComponent[] level_one_grid = new CardComponent[4];
  /**
   * The constant level_two_grid.
   */
  public static CardComponent[] level_two_grid = new CardComponent[4];
  /**
   * The constant level_three_grid.
   */
  public static CardComponent[] level_three_grid = new CardComponent[4];
  private final Level level;
  private final boolean purchased = false;
  /**
   * The Texture.
   */
  public String texture;
  private ViewComponent view;
  private TransformComponent position;
  private boolean fading = false;
  private boolean adding = false;
  private int gridX;
  private final String cardHash;

  private final PriceMap priceMap;

  /**
   * Creates a new card fxgl component.
   *
   * @param id       card id
   * @param level    card level
   * @param texture  card texture
   * @param priceMap the price of the card
   * @param cardHash MD5 hash of the card
   */
  public CardComponent(long id, Level level, String texture, PriceMap priceMap, String cardHash) {
    this.level = level;
    this.texture = texture;
    this.priceMap = priceMap;
    this.cardHash = cardHash;
  }

  /**
   * Reset the deck when exiting the game, might need to be deleted in the future.
   */
  public static void reset() {
    level_one_grid = new CardComponent[4];
    level_two_grid = new CardComponent[4];
    level_three_grid = new CardComponent[4];
  }

  @Override
  public void onUpdate(double tpf) {
    if (fading) {
      double opacity = entity.getViewComponent().getOpacity();
      if (opacity > 0) {
        entity.getViewComponent().setOpacity(opacity - 0.1);
      } else {
        entity.removeFromWorld();
      }
    } else if (adding) {
      double diff = (matCoordsX + 140 + 138 * gridX) - position.getX();

      if (diff > 0) {
        position.translateX(5);
      } else {
        adding = false;
      }
    }
  }

  @Override
  public void onAdded() {
    view.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> OpenPrompt.openPrompt(entity));
    view.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> pop());
    view.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> restore());
    switch (level) {
      case ONE -> addToMat(level_one_grid);
      case TWO -> addToMat(level_two_grid);
      case THREE -> addToMat(level_three_grid);
      default -> throw new IllegalArgumentException("Something is sussy about this.");
    }
  }


  private void pop() {
    position.setScaleX(0.18);
    position.setScaleY(0.18);
  }

  private void restore() {
    position.setScaleX(0.15);
    position.setScaleY(0.15);
  }

  /**
   * Adds this card to the game board.
   *
   * @param grid game board coords
   */
  private void addToMat(CardComponent[] grid) {
    for (int i = 0; i < grid.length; i++) {
      if (grid[i] == null) {
        gridX = i;
        grid[i] = this;
        adding = true;
        break;
      }
    }
  }

  /**
   * Removes this card from the game board.
   */
  public void removeFromMat() {
    this.fading = true;
    CardComponent[] grid = switch (level) {
      case ONE -> level_one_grid;
      case TWO -> level_two_grid;
      case THREE -> level_three_grid;
    };
    for (int i = 0; i < grid.length; i++) {
      if (grid[i] == this) {
        grid[i] = null;
        break;
      }
    }
  }

  /**
   * Gets price map.
   *
   * @return the price map
   */
  public PriceMap getPriceMap() {
    return priceMap;
  }

  /**
   * Gets card hash.
   *
   * @return the card hash
   */
  public String getCardHash() {
    return cardHash;
  }

}
