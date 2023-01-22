package com.hexanome16.server.controllers.lobbyservice.gameservice;

import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.models.sessions.GameParams;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.UrlUtils;
import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * This controller is used for Game Service operations related to Lobby Service.
 */
@Service
public class GameServiceController {
  private final RestTemplate restTemplate;

  private final AuthServiceInterface authService;
  private final UrlUtils urlUtils;
  private final GameParams gameParams;
  @Value("${gs.username}")
  private String gsUsername;
  @Value("${gs.password}")
  private String gsPassword;

  /**
   * Constructor.
   *
   * @param restTemplateBuilder The RestTemplateBuilder.
   * @param authService         The AuthController.
   * @param urlUtils            The UrlUtils.
   * @param gameParams          The GameParams.
   */
  public GameServiceController(@Autowired RestTemplateBuilder restTemplateBuilder,
                               @Autowired AuthServiceInterface authService,
                               @Autowired UrlUtils urlUtils,
                               @Autowired GameParams gameParams) {
    this.restTemplate = restTemplateBuilder.build();
    this.urlUtils = urlUtils;
    this.authService = authService;
    this.gameParams = gameParams;
  }

  /**
   * This method registers this server as a game service in Lobby Service.
   * It is called at the startup of the server.
   *
   * @return the response entity
   */
  @EventListener(ApplicationReadyEvent.class)
  public ResponseEntity<Void> createGameService() {
    ResponseEntity<TokensInfo> tokensInfo = authService.login(gsUsername, gsPassword);
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
    return ResponseEntity.ok().build();
  }

  /**
   * This method deletes the associated game service in Lobby Service.
   *
   * @return the response entity
   */
  @PreDestroy
  public ResponseEntity<Void> deleteGameService() {
    ResponseEntity<TokensInfo> tokensInfo = authService.login(gsUsername, gsPassword);
    System.out.println(tokensInfo.getBody());
    URI url = urlUtils.createLobbyServiceUri("/api/gameservices/Splendor",
        "access_token=" + Objects.requireNonNull(tokensInfo.getBody()).getAccessToken());
    assert url != null;
    try {
      this.restTemplate.delete(url);
      return ResponseEntity.ok().build();
    } catch (HttpClientErrorException.BadRequest e) {
      System.out.println("Game service not deleted");
      return ResponseEntity.badRequest().build();
    }
  }
}
