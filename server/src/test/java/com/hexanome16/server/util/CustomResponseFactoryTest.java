package com.hexanome16.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CustomResponseFactoryTest {

  @Test
  void getErrorResponse() {
    var status = CustomHttpResponses.BAD_LEVEL_INFO.getStatus();
    var body = CustomHttpResponses.BAD_LEVEL_INFO.getBody();
    var response = CustomResponseFactory.getErrorResponse(CustomHttpResponses.BAD_LEVEL_INFO);
    assertEquals(status, response.getStatusCode());
    assertTrue(response.hasBody());
    assertEquals(body, response.getBody());
  }

  @Test
  void getCustomErrorResponse() {
    var status = CustomHttpResponses.BAD_LEVEL_INFO.getStatus();
    var body = "Testing error";
    var response =
        CustomResponseFactory.getCustomErrorResponse(CustomHttpResponses.BAD_LEVEL_INFO, body);
    assertEquals(status, response.getStatusCode());
    assertTrue(response.hasBody());
    assertEquals(body, response.getBody());
  }
}