package com.hexanome16.server.controllers;

import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.services.token.TokenService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test of {@link TokensController}.
 */
public class TokensControllerTests {
  private TokensController underTest;
  @Mock
  private TokenService tokenService;
  private AutoCloseable autoCloseable;

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() {
    autoCloseable = MockitoAnnotations.openMocks(this);
    underTest = new TokensController(tokenService);
  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }

  /**
   * Test get on /games/{sessionId}/twoTokens;.
   */
  @SneakyThrows
  @Test
  public void testGetTwoTokens() {
    // Arrange
    long sessionId = 1L;

    // Act
    underTest.availableTwoTokensType(sessionId);

    // Assert
    verify(tokenService).availableTwoTokensType(sessionId);
  }

  /**
   * Test get on /games/{sessionId}/threeTokens;.
   */
  @SneakyThrows
  @Test
  public void testGetThreeTokens() {
    // Arrange
    long sessionId = 1L;

    // Act
    underTest.availableThreeTokensType(sessionId);

    // Assert
    verify(tokenService).availableThreeTokensType(sessionId);
  }

  /**
   * Test Put on /games/{sessionId}/twoTokens;.
   */
  @Test
  public void testPutOneTokens() {
    // Arrange
    long sessionId = 1L;
    String access = "access";
    String type = "type";

    // Act
    underTest.takeOneToken(sessionId, access, type);

    // Assert
    verify(tokenService).takeOneToken(sessionId, access, type);
  }

  /**
   * Test Put on /games/{sessionId}/twoTokens;.
   */
  @Test
  public void testPutTwoTokens() {
    // Arrange
    long sessionId = 1L;
    String access = "access";
    String type = "type";

    // Act
    underTest.takeTwoTokens(sessionId, access, type);

    // Assert
    verify(tokenService).takeTwoTokens(sessionId, access, type);
  }

  /**
   * Test Put on /games/{sessionId}/threeTokens;.
   */
  @Test
  public void testPutThreeTokens() {
    // Arrange
    long sessionId = 1L;
    String access = "access";
    String type1 = "type1";
    String type2 = "type2";
    String type3 = "type3";

    // Act
    underTest.takeThreeTokens(sessionId, access, type1, type2, type3);

    // Assert
    verify(tokenService).takeThreeTokens(sessionId, access, type1, type2, type3);
  }

  /**
   * Test Delete on /games/{sessionId}/tokens;.
   */
  @Test
  public void testDeleteToken() {
    // Arrange
    long sessionId = 1L;
    String access = "access";
    String type = "type";

    // Act
    underTest.discardToken(sessionId, access, type);

    // Assert
    verify(tokenService).discardToken(sessionId, access, type);
  }

}
