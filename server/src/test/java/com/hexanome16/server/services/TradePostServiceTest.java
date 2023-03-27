package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.ServiceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Test for trade post service.
 */
public class TradePostServiceTest {
  private final com.fasterxml.jackson.databind.ObjectMapper objectMapper =
      new com.fasterxml.jackson.databind.ObjectMapper().registerModule(new ParameterNamesModule(
          JsonCreator.Mode.PROPERTIES));
  private final SessionJson payload = new SessionJson();
  private DummyAuthService dummyAuthService;
  private GameManagerServiceInterface gameManagerMock;
  private TradePostService tradePostService;
  private Game validMockGame;
  private ServiceUtils serviceUtils;

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws JsonProcessingException {
    validMockGame =
        Game.create(DummyAuths.validSessionIds.get(0), PlayerDummies.validDummies, "imad", "",
            WinCondition.TRADEROUTES);
    dummyAuthService = new DummyAuthService();
    gameManagerMock =
        DummyGameManagerService.getDummyGameManagerService();
    serviceUtils = DummyServiceUtils.getDummyServiceUtils();
    tradePostService = new TradePostService(gameManagerMock, serviceUtils);
  }

  /**
   * Testing getPlayerTradePosts.
   *
   * @throws JsonProcessingException possible json processing error
   */
  @Test
  public void testGetPlayerTradePosts() throws JsonProcessingException {
    var response = tradePostService.getPlayerTradePosts(DummyAuths.validSessionIds.get(0),
        DummyAuths.validPlayerList.get(0).getName());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    response = tradePostService.getPlayerTradePosts(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validPlayerList.get(0).getName());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getStatusCode().value());
    response = tradePostService.getPlayerTradePosts(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidPlayerList.get(0).getName());
    assertEquals(CustomHttpResponses.PLAYER_NOT_IN_GAME.getStatus(),
        response.getStatusCode().value());
  }
}
