package com.hexanome16.server.controllers.lobbyservice.gameservice;

import com.hexanome16.common.models.auth.TokensInfo;
import com.hexanome16.server.models.sessions.ServerGameParams;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.services.winconditions.WinCondition;
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
import org.springframework.core.annotation.Order;
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
   */
  public GameServiceController(@Autowired RestTemplateBuilder restTemplateBuilder,
                               @Autowired AuthServiceInterface authService,
                               @Autowired UrlUtils urlUtils) {
    this.restTemplate = restTemplateBuilder.build();
    this.urlUtils = urlUtils;
    this.authService = authService;
  }

  /**
   * This method creates the game services in Lobby Service when the application is ready.
   */
  @EventListener(ApplicationReadyEvent.class)
  @Order(10000)
  public void createGameServices() {
    createGameService(new ServerGameParams(4, 2,
        WinCondition.BASE.getGameServiceJson().getName(),
        WinCondition.BASE.getGameServiceJson().getDisplayName(), "true"));
    createGameService(new ServerGameParams(4, 2,
        WinCondition.TRADEROUTES.getGameServiceJson().getName(),
        WinCondition.TRADEROUTES.getGameServiceJson().getDisplayName(), "true"));
    createGameService(new ServerGameParams(4, 2,
        WinCondition.CITIES.getGameServiceJson().getName(),
        WinCondition.CITIES.getGameServiceJson().getDisplayName(), "true"));
  }

  /**
   * This method registers this server as a game service in Lobby Service.
   *
   * @param serverGameParams the server game params
   * @return the response entity
   */
  public ResponseEntity<Void> createGameService(ServerGameParams serverGameParams) {
    ResponseEntity<TokensInfo> tokensInfo = authService.login(gsUsername, gsPassword);
    URI url = urlUtils.createLobbyServiceUri(
        "/api/gameservices/" + serverGameParams.getName(),
        "access_token=" + Objects.requireNonNull(tokensInfo.getBody()).getAccessToken());
    assert url != null;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    HttpEntity<ServerGameParams> entity = new HttpEntity<>(serverGameParams, headers);
    try {
      this.restTemplate.put(url, entity);
    } catch (HttpClientErrorException.BadRequest e) {
      System.out.println("Game service already registered");
    }
    return ResponseEntity.ok().build();
  }

  /**
   * This method deletes the game services in Lobby Service when the application is destroyed.
   */
  @PreDestroy
  public void deleteGameServices() {
    System.out.println("Deleting game services");
    deleteGameService(WinCondition.BASE.getGameServiceJson().getName());
    System.out.println("Deleted base service");
    deleteGameService(WinCondition.TRADEROUTES.getGameServiceJson().getName());
    System.out.println("Deleted trade service");
    deleteGameService(WinCondition.CITIES.getGameServiceJson().getName());
    System.out.println("Deleted cities service");
  }

  /**
   * This method deletes the associated game service in Lobby Service.
   *
   * @param serviceName the service name
   * @return the response entity
   */
  public ResponseEntity<Void> deleteGameService(String serviceName) {
    ResponseEntity<TokensInfo> tokensInfo = authService.login(gsUsername, gsPassword);
    System.out.println(tokensInfo.getBody());
    URI url = urlUtils.createLobbyServiceUri("/api/gameservices/" + serviceName,
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
