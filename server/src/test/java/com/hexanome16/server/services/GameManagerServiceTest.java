package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import com.hexanome16.server.models.winconditions.WinCondition;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameManagerServiceTest {
  private GameManagerService gameManagerService;

  @BeforeEach
  void setUp() {
    gameManagerService = new GameManagerService();
  }

  /**
   * Should have non-null empty map on creation.
   */
  @Test
  void testNonNullEmptyMapOnCreation() {
    Field field;
    try {
      field = GameManagerService.class.getDeclaredField("gameMap");

      field.setAccessible(true);

      var map = ((Map<Long, Game>) field.get(gameManagerService));
      assertNotNull(map);
      assertTrue(map.keySet().isEmpty());
    } catch (NoSuchFieldException e) {
      fail("gameMap not in GameManagerService");
    } catch (IllegalAccessException e) {
      fail("set accessible did not work");
    }
  }

  @Test
  void testCreateGame() {
    String response = createValidGame(123L);
    Field field;
    Game testGame = null;
    try {
      field = GameManagerService.class.getDeclaredField("gameMap");

      field.setAccessible(true);

      testGame = ((Map<Long, Game>) field.get(gameManagerService)).get(123L);
    } catch (NoSuchFieldException e) {
      fail("gameMap not in GameManagerService");
    } catch (IllegalAccessException e) {
      fail("set accessible did not work");
    }
    assertNotNull(testGame);
    assertEquals("success", response);
  }

  /**
   * Create valid game inside gameManagerService and return response.
   *
   * @return response of gameManagerService.createGame
   */
  private String createValidGame(long sessionId) {
    WinCondition winCondition = new BaseWinCondition();
    SessionJson testJson =
        new SessionJson(PlayerDummies.dummies, PlayerDummies.dummies[0].getName(), "",
            winCondition);
    return gameManagerService.createGame(sessionId, testJson);
  }

  @Test
  void getGame() {
    createValidGame(123L);
    createValidGame(124L);

    Field field;
    Game testGame = null;
    Game testGame2 = null;
    try {
      field = GameManagerService.class.getDeclaredField("gameMap");

      field.setAccessible(true);

      testGame = ((Map<Long, Game>) field.get(gameManagerService)).get(123L);
      testGame2 = ((Map<Long, Game>) field.get(gameManagerService)).get(124L);
    } catch (NoSuchFieldException e) {
      fail("gameMap not in GameManagerService");
    } catch (IllegalAccessException e) {
      fail("set accessible did not work");
    }

    assertEquals(testGame, gameManagerService.getGame(123L));
    assertNotEquals(testGame, gameManagerService.getGame(124L));
    assertEquals(testGame2, gameManagerService.getGame(124L));

    assertNull(gameManagerService.getGame(125L));
  }
}