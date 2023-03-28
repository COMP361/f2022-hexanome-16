package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.savegame.SaveGame;
import com.hexanome16.server.services.winconditions.WinCondition;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SavegameTest {
  ServerPlayer imad = new ServerPlayer("imad", "white", 0);
  ServerPlayer tristan = new ServerPlayer("tristan", "blue", 1);
  private Game origGame;
  private SaveGame saveGame;

  /**
   * Initiates every test.
   *
   * @throws IOException from game creation
   */
  @BeforeEach
  public void init() throws IOException {
    origGame = Game.create(12345,
        new ServerPlayer[] {imad, tristan}, "imad", "", WinCondition.BASE);
    saveGame = new SaveGame(origGame, "test_savegame");
  }

  /**
   * Test savegame creation.
   */
  @Test
  public void testSavegameCreation() {
    assertEquals(origGame, Game.create(12345, saveGame));
  }
}
