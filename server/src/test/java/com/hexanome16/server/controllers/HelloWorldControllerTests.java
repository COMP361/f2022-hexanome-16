package com.hexanome16.server.controllers;

import static org.assertj.core.api.BDDAssertions.then;

import com.hexanome16.server.models.Greeting;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class contains tests for the server.
 */
@WebMvcTest(HelloWorldController.class)
public class HelloWorldControllerTests {
  /**
   * Test say hello.
   */
  @Test
  public void testSayHello() {
    ResponseEntity<Greeting> response = new HelloWorldController().sayHello("World");
    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(response.getBody()).isEqualTo(new Greeting(1, "Hello, World!"));
  }
}
