package com.hexanome16.server.controllers.lobbyservice;

import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.models.sessions.GameParams;
import com.hexanome16.server.util.UrlUtils;
import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * TODO.
 */
@RestController
@Service
public class GameServiceController {
  private final RestTemplate restTemplate;

  private final AuthController authController;
  private final UrlUtils urlUtils;
  private final GameParams gameParams;
  @Value("${gs.username}")
  private String gsUsername;
  @Value("${gs.password}")
  private String gsPassword;

  /**
   * TODO.
   */
  public GameServiceController(RestTemplateBuilder restTemplateBuilder,
                               AuthController authController, UrlUtils urlUtils,
                               GameParams gameParams) {
    this.restTemplate = restTemplateBuilder.build();
    this.urlUtils = urlUtils;
    this.authController = authController;
    this.gameParams = gameParams;
  }

  /**
   * TODO.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void createGameService() {
    ResponseEntity<TokensInfo> tokensInfo = authController.login(gsUsername, gsPassword);
    System.out.println(tokensInfo.getBody());
    URI url = urlUtils.createLobbyServiceUri("/api/gameservices/Splendor",
        "access_token=" + Objects.requireNonNull(tokensInfo.getBody()).getAccessToken());
    assert url != null;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    HttpEntity<GameParams> entity = new HttpEntity<>(gameParams, headers);
    try {
      this.restTemplate.put(url, entity);
    } catch (HttpClientErrorException.BadRequest e) {
      System.out.println("Game service already registered");
    }
  }
}
