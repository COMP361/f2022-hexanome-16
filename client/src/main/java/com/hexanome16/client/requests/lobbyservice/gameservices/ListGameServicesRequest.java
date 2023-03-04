package com.hexanome16.client.requests.lobbyservice.gameservices;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.common.dto.GameServiceJson;
import java.util.Map;
import javafx.util.Pair;

/**
 * This class provides a request to get the list of game services from Lobby Service.
 */
public class ListGameServicesRequest {
  /**
   * Executes the request.
   *
   * @return the array of game services
   */
  public static GameServiceJson[] execute() {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.LS,
        "/api/gameservices", GameServiceJson[].class));
  }
}
