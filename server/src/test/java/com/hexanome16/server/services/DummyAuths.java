package com.hexanome16.server.services;

import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Static accounts to use for tests that use DummyAuthService.
 *
 * <p>List of accounts:
 * 1.
 * <pre>
 *   username: tristan
 *   preferredColor: #FFFFFF
 *   password: 123
 *   accessToken: abc
 *   refreshToken: abc
 * </pre>
 * 2.
 * <pre>
 *   username: elea
 *   preferredColor: #FFFFFF
 *   password: 1234
 *   accessToken: abcd
 *   refreshToken: abcd
 * </pre>
 */
public class DummyAuths {
  /**
   * Immutable static list of tokensInfo.
   *
   * <pre>
   * Usage:
   *   {@code DummyAuths.tokensInfos.get(0)} to get the "tristan" tokens
   *   {@code DummyAuths.tokensInfos.get(1)} to get the "elea" tokens
   * </pre>
   */
  public static final List<TokensInfo> validTokensInfos =
      List.of(new TokensInfo("abc", "abc", "bearer", 1800, "read write trust"),
          new TokensInfo("abcd", "abcd", "bearer", 1800, "read write trust"));

  /**
   * Immutable static list of invalid tokensInfo.
   *
   * <pre>
   * Usage:
   *   {@code DummyAuths.tokensInfos.get(0)} to get an invalid token
   * </pre>
   */
  public static final List<TokensInfo> invalidTokensInfos =
      List.of(new TokensInfo("a", "a", "bearer", 1800, "read write trust"),
          new TokensInfo("ab", "ab", "bearer", 1800, "read write trust"));

  /**
   * Immutable static list of players.
   *
   * <pre>
   * Usage:
   *   {@code DummyAuths.tokensInfos.get(0)} to get the "tristan" player
   *   {@code DummyAuths.tokensInfos.get(1)} to get the "elea" player
   * </pre>
   */
  public static final List<Player> validPlayerList =
      List.of(new Player("tristan", "#FFFFFF"), new Player("elea", "#FFFFFF"));

  /**
   * Immutable static list of invalid players.
   *
   * <pre>
   * Usage:
   *   {@code DummyAuths.tokensInfos.get(0)} to get the "imad" player
   *   {@code DummyAuths.tokensInfos.get(1)} to get the "el" player
   * </pre>
   */
  public static final List<Player> invalidPlayerList =
      List.of(new Player("imad", "#FFFFFF"), new Player("el", "#FFFFFF"));

  /**
   * Immutable static list of players in json format.
   *
   * <pre>
   * Usage:
   *   {@code DummyAuths.jsonList.get(0)} to get the "tristan" player
   *   {@code DummyAuths.jsonList.get(1)} to get the "elea" player
   * </pre>
   */
  public static final List<String> validJsonList =
      List.of("{\"name\":" + "\"tristan\"," + "\"preferredColour\":" + "\"#FFFFFF\"}",
          "{\"name\":" + "\"elea\"," + "\"preferredColour\":" + "\"#FFFFFF\"}");

  /**
   * Immutable static list of invalid players in json format.
   *
   * <pre>
   * Usage:
   *   {@code DummyAuths.jsonList.get(0)} to get the "tris" player
   *   {@code DummyAuths.jsonList.get(1)} to get the "el" player
   * </pre>
   */
  public static final List<String> invalidJsonList =
      List.of("{\"name\":" + "\"tris\"," + "\"preferredColour\":" + "\"#FFFFFF\"}",
          "{\"name\":" + "\"el\"," + "\"preferredColour\":" + "\"#FFFFFF\"}");

  /**
   * Immutable static list of valid session IDs.
   */
  public static final List<Long> validSessionIds = List.of(12345L, 123456L);

  /**
   * Immutable static list of invalid session IDs.
   */
  public static final List<Long> invalidSessionIds = List.of(0L, 1L);

  public static final Map<Long, Game> validGames;

  static {
    try {
      validGames = Map.of(
          validSessionIds.get(0), new Game(validSessionIds.get(0),
              validPlayerList.toArray(new Player[2]), validPlayerList.get(0).getName(),
              "", new BaseWinCondition())
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Immutable static list of valid passwords.
   */
  public static final List<String> validPasswords = List.of("123");

  /**
   * Immutable static list of invalid passwords.
   */
  public static final List<String> invalidPasswords = List.of("wrong");
}
