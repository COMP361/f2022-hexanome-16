package com.hexanome16.types.lobby.auth;

import com.hexanome16.utils.StringConverter;
import java.util.Objects;

public final class TokensInfo {
  private String access_token;
  private String refresh_token;
  private String token_type;
  private int expires_in;
  private String scope;

  public TokensInfo(String access_token, String refresh_token, String token_type, int expires_in,
                    String scope) {
    this.access_token = StringConverter.escape(access_token);
    this.refresh_token = StringConverter.escape(refresh_token);
    this.token_type = token_type;
    this.expires_in = expires_in;
    this.scope = scope;
  }

  @Override
  public String toString() {
    return "TokensInfo{" +
        "access_token='" + access_token + '\'' +
        ", refresh_token='" + refresh_token + '\'' +
        ", token_type='" + token_type + '\'' +
        ", expires_in=" + expires_in +
        ", scope='" + scope + '\'' +
        '}';
  }

  public String access_token() {
    return access_token;
  }

  public String refresh_token() {
    return refresh_token;
  }

  public String token_type() {
    return token_type;
  }

  public int expires_in() {
    return expires_in;
  }

  public String scope() {
    return scope;
  }

  public void setAccess_token(String access_token) {
    this.access_token = StringConverter.escape(access_token);
  }

  public void setRefresh_token(String refresh_token) {
    this.refresh_token = StringConverter.escape(refresh_token);
  }

  public void setToken_type(String token_type) {
    this.token_type = token_type;
  }

  public void setExpires_in(int expires_in) {
    this.expires_in = expires_in;
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
    return Objects.equals(this.access_token, that.access_token) &&
        Objects.equals(this.refresh_token, that.refresh_token) &&
        Objects.equals(this.token_type, that.token_type) &&
        this.expires_in == that.expires_in &&
        Objects.equals(this.scope, that.scope);
  }

  @Override
  public int hashCode() {
    return Objects.hash(access_token, refresh_token, token_type, expires_in, scope);
  }

}
