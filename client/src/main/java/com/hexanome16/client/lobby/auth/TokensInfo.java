package com.hexanome16.client.lobby.auth;

import com.hexanome16.client.utils.StringConverter;
import java.util.Objects;

/**
 * This class contains information about OAuth user tokens.
 */
public final class TokensInfo {
  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private int expiresIn;
  private String scope;

  /**
   * Constructor.
   *
   * @param accessToken The access token.
   * @param refreshToken The refresh token.
   * @param tokenType The token type (should be "bearer").
   * @param expiresIn The number of seconds before the access token expires.
   * @param scope The scope of the access token.
   */
  public TokensInfo(String accessToken, String refreshToken, String tokenType, int expiresIn,
                    String scope) {
    this.accessToken = StringConverter.escape(accessToken);
    this.refreshToken = StringConverter.escape(refreshToken);
    this.tokenType = tokenType;
    this.expiresIn = expiresIn;
    this.scope = scope;
  }

  @Override
  public String toString() {
    return "TokensInfo{" + "access_token='" + accessToken
        + '\'' + ", refresh_token='" + refreshToken + '\''
        + ", token_type='" + tokenType + '\'' + ", expires_in="
        + expiresIn + ", scope='" + scope + '\'' + '}';
  }

  public String access_token() {
    return accessToken;
  }

  public String refresh_token() {
    return refreshToken;
  }

  public String token_type() {
    return tokenType;
  }

  public int expires_in() {
    return expiresIn;
  }

  public String scope() {
    return scope;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = StringConverter.escape(accessToken);
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = StringConverter.escape(refreshToken);
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public void setExpiresIn(int expiresIn) {
    this.expiresIn = expiresIn;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (TokensInfo) obj;
    return Objects.equals(this.accessToken, that.accessToken)
        && Objects.equals(this.refreshToken, that.refreshToken)
        && Objects.equals(this.tokenType, that.tokenType)
        && this.expiresIn == that.expiresIn
        && Objects.equals(this.scope, that.scope);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, refreshToken, tokenType, expiresIn, scope);
  }

}
