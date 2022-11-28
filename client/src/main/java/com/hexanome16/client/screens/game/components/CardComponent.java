package com.hexanome16.client.screens.game.components;

import static com.hexanome16.client.screens.game.GameFactory.matCoordsX;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.Level;
import com.hexanome16.client.screens.game.PriceMap;
import com.hexanome16.client.screens.game.prompts.OpenPrompt;
import com.hexanome16.client.screens.game.prompts.components.events.SplendorEvents;
import javafx.scene.input.MouseEvent;

/**
 * FXGL component for development cards on board.
 */
public class CardComponent extends Component {
  public static CardComponent[] level_one_grid = new CardComponent[4];
  public static CardComponent[] level_two_grid = new CardComponent[4];
  public static CardComponent[] level_three_grid = new CardComponent[4];
  private final Level level;

  public String texture;
  private ViewComponent view;
  private TransformComponent position;
  private boolean fading = false;
  private boolean adding = false;
  private int gridX;

  private boolean purchased = false;

  private String cardMD5 = "";

  private PriceMap priceMap = new PriceMap();


  public CardComponent(long id, Level level, String texture, PriceMap priceMap, String cardMD5) {
    this.level = level;
    this.texture = texture;
    this.priceMap = priceMap;
    this.cardMD5 = cardMD5;
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
      Double opacity = entity.getViewComponent().getOpacity();
      if(opacity > 0) {
        entity.getViewComponent().setOpacity(opacity - 0.1);
      }else {
        entity.removeFromWorld();
      }
    } else if (adding) {
      double diff = (matCoordsX + 140 + 138 * gridX) - position.getX();

      if (diff > 0) {
        position.translateX(5);
      } else {
        System.out.println("diff: " + position.getX() + " " + position.getY());
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
      default:
      case ONE:
        addToMat(level_one_grid);
        break;
      case TWO:
        addToMat(level_two_grid);
        break;
      case THREE:
        addToMat(level_three_grid);
        break;
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

  public void removeFromMat() {
    this.fading = true;
    CardComponent[] grid;
    switch (level) {
      default:
      case ONE:
        grid = level_one_grid;
        break;
      case TWO:
        grid = level_two_grid;
        break;
      case THREE:
        grid = level_three_grid;
        break;
    }
    for (int i = 0; i < grid.length; i++) {
      if (grid[i] == this) {
        grid[i] = null;
        break;
      }
    }
  }

  public PriceMap getPriceMap() {
    return priceMap;
  }

  public String getCardHash() {
    return cardMD5;
  }

}
