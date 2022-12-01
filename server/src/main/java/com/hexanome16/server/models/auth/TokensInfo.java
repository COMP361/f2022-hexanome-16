package com.hexanome16.server.models.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class contains information about OAuth user tokens.
 */
@ToString
@EqualsAndHashCode
@Setter
@Getter
public final class TokensInfo {
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("expires_in")
  private int expiresIn;
  private String scope;

  /**
   * Constructor.
   *
   * @param accessToken  The access token.
   * @param refreshToken The refresh token.
   * @param tokenType    The token type (should be "bearer").
   * @param expiresIn    The number of seconds before the access token expires.
   * @param scope        The scope of the access token.
   */
  public TokensInfo(String accessToken, String refreshToken, String tokenType, int expiresIn,
                    String scope) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.tokenType = tokenType;
    this.expiresIn = expiresIn;
    this.scope = scope;
  }

}
