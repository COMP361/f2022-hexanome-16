package com.hexanome16.server.controllers.lobbyservice;

import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.util.UrlUtils;
import java.net.URI;
import java.util.Collections;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * TODO.
 */
@RestController
public class AuthController {
  private final RestTemplate restTemplate;
  private final UrlUtils urlUtils;

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
    headers.setBasicAuth("bgp-client-name", "bgp-client-pw");
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
  public ResponseEntity<TokensInfo> refresh(String refreshToken) {
    return login(null, null, refreshToken);
  }
}
