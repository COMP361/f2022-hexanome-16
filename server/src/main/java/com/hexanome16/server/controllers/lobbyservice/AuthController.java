package com.hexanome16.server.controllers.lobbyservice;

import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.util.UrlUtils;
import java.util.Collections;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * TODO.
 */
@RestController
public class AuthController {
  private final RestTemplate restTemplate;

  public AuthController(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  private ResponseEntity<TokensInfo> login(String username, String password, String refreshToken) {
    StringBuilder params = new StringBuilder();
    if (refreshToken == null || refreshToken.isBlank()) {
      params.append("grant_type=password&username=").append(username)
          .append("&password=").append(password);
    } else {
      params.append("grant_type=refresh_token&refresh_token=")
          .append(UrlUtils.encodeUriComponent(refreshToken));
    }
    String url = UrlUtils.createLobbyServiceUri("/oauth/token", params.toString());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBasicAuth("bgp-client-name", "bgp-client-pw");
    ResponseEntity<TokensInfo> response =
        restTemplate.postForEntity(url, headers, TokensInfo.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      return response;
    } else {
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
