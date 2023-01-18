package com.hexanome16.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Gem;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service is responsible for managing taking tokens requests from
 * the {@link com.hexanome16.server.controllers.TokensController}.
 */
@Service
public class TokenService implements TokenServiceInterface {

  private final GameServiceInterface gameService;
  private final AuthServiceInterface authService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Constructor for the Token service Class.
   *
   * @param gameService Needed for getting games and such.
   * @param authService Needed for player verification.
   */
  public TokenService(@Autowired GameServiceInterface gameService,
                      @Autowired AuthServiceInterface authService) {
    this.gameService = gameService;
    this.authService = authService;
  }

  @Override
  public ResponseEntity<String> availableTwoTokensType(long sessionId)
      throws JsonProcessingException {
    Game currentGame = gameService.getGameMap().get(sessionId);
    ArrayList<Gem> listAvailableForTwo = currentGame.availableTwoTokensType();
    ArrayList<String> listAvailableForTwoBonusType = new ArrayList<>(listAvailableForTwo.stream()
        .map(Gem::getBonusType).toList());
    return new ResponseEntity<>(
        objectMapper.writeValueAsString(listAvailableForTwoBonusType), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> availableThreeTokensType(long sessionId)
      throws JsonProcessingException {
    Game currentGame = gameService.getGameMap().get(sessionId);
    ArrayList<Gem> listAvailableForThree = currentGame.availableThreeTokensType();
    ArrayList<String> listAvailableForThreeBonusType =
        new ArrayList<>(listAvailableForThree.stream()
        .map(Gem::getBonusType).toList());
    return new ResponseEntity<>(
        objectMapper.writeValueAsString(listAvailableForThreeBonusType), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> takeTwoTokens(long sessionId, String authenticationToken,
                                              String tokenType) {

    return null;
  }

  @Override
  public ResponseEntity<String> takeThreeTokens(long sessionId, String authenticationToken,
                                                String tokenTypeOne, String tokenTypeTwo,
                                                String tokenTypeThree) {

    return null;
  }
}
