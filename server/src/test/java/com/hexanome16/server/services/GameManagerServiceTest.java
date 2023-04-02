package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.SavegameService;
import com.hexanome16.server.services.game.SavegameServiceInterface;
import com.hexanome16.server.services.winconditions.WinCondition;
import com.hexanome16.server.util.UrlUtils;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

class GameManagerServiceTest {
  private GameManagerService gameManagerService;

  @BeforeEach
  void setUp() {
    UrlUtils urlUtilsMock = Mockito.mock(UrlUtils.class);
    when(urlUtilsMock.createLobbyServiceUri(anyString(), anyString())).thenAnswer(invocation -> {
      String path = invocation.getArgument(0);
      String params = invocation.getArgument(1);
      return URI.create("http://localhost:4242" + path + "?" + params);
    });
    RestTemplateBuilder restTemplateBuilderMock = Mockito.mock(RestTemplateBuilder.class);
    RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    SavegameServiceInterface savegameServiceInterface = Mockito.mock(SavegameService.class);
    gameManagerService = new GameManagerService(savegameServiceInterface, restTemplateBuilderMock,
        urlUtilsMock, new DummyAuthService());
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
    SessionJson testJson =
        new SessionJson(PlayerDummies.validDummies, PlayerDummies.validDummies[0].getName(), "",
            WinCondition.BASE.getGameServiceJson().getName());
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
