package com.hexanome16.server.controllers.lobbyservice;

import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.util.UrlUtils;
import java.util.Collections;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * TODO.
 */
@Service
public class GameServiceController {
  private final RestTemplate restTemplate;

  public GameServiceController(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  /**
   * TODO.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void createGameService() {
    TokensInfo tokensInfo =
        new AuthController(new RestTemplateBuilder()).login("xox", "laaPhie*aiN0");
    String url = UrlUtils.createLobbyServiceUri("/api/gameservices/{name}",
        "access_token=" + tokensInfo.getAccessToken());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    Payload payload = new Payload();
    HttpEntity<Payload> entity = new HttpEntity<>(payload, headers);
    this.restTemplate.put(url, entity, payload.name);
  }

  /**
   * This class represents the payload of the request (used for JSON conversion).
   */
  private static class Payload {
    String location;
    String name;
    Integer maxSessionPlayers;
    Integer minSessionPlayers;
    String displayName;
    String webSupport;

    /**
     * Default params used for testing/UI demo.
     */
    public Payload() {
      location = "http://127.0.0.1:4243/SplendorService";
      name = "Splendor";
      maxSessionPlayers = 4;
      minSessionPlayers = 2;
      displayName = "Splendor";
      webSupport = "true";
    }

    /**
     * Creates the payload with the given params.
     *
     * @param location          The location of the game service.
     * @param name              The name of the game service.
     * @param maxSessionPlayers The maximum number of players in a session.
     * @param minSessionPlayers The minimum number of players in a session.
     * @param displayName       The display name of the game service.
     * @param webSupport        Whether the game service supports web.
     */
    public Payload(String location, String name, Integer maxSessionPlayers,
                   Integer minSessionPlayers, String displayName, String webSupport) {
      this.location = location;
      this.name = name;
      this.maxSessionPlayers = maxSessionPlayers;
      this.minSessionPlayers = minSessionPlayers;
      this.displayName = displayName;
      this.webSupport = webSupport;
    }
  }
}
