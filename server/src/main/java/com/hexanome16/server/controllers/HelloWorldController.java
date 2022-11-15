package com.hexanome16.server.controllers;

import com.hexanome16.server.models.Greeting;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is a sample controller to test the server.
 */
@RestController
public class HelloWorldController {
  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  /**
   * Say hello response entity.
   *
   * @param name the name
   * @return the response entity
   */
  @GetMapping("/hello-world")
  @ResponseBody
  public ResponseEntity<Greeting> sayHello(
      @RequestParam(name = "name", required = false, defaultValue = "Stranger") String name
  ) {
    return new ResponseEntity<>(
        new Greeting(counter.incrementAndGet(), String.format(template, name)),
        HttpStatus.OK
    );
  }
}
