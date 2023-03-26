package com.hexanome16.client.screens.game.components;

import static com.hexanome16.client.screens.game.GameFactory.matCoordsX;
import static com.hexanome16.client.screens.game.GameFactory.matCoordsY;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.entity.components.ViewComponent;
import javafx.scene.input.MouseEvent;

/**
 * FXGL component for cities on board.
 */
public class CityComponent extends Component {
  private static boolean[] grid = new boolean[3];
  private TransformComponent position;
  private ViewComponent view;
  private int gridX;

  /**
   * Reset city grid.
   */
  public static void reset() {
    grid = new boolean[3];
  }

  @Override
  public void onAdded() {
    view.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> pop());
    view.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> restore());
    for (int i = 0; i < grid.length; i++) {
      if (!grid[i]) {
        gridX = i;
        grid[i] = true;
        break;
      }
    }

    position.setPosition(matCoordsX + 570 + 175 * gridX, matCoordsY + 10);
  }

  private void pop() {
    position.setScaleX(0.15);
    position.setScaleY(0.15);
  }

  private void restore() {
    position.setScaleX(0.12);
    position.setScaleY(0.12);
  }
}
