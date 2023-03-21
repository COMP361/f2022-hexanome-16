package com.hexanome16.client.requests.lobbyservice.savegames;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;

/**
 * This class provied a request to delete a savegame.
 */
public class DeleteSavegameRequest {
  /**
   * Delete a savegame.
   *
   * @param gameServer The game server where the savegame is.
   * @param savegameId The savegame id.
   */
  public static void execute(String gameServer, String savegameId) {
    RequestClient.sendRequestString(new Request<>(RequestMethod.DELETE, RequestDest.SERVER,
        "/api/gameservices/" + gameServer + "/savegames/" + savegameId, Void.class));
  }
}
