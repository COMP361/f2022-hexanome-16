package com.hexanome16.server.controllers.lobbyservice;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.models.sessions.GameParams;
import com.hexanome16.server.util.UrlUtils;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * TODO.
 */
@RestController
public class GameServiceController {
  private final RestTemplate restTemplate;

  private final AuthController authController;

  /**
   * TODO.
   */
  public GameServiceController(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
    this.authController = new AuthController(restTemplateBuilder);
  }

  /**
   * TODO.
   */
  public void createGameService() {
    ResponseEntity<TokensInfo> tokensInfo =
        authController.login("xox", "laaPhie*aiN0");
    URI url = new UrlUtils().createLobbyServiceUri("/api/gameservices/Splendor",
        "access_token=" + Objects.requireNonNull(tokensInfo.getBody()).getAccessToken());
    assert url != null;
    GameParams gameParams = new GameParams();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    HttpEntity<GameParams> entity = new HttpEntity<>(gameParams, headers);
    this.restTemplate.put(url, entity);
  }

  /**
   * TODO.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void createGameServiceAfterStartup() {
    createGameService();
  }
}
