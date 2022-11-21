package com.hexanome16.client.types.auth;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 * This class contains information about OAuth user tokens.
 */
public final class TokensInfo {
  @SerializedName("access_token")
  private String accessToken;
  @SerializedName("refresh_token")
  private String refreshToken;
  @SerializedName("token_type")
  private String tokenType;
  @SerializedName("expires_in")
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
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
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

  public String accessToken() {
    return accessToken;
  }

  public String refreshToken() {
    return refreshToken;
  }

  public String tokenType() {
    return tokenType;
  }

  public int expiresIn() {
    return expiresIn;
  }

  public String scope() {
    return scope;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
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
