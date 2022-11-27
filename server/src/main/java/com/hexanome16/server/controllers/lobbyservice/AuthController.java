package com.hexanome16.server.controllers.lobbyservice;

import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.util.UrlUtils;
import java.net.URI;
import java.util.Collections;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * This controller is used for authentication operations related to Lobby Service.
 */
@RestController
@Service
public class AuthController {
  private final RestTemplate restTemplate;
  private final UrlUtils urlUtils;
  @Value("${ls.username}")
  private String lsUsername;
  @Value("${ls.password}")
  private String lsPassword;

  public AuthController(RestTemplateBuilder restTemplateBuilder, UrlUtils urlUtils) {
    this.restTemplate = restTemplateBuilder.build();
    this.urlUtils = urlUtils;
  }

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
    assert username != null && !username.isBlank() && password != null && !password.isBlank();
    return login(username, password, null);
  }

  @ResponseBody
  public ResponseEntity<TokensInfo> login(String refreshToken) {
    return login(null, null, refreshToken);
  }

  /**
   * This request returns the username of the LS account associated with the access token.
   *
   * @param accessToken The access token.
   * @return The username of the LS account associated with the access token.
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
  @PreDestroy
  public void logout(String accessToken) {
    URI url = urlUtils.createLobbyServiceUri("/oauth/active", "refresh_token=" + accessToken);
    try {
      this.restTemplate.delete(url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
