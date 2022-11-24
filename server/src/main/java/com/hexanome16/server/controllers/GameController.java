package com.hexanome16.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Not implemented.
 */
@RestController
public class GameController {
  private final Map<Long, Game> gameMap = new HashMap<Long, Game>();

  /**
   * Create a new game as client requested.
   *
   * @param sessionId sessionId
   * @return error if present
   */
  @PostMapping(value = {"/game/{sessionId}", "/game/{sessionId}/"})
  public String createGame(@PathVariable long sessionId) {
    try {
      Game game = new Game(sessionId);
      gameMap.put(sessionId, game);
    } catch (Exception e) {
     // StringWriter errors = new StringWriter();
     // e.printStackTrace(new PrintWriter(errors));
    }
    File file = new File("/app");
    return Arrays.toString(file.list());
  }

  @GetMapping(value = {"/game/test", "/game/test/"})
  public String test() {
    return "test";
  }

  /**
   * Return next card in a deck to client.
   *
   * @param sessionId sessionId
   * @param level     deck level
   * @return next card on board
   * @throws JsonProcessingException json exception
   */
  @GetMapping(value = {"/game/nextCard/{sessionId}", "/game/nextCard/{sessionId}/"})
  public String nextCard(@PathVariable long sessionId, @RequestParam String level)
      throws JsonProcessingException {
    Level alevel = null;
    switch (level) {
      default:
      case "ONE":
        alevel = Level.ONE;
        break;
      case "TWO":
        alevel = Level.TWO;
        break;
      case "THREE":
        alevel = Level.THREE;
        break;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(gameMap.get(sessionId).getDeck(alevel).nextCard());
  }
}
