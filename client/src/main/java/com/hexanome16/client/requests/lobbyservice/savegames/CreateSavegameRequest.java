package com.hexanome16.client.requests.lobbyservice.savegames;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.sessions.SaveGameJson;
import java.util.Map;
import java.util.UUID;

/**
 * This class provides methods to create a savegame in LS.
 */
public class CreateSavegameRequest {
  private CreateSavegameRequest() {
    super();
  }

  /**
   * Sends a request to create a savegame in LS.
   *
   * @param gamename The name of the game server.
   * @param usernames The usernames of the players.
   * @param sessionId The session id of the game to save.
   */
  public static void execute(String gamename, String[] usernames, long sessionId) {
    String savegameId = UUID.randomUUID().toString();
    RequestClient.sendRequest(new Request<>(RequestMethod.PUT, RequestDest.SERVER,
        "/api/gameservices/" + gamename + "/savegames/" + savegameId,
        Map.of("access_token", AuthUtils.getAuth().getAccessToken(), "sessionId", sessionId),
        new SaveGameJson(savegameId, gamename, usernames), Void.class));
  }
}
