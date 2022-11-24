package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
  private Level level;

  private int index;
  private List<DevelopmentCard> cardList = new ArrayList<>();
  public Deck(Level level) {
    this.level = level;
    this.index = 0;
  }

  public void addCard(DevelopmentCard card){
    cardList.add(card);
  }

  public void shuffle(){
    Collections.shuffle(cardList);
  }

  public DevelopmentCard nextCard(){
    return cardList.get(index++);
  }

}
