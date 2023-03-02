package com.hexanome16.server.services.auth;

import com.hexanome16.server.models.Game;
import com.hexanome16.server.util.UrlUtils;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import models.auth.TokensInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * Service is responsible for sending authentication related requests to Lobby Service.
 */
@Service
public class AuthService implements AuthServiceInterface {
  private final RestTemplate restTemplate;
  private final UrlUtils urlUtils;
  @Value("${ls.username}")
  private String lsUsername;
  @Value("${ls.password}")
  private String lsPassword;

  /**
   * Instantiates a new Auth controller.
   *
   * @param restTemplateBuilder the rest template builder
   * @param urlUtils            the url utils
   */
  public AuthService(@Autowired RestTemplateBuilder restTemplateBuilder,
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

  @Override
  @ResponseBody
  public ResponseEntity<TokensInfo> login(String username, String password) {
    return login(username, password, null);
  }

  @Override
  @ResponseBody
  public ResponseEntity<TokensInfo> login(String refreshToken) {
    return login(null, null, refreshToken);
  }

  @Override
  @ResponseBody
  public ResponseEntity<String> getPlayer(String accessToken) {
    URI url = urlUtils.createLobbyServiceUri("/oauth/username",
        "access_token=" + accessToken);
    assert url != null;
    System.out.println(url.toASCIIString());
    try {
      return this.restTemplate.getForEntity(url, String.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public ResponseEntity<Void> logout(String accessToken) {
    URI url = urlUtils.createLobbyServiceUri("/oauth/active", "access_token=" + accessToken);
    try {
      this.restTemplate.delete(url);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  @Override
  public boolean verifyPlayer(String accessToken, @NonNull Game game) {
    ResponseEntity<String> username = getPlayer(accessToken);
    if (username != null && username.getStatusCode().is2xxSuccessful()) {
      return Arrays.stream(game.getPlayers())
          .anyMatch(player -> player.getName().equals(username.getBody()));
    }
    return false;
  }
}
