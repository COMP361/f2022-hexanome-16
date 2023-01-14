//package com.hexanome16.server.controllers;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
//import com.hexanome16.server.dto.DeckHash;
//import com.hexanome16.server.models.Level;
//import com.hexanome16.server.models.LevelCard;
//import com.hexanome16.server.models.PriceMap;
//import com.hexanome16.server.models.PurchaseMap;
//import com.hexanome16.server.models.TokenPrice;
//import com.hexanome16.server.models.auth.TokensInfo;
//import com.hexanome16.server.services.auth.AuthService;
//import com.hexanome16.server.util.UrlUtils;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.web.context.request.async.DeferredResult;
//import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
//
///**
// * Tests for {@link GameController}.
// */
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {GameController.class, AuthService.class, UrlUtils.class})
//@RestClientTest(excludeAutoConfiguration = MockRestServiceServerAutoConfiguration.class)
//public class GameControllerTest {
//  private final String kim = "{\"name\":" + "\"kim\"," + "\"preferredColour\":" + "\"#FFFFFF\"}";
//  private final String imad = "{\"name\":" + "\"imad\"," + "\"preferredColour\":" + "\"#FFFFFF\"}";
//  private final String creator = "kim";
//  private final String savegame = "";
//  private final Long sessionId = Long.valueOf(12345);
//  private final ObjectMapper objectMapper =
//      new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
//  private final Map<String, Object> payload = new HashMap<String, Object>();
//  @Autowired
//  private GameController gameController;
//  @Autowired
//  private AuthService authService;
//  private String accessToken;
//  private String invalidAccessToken;
//  private String gameResponse;
//
//  /**
//   * Setup mock for game tests.
//   *
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @BeforeEach
//  public void createGame() throws com.fasterxml.jackson.core.JsonProcessingException {
//    ResponseEntity<TokensInfo> tokens = authService.login("kim", "123");
//    authService.login("imad", "123");
//    accessToken = Objects.requireNonNull(tokens.getBody()).getAccessToken();
//    tokens = authService.login("peini", "123");
//    invalidAccessToken = Objects.requireNonNull(tokens.getBody()).getAccessToken();
//    List playerList = new ArrayList<String>();
//    playerList.add(objectMapper.readValue(imad, Map.class));
//    playerList.add(objectMapper.readValue(kim, Map.class));
//    payload.put("players", playerList);
//    payload.put("creator", creator);
//    payload.put("savegame", savegame);
//    gameResponse = gameController.createGame(12345, payload);
//  }
//
//  /**
//   * Test create game.
//   *
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @Test
//  public void testCreateGame() throws com.fasterxml.jackson.core.JsonProcessingException {
//    assertEquals("success", gameResponse);
//  }
//
//  /**
//   * Test update deck success.
//   *
//   * @throws JsonProcessingException                            the json processing exception
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @Test
//  public void testUpdateDeckSuccess()
//      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
//    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
//    ResponseEntity<String> response =
//        (ResponseEntity<String>) gameController.getDeck(12345, "ONE", accessToken, hash)
//            .getResult();
//    assertNotNull(response);
//  }
//
//  /**
//   * Test update deck fail.
//   *
//   * @throws JsonProcessingException                            the json processing exception
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @Test
//  public void testUpdateDeckFail()
//      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
//    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
//    DeferredResult<ResponseEntity<String>> response =
//        gameController.getDeck(12345, "ONE",
//            invalidAccessToken, hash);
//    assertNull(response);
//  }
//
//  /**
//   * Test update nobles success.
//   *
//   * @throws JsonProcessingException                            the json processing exception
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @Test
//  public void testUpdateNoblesSuccess()
//      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
//    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
//    ResponseEntity<String> response =
//        (ResponseEntity<String>) gameController.getNobles(12345, accessToken, hash).getResult();
//    assertNotNull(response);
//  }
//
//  /**
//   * Test update nobles fail.
//   *
//   * @throws JsonProcessingException                            the json processing exception
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @Test
//  public void testUpdateNoblesFail()
//      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
//    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
//    DeferredResult<ResponseEntity<String>> response =
//        gameController.getNobles(12345, invalidAccessToken,
//            hash);
//    assertNull(response);
//  }
//
//  /**
//   * Test current player success.
//   *
//   * @throws JsonProcessingException                            the json processing exception
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @Test
//  public void testCurrentPlayerSuccess()
//      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
//    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
//    ResponseEntity<String> response =
//        (ResponseEntity<String>) gameController.getCurrentPlayer(12345, accessToken, hash)
//            .getResult();
//    assertNotNull(response);
//  }
//
//  /**
//   * Test current player fail.
//   *
//   * @throws JsonProcessingException                            the json processing exception
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @Test
//  public void testCurrentPlayerFail()
//      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
//    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
//    DeferredResult<ResponseEntity<String>> response =
//        gameController.getCurrentPlayer(12345,
//            invalidAccessToken, hash);
//    assertNull(response);
//  }
//
//  /**
//   * Test get player bank info.
//   *
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @Test
//  public void testGetPlayerBankInfo() throws com.fasterxml.jackson.core.JsonProcessingException {
//    ResponseEntity<String> response =
//        gameController.getPlayerBankInfo(sessionId, "imad");
//    String string = response.getBody().toString();
//    PurchaseMap myPm = objectMapper.readValue(string,
//        new TypeReference<PurchaseMap>() {
//        });
//
//    assertEquals(myPm.getRubyAmount(), 3);
//    assertEquals(myPm.getEmeraldAmount(), 3);
//    assertEquals(myPm.getSapphireAmount(), 3);
//    assertEquals(myPm.getDiamondAmount(), 3);
//    assertEquals(myPm.getOnyxAmount(), 3);
//    assertEquals(myPm.getGoldAmount(), 3);
//  }
//
//  /**
//   * Test get game bank info.
//   *
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @Test
//  public void testGetGameBankInfo() throws com.fasterxml.jackson.core.JsonProcessingException {
//    ResponseEntity<String> response =
//        gameController.getGameBankInfo(sessionId);
//    String string = response.getBody().toString();
//    PurchaseMap myPm = objectMapper.readValue(string,
//        new TypeReference<PurchaseMap>() {
//        });
//
//    assertEquals(myPm.getRubyAmount(), 7);
//    assertEquals(myPm.getEmeraldAmount(), 7);
//    assertEquals(myPm.getSapphireAmount(), 7);
//    assertEquals(myPm.getDiamondAmount(), 7);
//    assertEquals(myPm.getOnyxAmount(), 7);
//    assertEquals(myPm.getGoldAmount(), 5);
//  }
//
//
//  /**
//   * Test buy card.
//   *
//   * @throws com.fasterxml.jackson.core.JsonProcessingException the json processing exception
//   */
//  @Test
//  public void testBuyCard() throws com.fasterxml.jackson.core.JsonProcessingException {
//
//    LevelCard myCard =
//        new LevelCard(20, 0, "",
//            new TokenPrice(
//                new PriceMap(1, 1, 1,
//                    1, 0)),
//            Level.ONE);
//
//    gameController.getGameMap().get(sessionId).endCurrentPlayersTurn();
//
//    DeckHash.allCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);
//
//    ResponseEntity<String> response =
//        gameController.buyCard(sessionId,
//            DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken,
//            1, 1, 1, 0, 0, 1
//        );
//
//
//    assertEquals(response.getStatusCode(), HttpStatus.OK);
//  }
//
//}
//
