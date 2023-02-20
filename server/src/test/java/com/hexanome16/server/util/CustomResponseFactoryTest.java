package com.hexanome16.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class CustomResponseFactoryTest {

  @Test
  void getErrorResponse() {
    // Arrange
    var status = CustomHttpResponses.BAD_LEVEL_INFO.getStatus();
    var body = CustomHttpResponses.BAD_LEVEL_INFO.getBody();

    // Act
    var response = CustomResponseFactory.getErrorResponse(CustomHttpResponses.BAD_LEVEL_INFO);

    // Assert
    assertEquals(status, response.getStatusCode());
    assertTrue(response.hasBody());
    assertEquals(body, response.getBody());
  }

  @Test
  void getCustomErrorResponse() {
    // Arrange
    var status = CustomHttpResponses.BAD_LEVEL_INFO.getStatus();
    var body = "Testing error";

    // Act
    var response =
        CustomResponseFactory.getCustomErrorResponse(CustomHttpResponses.BAD_LEVEL_INFO, body);

    // Assert
    assertEquals(status, response.getStatusCode());
    assertTrue(response.hasBody());
    assertEquals(body, response.getBody());
  }

  @Test
  void getDeferredErrorResponse() {
    // Arrange
    var status = CustomHttpResponses.BAD_LEVEL_INFO.getStatus();
    var body = CustomHttpResponses.BAD_LEVEL_INFO.getBody();

    // Act
    var response =
        CustomResponseFactory.getDeferredErrorResponse(CustomHttpResponses.BAD_LEVEL_INFO);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(status, result.getStatusCode());
    assertTrue(result.hasBody());
    assertEquals(body, result.getBody());
  }

  @Test
  void getDeferredCustomErrorResponse() {
    // Arrange
    var status = CustomHttpResponses.BAD_LEVEL_INFO.getStatus();
    var body = "Testing error";

    // Act
    var response =
        CustomResponseFactory.getDeferredCustomErrorResponse(CustomHttpResponses.BAD_LEVEL_INFO,
            body);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(status, result.getStatusCode());
    assertTrue(result.hasBody());
    assertEquals(body, result.getBody());
  }
}