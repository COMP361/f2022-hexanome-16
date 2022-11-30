package com.hexanome16.server.controllers.lobbyservice.auth;

import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.util.UrlUtils;
import java.net.URI;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * This service is responsible for sending authentication related requests to Lobby Service.
 */
@Service
public class AuthController {
  private final RestTemplate restTemplate;
  private final UrlUtils urlUtils;
  @Value("${ls.username}")
  private String lsUsername;
  @Value("${ls.password}")
  private String lsPassword;

  public AuthController(@Autowired RestTemplateBuilder restTemplateBuilder,
                        @Autowired UrlUtils urlUtils) {
    this.restTemplate = restTemplateBuilder.build();
    this.urlUtils = urlUtils;
  }

  @ResponseBody
  private ResponseEntity<TokensInfo> login(String username, String password, String refreshToken) {
    StringBuilder params = new StringBuilder();
    if (refreshToken == null || refreshToken.isBlank()) {
      params.append("grant_type=password&username=").append(username)
          .append("&password=").append(password);
    } else {
      params.append("grant_type=refresh_token&refresh_token=").append(refreshToken);
    }
    URI url = urlUtils.createLobbyServiceUri("/oauth/token", params.toString());
    assert url != null;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBasicAuth(lsUsername, lsPassword);
    HttpEntity<Void> entity = new HttpEntity<>(null, headers);
    try {
      return this.restTemplate.postForEntity(url, entity, TokensInfo.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @ResponseBody
  public ResponseEntity<TokensInfo> login(String username, String password) {
    return login(username, password, null);
  }

  @ResponseBody
  public ResponseEntity<TokensInfo> login(String refreshToken) {
    return login(null, null, refreshToken);
  }

  /**
   * Sends a request to Lobby Service to get the username associated with the passed access token.
   *
   * @param accessToken The access token of the user.
   * @return The username associated with the passed access token.
   */
  @ResponseBody
  public ResponseEntity<String> getPlayer(String accessToken) {
    URI url = urlUtils.createLobbyServiceUri("/oauth/username",
        "access_token=" + accessToken);
    assert url != null;
    try {
      return this.restTemplate.getForEntity(url, String.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * This request logs out the user in LS associated with the access token.
   *
   * @param accessToken The access token.
   */
  public ResponseEntity<Void> logout(String accessToken) {
    URI url = urlUtils.createLobbyServiceUri("/oauth/active", "refresh_token=" + accessToken);
    try {
      this.restTemplate.delete(url);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.ok().build();
  }
}
