package com.hexanome16.server.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DevelopmentCardTest {
  private final PriceMap priceMap = new PriceMap(1, 2, 3, 4, 5);

  private final Price price = new TokenPrice (priceMap);

  private final DevelopmentCard levelCard = new LevelCard(0, 1, "texture.png", price, Level.ONE);

  private final DevelopmentCard noble = new Noble(0, 3, "texture.png", price);

  @Test
  public void testLevelCardId(){
    assertEquals(0, levelCard.getId());
  }

  @Test
  public void testNobleId(){
    assertEquals(0, noble.getId());
  }

  @Test
  public void testLevelCardPrestigePoint(){
    assertEquals(1, levelCard.getPrestigePoint());
  }

  @Test
  public void testNoblePrestigePoint(){
    assertEquals(3, noble.getPrestigePoint());
  }

  @Test
  public void testLevelCardTexture(){
    assertEquals("texture.png", levelCard.getTexturePath());
  }

  @Test
  public void testNobleTexture(){
    assertEquals("texture.png", noble.getTexturePath());
  }

  @Test
  public void testLevelCardPrice(){
    assertEquals(price, levelCard.getPrice());
  }

  @Test
  public void testNoblePrice(){
    assertEquals(price, noble.getPrice());
  }

  @Test
  public void testLevelCardLevel(){
    assertEquals(Level.ONE, ((LevelCard)levelCard).getLevel());
  }
}
