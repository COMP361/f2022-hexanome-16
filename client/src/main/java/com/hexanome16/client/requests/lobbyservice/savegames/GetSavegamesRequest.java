package com.hexanome16.client.requests.lobbyservice.savegames;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.sessions.SaveGameJson;
import java.util.Map;

/**
 * This class provides methods to get the savegames from all game services.
 */
public class GetSavegamesRequest {
  private GetSavegamesRequest() {
    super();
  }

  /**
   * Sends a request to get the savegames of a game server.
   *
   * @param gamename The name of the game server.
   * @return The savegames of the game server.
   */
  public static SaveGameJson[] execute(String gamename) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.LS,
        "/api/gameservices/" + gamename + "/savegames",
        Map.of("access_token", AuthUtils.getAuth().getAccessToken()), SaveGameJson[].class));
  }
}
