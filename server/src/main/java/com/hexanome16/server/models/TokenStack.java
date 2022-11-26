package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A stack of tokens of the same color.
 */
public class TokenStack {
  private final Gem gem;
  private final List<Token> tokenList;

  public TokenStack(Gem gem) {
    this.gem = gem;
    this.tokenList = new ArrayList<Token>();
  }

  public Gem getGem() {
    return gem;
  }

  public List<Token> getTokenList() {
    return tokenList;
  }

  public void addToken(Token token) {
    tokenList.add(token);
  }
}

