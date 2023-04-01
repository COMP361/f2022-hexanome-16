package com.hexanome16.server.controllers.lobbyservice.gameservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.hexanome16.common.models.sessions.SaveGameJson;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.SavegameServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * SaveGameController tests.
 *
 * @author Elea
 */
@ExtendWith(MockitoExtension.class)
public class SaveGameControllerTest {
  /* mocked dependencies */
  @Mock
  private GameManagerServiceInterface gameManagerServiceMock;
  @Mock
  private SavegameServiceInterface saveGameServiceMock;
  /* field we are testing */
  @InjectMocks
  private SaveGameController savegameController;
  /* fields we are using */
  private final String saveGameId = String.valueOf(0);
  private final String gameName = "placeholder name";
  private final String[] players = {"player 1", "player 2", "player 3"};
  private final SaveGameJson saveGameJson = new SaveGameJson();
  private final ResponseEntity<String> responseStub = new ResponseEntity<>(HttpStatus.OK);

  /**
   * Setting up before each test is done.
   */
  @BeforeEach
  public void setup() {
    saveGameJson.setSaveGameId(saveGameId);
    saveGameJson.setGameName(gameName);
    saveGameJson.setPlayers(players);
  }

  /**
   * Test to see if a game in the form of a JSON can be successfully saved.
   * The test here is the createSaveGame method of the SaveGameController class.
   */
  @Test
  @DisplayName("Create a saved game successfully.")
  public void testCreateSavedGame() {
    // session ID is arbitrary
    long sessionId = 0;
    // mock the response
    when(gameManagerServiceMock.getGame(sessionId)).thenReturn(null);
    when(saveGameServiceMock.saveGame(null, saveGameId, saveGameJson)).thenReturn(responseStub);
    // create the save
    String accessToken = "placeholder access token";
    ResponseEntity<String> result =
        savegameController.createSavegame(gameName, saveGameId, accessToken,
            String.valueOf(sessionId), saveGameJson);
    // assert it was created successfully
    assertEquals(responseStub, result);
    verify(gameManagerServiceMock).getGame(sessionId);
    verify(saveGameServiceMock).saveGame(null, saveGameId, saveGameJson);
    verifyNoMoreInteractions(gameManagerServiceMock, saveGameServiceMock);
  }

  /**
   * Test to see if a save game can be successfully deleted.
   * The test here is the deleteSaveGame method of the SaveGameController class.
   */
  @Test
  @DisplayName("Delete a saved game successfully.")
  public void testDeleteSavedGame() {
    // mock the response
    when(saveGameServiceMock.deleteSavegame(gameName, saveGameId)).thenReturn(responseStub);
    // delete the save
    ResponseEntity<String> result = savegameController.deleteSavegame(gameName, saveGameId);
    // assert it was deleted successfully
    assertEquals(responseStub, result);
    verify(saveGameServiceMock).deleteSavegame(gameName, saveGameId);
    verifyNoMoreInteractions(saveGameServiceMock);
  }

  /**
   * Test to see if all saved games can be successfully deleted.
   * The test here is the deleteAllSaveGame method of the SaveGameController class.
   */
  @Test
  @DisplayName("Delete all saved games successfully.")
  public void testDeleteAllSavedGames() {
    // mock the response
    when(saveGameServiceMock.deleteAllSavegames(gameName)).thenReturn(responseStub);
    // delete all saves
    ResponseEntity<String> result = savegameController.deleteAllSavegames(gameName);
    // assert they were deleted successfully
    assertEquals(responseStub, result);
    verify(saveGameServiceMock).deleteAllSavegames(gameName);
    verifyNoMoreInteractions(saveGameServiceMock);
  }
}
