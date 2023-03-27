package com.hexanome16.server.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.dto.TradePostJson;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.TradePost;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Trade post service for trade post controller.
 */
@Service
public class TradePostService implements TradePostServiceInterface {
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

  @SneakyThrows
  @Override
  public ResponseEntity<String> getPlayerTradePosts(long sessionId, String username) {
    Game game = gameManagerService.getGame(sessionId);

    if (game == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }

    // get player with username
    ServerPlayer player = serviceUtils.findPlayerByName(game, username);

    if (player == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.PLAYER_NOT_IN_GAME);
    }

    Map<RouteType, TradePost> tradePosts = player.getInventory().getTradePosts();
    TradePostJson[] tradePostJsons = new TradePostJson[tradePosts.size()];

    int i = 0;
    for (Map.Entry<RouteType, TradePost> entry : tradePosts.entrySet()) {
      tradePostJsons[i] = new TradePostJson(entry.getKey());
      i++;
    }

    return new ResponseEntity<>(
        objectMapper.writeValueAsString(tradePostJsons),
        HttpStatus.OK
    );
  }
}
