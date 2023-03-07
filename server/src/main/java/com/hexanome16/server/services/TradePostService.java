package com.hexanome16.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Trade post service for trade post controller.
 */
@Service
public class TradePostService {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final GameManagerServiceInterface gameManagerService;
  private final ServiceUtils serviceUtils;

  /**
   * Controller for the Trade Post.
   *
   * @param gameManagerService the game manager for fetching games
   * @param serviceUtils       the utility used by services
   */
  public TradePostService(@Autowired GameManagerServiceInterface gameManagerService,
                          @Autowired ServiceUtils serviceUtils) {
    this.gameManagerService = gameManagerService;
    this.serviceUtils = serviceUtils;
  }

  /**
   * Request for the trade post obtained by the player.
   *
   * @param sessionId sessionId of the game.
   * @param username  username of the player.
   * @return the trade posts the player has.
   * @throws JsonProcessingException json exception
   */
  public ResponseEntity<String> getPlayerTradePost(long sessionId, String username)
      throws JsonProcessingException {
    Game game = gameManagerService.getGame(sessionId);

    if (game == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }

    // get player with username
    ServerPlayer player = serviceUtils.findPlayerByName(game, username);

    if (player == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.PLAYER_NOT_IN_GAME);
    }

    return new ResponseEntity<>(
        objectMapper.writeValueAsString(player.getTradePosts()),
        HttpStatus.OK
    );
  }
}
