package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.hexanome16.common.models.Player;
import com.hexanome16.common.models.sessions.SaveGameJson;
import com.hexanome16.server.ReflectionUtils;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.savegame.SaveGame;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.SavegameService;
import com.hexanome16.server.services.game.SavegameServiceInterface;
import com.hexanome16.server.services.winconditions.WinCondition;
import com.hexanome16.server.util.UrlUtils;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * Tests for {@link SavegameService}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SavegameServiceTests {
  private SavegameServiceInterface savegameService;
  private GameManagerServiceInterface gameManagerService;
  private final SaveGame[] testSaveGames = new SaveGame[5];
  private final String gameServer = WinCondition.BASE.getGameServiceJson().getName();

  /**
   * Setup.
   */
  @BeforeEach
  public void setup() {
    UrlUtils urlUtilsMock = Mockito.mock(UrlUtils.class);
    when(urlUtilsMock.createLobbyServiceUri(anyString(), anyString())).thenAnswer(invocation -> {
      String path = invocation.getArgument(0);
      String params = invocation.getArgument(1);
      return URI.create("http://localhost:4242" + path + "?" + params);
    });
    RestTemplateBuilder restTemplateBuilderMock = Mockito.mock(RestTemplateBuilder.class);
    RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    savegameService = new SavegameService(urlUtilsMock, restTemplateBuilderMock,
        new DummyAuthService());
    Field path = ReflectionUtils.getFieldAndSetPublic(savegameService, "savegamesPath");
    Field username = ReflectionUtils.getFieldAndSetPublic(savegameService, "gsUsername");
    Field password = ReflectionUtils.getFieldAndSetPublic(savegameService, "gsPassword");
    if (path == null || username == null || password == null) {
      throw new RuntimeException("Could not get required fields");
    } else {
      try {
        path.set(savegameService, "./data/savegames");
        username.set(savegameService, "xox");
        password.set(savegameService, "laaPhie*aiN0");
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    gameManagerService = DummyGameManagerService.getDummyGameManagerService();
  }

  @Test
  @Order(1)
  public void testSavegame() {
    savegameService.saveGame(
        gameManagerService.getGame(DummyAuths.validSessionIds.get(0)), "test_savegame",
        new SaveGameJson("test_savegame", gameServer,
            DummyAuths.validPlayerList.stream().map(Player::getName).toArray(String[]::new)));
    assertTrue(new File("./data/savegames/test_savegame.json").exists());
  }

  @Test
  @Order(2)
  public void testLoadgame() {
    testSaveGames[0] = savegameService.loadGame("test_savegame");
    assertNotNull(gameManagerService.getGame(DummyAuths.validSessionIds.get(0)));
  }

  @Test
  @Order(3)
  public void testDeletegame() {
    savegameService.deleteSavegame(gameServer, "test_savegame");
    assertFalse(new File("./data/savegames/test_savegame.json").exists());
  }

  @Test
  @Order(4)
  public void testDeleteAllSavegames() {
    for (int i = 0; i < testSaveGames.length; i++) {
      savegameService.saveGame(
          gameManagerService.getGame(DummyAuths.validSessionIds.get(0)), "test_savegame" + i,
          new SaveGameJson("test_savegame" + i, gameServer,
              DummyAuths.validPlayerList.stream().map(Player::getName).toArray(String[]::new)));
    }
    savegameService.deleteAllSavegames(gameServer);
    for (int i = 0; i < testSaveGames.length; i++) {
      assertFalse(new File("./data/savegames/test_savegame" + i + ".json").exists());
    }
  }
}
