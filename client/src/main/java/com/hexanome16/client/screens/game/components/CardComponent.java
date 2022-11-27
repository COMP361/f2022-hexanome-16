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
  private static boolean[] level_one_grid = new boolean[4];
  private static boolean[] level_two_grid = new boolean[4];
  private static boolean[] level_three_grid = new boolean[4];
  private final Level level;
  public String texture;
  private ViewComponent view;
  private TransformComponent position;
  private boolean moving = false;
  private Direction direction;
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
    System.out.println("card hash: "+cardMD5);
  }

  /**
   * Reset the deck when exiting the game, might need to be deleted in the future.
   */
  public static void reset() {
    level_one_grid = new boolean[4];
    level_two_grid = new boolean[4];
    level_three_grid = new boolean[4];
  }

  @Override
  public void onUpdate(double tpf) {
    if (moving) {
      // moving(direction);
      entity.getTransformComponent().translateY(10);

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

    FXGL.getEventBus().addEventHandler(SplendorEvents.BOUGHT, e -> {
      if (e.eventEntity == entity) {
        buyCard();
      }
    });

    view.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> OpenPrompt.openPrompt(entity));
    view.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> pop());
    view.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> restore());
    adding = true;
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

  private void moving(Direction direction) {
    switch (direction) {
      default:
      case UP:
        double diffUp = position.getY() - 50;

        if (diffUp > 0) {
          position.translateY(-10);
        } else {
          moving = false;
          view.setVisible(false);
        }
        break;
      case DOWN:
        double diffDown = 900 - position.getY();

        if (diffDown > 0) {
          position.translateY(10);
        } else {
          moving = false;
          view.setVisible(false);
        }
        break;
      case LEFT:
        double diffLeft = position.getX() - 100;

        if (diffLeft > 0) {
          position.translateX(-10);
        } else {
          moving = false;
          view.setVisible(false);
        }
        break;
      case RIGHT:
        double diffRight = 1800 - position.getX();

        if (diffRight > 0) {
          position.translateX(10);
        } else {
          moving = false;
          view.setVisible(false);
        }
        break;
    }
  }

  private void addToMat(boolean[] grid) {
    for (int i = 0; i < grid.length; i++) {
      if (!grid[i]) {
        gridX = i;
        grid[i] = true;
        break;
      }
    }
  }

  private void buyCard() {
    moving = true;
    direction = Direction.DOWN;
    purchased = false;
    switch (level) {
      default:
      case ONE:
        level_one_grid[gridX] = false;
        GameScreen.addLevelOneCard();
        break;
      case TWO:
        level_two_grid[gridX] = false;
        GameScreen.addLevelTwoCard();
        break;
      case THREE:
        level_three_grid[gridX] = false;
        GameScreen.addLevelThreeCard();
        break;
    }
  }

  public PriceMap getPriceMap() {
    return priceMap;
  }

  enum Direction {
    DOWN,
    RIGHT,
    LEFT,
    UP
  }
}
