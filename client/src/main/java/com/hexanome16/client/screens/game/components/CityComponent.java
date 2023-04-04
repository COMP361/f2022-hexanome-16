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
  private static CityComponent[] grid = new CityComponent[3];
  private TransformComponent position;
  private ViewComponent view;
  private final String cityHash;
  private int gridX;

  /**
   * Create a new noble component.
   *
   * @param hash noble hash.
   */
  public CityComponent(String hash) {
    this.cityHash = hash;
  }

  /**
   * Reset city grid.
   */
  public static void reset() {
    grid = new CityComponent[3];
  }

  @Override
  public void onAdded() {
    view.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> pop());
    view.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> restore());
    for (int i = 0; i < grid.length; i++) {
      if (grid[i] == null) {
        gridX = i;
        grid[i] = this;
        break;
      }
    }

    position.setPosition(matCoordsX + 570 + 175 * gridX, matCoordsY + 10);
  }

  /**
   * Getter for the gird of on board city.
   *
   * @return The gird of on board city.
   */
  public static CityComponent[] getGrid() {
    return grid;
  }

  /**
   * Getter for city MD5.
   *
   * @return city MD5.
   */
  public String getCityHash() {
    return cityHash;
  }

  private void pop() {
    position.setScaleX(0.15);
    position.setScaleY(0.15);
  }

  private void restore() {
    position.setScaleX(0.12);
    position.setScaleY(0.12);
  }

  /**
   * Removes this city from the game board.
   */
  public void removeFromMat() {
    entity.removeFromWorld();
    for (int i = 0; i < grid.length; i++) {
      if (grid[i] == this) {
        grid[i] = null;
        break;
      }
    }
  }
}
