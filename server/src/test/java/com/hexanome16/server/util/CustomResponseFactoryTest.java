package com.hexanome16.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.common.util.CustomHttpResponses;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class CustomResponseFactoryTest {

  @Test
  void getErrorResponse() {
    // Arrange
    final var status = CustomHttpResponses.BAD_LEVEL_INFO.getStatus();
    final var body = CustomHttpResponses.BAD_LEVEL_INFO.getBody();

    // Act
    var response = CustomResponseFactory.getResponse(CustomHttpResponses.BAD_LEVEL_INFO);

    // Assert
    assertEquals(status, response.getStatusCode().value());
    assertTrue(response.hasBody());
    assertEquals(body, response.getBody());
  }

  @Test
  void getCustomErrorResponse() {
    // Arrange
    final var status = CustomHttpResponses.BAD_LEVEL_INFO.getStatus();
    final var body = "Testing error";

    // Act
    var response =
        CustomResponseFactory.getCustomResponse(CustomHttpResponses.BAD_LEVEL_INFO, body, null);

    // Assert
    assertEquals(status, response.getStatusCode().value());
    assertTrue(response.hasBody());
    assertEquals(body, response.getBody());
  }

  @Test
  void getDeferredErrorResponse() {
    // Arrange
    final var status = CustomHttpResponses.BAD_LEVEL_INFO.getStatus();
    final var body = CustomHttpResponses.BAD_LEVEL_INFO.getBody();

    // Act
    var response =
        CustomResponseFactory.getDeferredResponse(CustomHttpResponses.BAD_LEVEL_INFO);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(status, result.getStatusCode().value());
    assertTrue(result.hasBody());
    assertEquals(body, result.getBody());
  }

  @Test
  void getDeferredCustomErrorResponse() {
    // Arrange
    final var status = CustomHttpResponses.BAD_LEVEL_INFO.getStatus();
    final var body = "Testing error";

    // Act
    var response =
        CustomResponseFactory.getDeferredCustomResponse(CustomHttpResponses.BAD_LEVEL_INFO,
            body, null);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(status, result.getStatusCode().value());
    assertTrue(result.hasBody());
    assertEquals(body, result.getBody());
  }
}
