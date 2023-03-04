package com.hexanome16.common.models.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class contains information about OAuth user tokens.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokensInfo {
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("expires_in")
  private int expiresIn;
  private String scope;
}
