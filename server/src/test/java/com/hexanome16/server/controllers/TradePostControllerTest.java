package com.hexanome16.server.controllers;

import static org.mockito.Mockito.verify;

import com.hexanome16.server.services.TradePostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TradePostControllerTest {

  private TradePostController underTest;
  @Mock
  private TradePostService tradePostService;
  private AutoCloseable autoCloseable;

  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
    underTest = new TradePostController(tradePostService);
  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }

  @Test
  void testGetPlayerTradePostsInvokesService() {
    // Arrange
    long id = 1L;
    String username = "username";

    // Act
    underTest.getPlayerTradePosts(id, username);

    // Assert
    verify(tradePostService).getPlayerTradePosts(id, username);
  }
}
