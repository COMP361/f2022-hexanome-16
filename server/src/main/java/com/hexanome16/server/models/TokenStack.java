package com.hexanome16.server.models;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * A stack of tokens of the same color.
 */
@Getter
@AllArgsConstructor
public class TokenStack {
  private final Gem gem;
  private final List<Token> tokenList = new ArrayList<>();

  public void addToken(Token token) {
    tokenList.add(token);
  }

  public void removeToken() {
    tokenList.remove(0);
  }

  public boolean isEmpty() {
    return tokenList.isEmpty();
  }
}

