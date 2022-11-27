package com.hexanome16.server.controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.lobbyservice.AuthController;
import com.hexanome16.server.models.DevelopmentCard;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Not implemented.
 */
@RestController
public class GameController {
  //store all the games here
  private static final Map<Long, Game> gameMap = new HashMap<>();
  private final Map<String, DevelopmentCard> cardHashMap = new HashMap<>();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AuthController authController;

  public GameController(AuthController authController) {
    this.authController = authController;
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  }

  private Level getLevel(String level) {
    return switch (level) {
      case "ONE" -> Level.ONE;
      case "TWO" -> Level.TWO;
      case "THREE" -> Level.THREE;
      default -> throw new IllegalArgumentException("Invalid level");
    };
  }

  private boolean verifyPlayer(long sessionId, String accessToken) {
    Game game = gameMap.get(sessionId);
    if (game == null) {
      return false;
    }
    ResponseEntity<String> username = authController.getPlayer(accessToken);
    if (username.getStatusCode().is2xxSuccessful()) {
      return Arrays.stream(game.getPlayers())
          .anyMatch(player -> player.getName().equals(username.getBody()));
    }
    return false;
  }

  public static Map<Long, Game> getGameMap() {
    return gameMap;
  }

  /**
   * Create a new game as client requested.
   *
   * @param sessionId sessionId
   * @return error if present
   */
  @PutMapping(value = {"/games/{sessionId}", "/games/{sessionId}/"})
  public String createGame(@PathVariable long sessionId, @RequestBody Map<String, Object> payload) {
    try {
      Player[] players = objectMapper.convertValue(payload.get("players"), Player[].class);
      String creator = objectMapper.convertValue(payload.get("creator"), String.class);
      String savegame = objectMapper.convertValue(payload.get("savegame"), String.class);
      Game game = new Game(sessionId, players, creator, savegame);
      gameMap.put(sessionId, game);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "success";
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
  public String nextCard(@PathVariable long sessionId, @RequestParam String level,
                         @RequestParam String accessToken)
      throws JsonProcessingException {
    return verifyPlayer(sessionId, accessToken) ? objectMapper.writeValueAsString(
        gameMap.get(sessionId).getDeck(getLevel(level)).nextCard()) : null;
  }

  /**
   * Return initial deck to client at the start of the game.
   *
   * @param sessionId sessionId
   * @param level     deck level
   * @return next card on board
   * @throws JsonProcessingException json exception
   */
  @GetMapping(value = {"/game/getCards/{sessionId}", "/game/getCards/{sessionId}/"})
  public String getCards(@PathVariable long sessionId, @RequestParam String level,
                         @RequestParam String accessToken)
      throws JsonProcessingException {
    if (verifyPlayer(sessionId, accessToken)) {
      //All hash is MD5 checksum, checkstyle won't let me use the word MD5!!!
      Map<String, DevelopmentCard> cardHash = new HashMap<>();
      for (DevelopmentCard card : gameMap.get(sessionId).getOnBoardDeck(getLevel(level))
          .getCardList()) {
        //store in the class
        cardHashMap.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
        //store in the list we are going to send to the client
        cardHash.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
        System.out.println(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)));
      }
      System.out.println(objectMapper.writeValueAsString(cardHash));
      return objectMapper.writeValueAsString(cardHash);
    }
    return null;
  }

  /**
   * Returns nobles present on the game board.
   *
   * @param sessionId   session id
   * @param accessToken access token
   * @return nobles present on the game board
   * @throws JsonProcessingException if json processing fails
   */
  @GetMapping(value = {"/game/{sessionId}/getNobles", "/game/{sessionId}/getNobles/"})
  public String getNobles(@PathVariable long sessionId, @RequestParam String accessToken)
      throws JsonProcessingException {
    return verifyPlayer(sessionId, accessToken)
        ? objectMapper.writeValueAsString(gameMap.get(sessionId).getOnBoardNobles())
        : null;
  }
}


