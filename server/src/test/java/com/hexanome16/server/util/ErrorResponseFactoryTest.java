package com.hexanome16.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ErrorResponseFactoryTest {

  @Test
  void getErrorResponse() {
    var status = ErrorResponses.BAD_LEVEL_INFO.getStatus();
    var body = ErrorResponses.BAD_LEVEL_INFO.getBody();
    var response = ErrorResponseFactory.getErrorResponse(ErrorResponses.BAD_LEVEL_INFO);
    assertEquals(status, response.getStatusCode());
    assertTrue(response.hasBody());
    assertEquals(body, response.getBody());
  }

  @Test
  void getCustomErrorResponse() {
    var status = ErrorResponses.BAD_LEVEL_INFO.getStatus();
    var body = "Testing error";
    var response = ErrorResponseFactory.getCustomErrorResponse(ErrorResponses.BAD_LEVEL_INFO, body);
    assertEquals(status, response.getStatusCode());
    assertTrue(response.hasBody());
    assertEquals(body, response.getBody());
  }
}