package com.hexanome16.server.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Player bank class.
 */
public class PlayerBank {
  private final Map<Gem, TokenStack> tokenStackMap = new HashMap<Gem, TokenStack>();

  public PlayerBank() {
  }

  public Map<Gem, TokenStack> getTokenStackMap() {
    return tokenStackMap;
  }


  /**
   * increments Player bank by the amount specified by each parameter for each of their
   * corresponding gem types.
   *
   * @param rubyAmount amount to increase ruby stack by.
   * @param emeraldAmount amount to increase emerald stack by.
   * @param sapphireAmount amount to increase sapphire stack by.
   * @param diamondAmount amount to increase diamond stack by.
   * @param onyxAmount amount to increase onyx stack by.
   * @param goldAmount amount to increase gold stack by.
   */
  public void incPlayerBank(int rubyAmount, int emeraldAmount, int sapphireAmount,
                            int diamondAmount, int onyxAmount, int goldAmount) {

    for (Gem e : Gem.values()) {
      TokenStack tokenStackForGem = tokenStackMap.get(e);
      switch (e) {
        case RUBY -> {
          incSingularTokenStack(tokenStackForGem, rubyAmount);
        }
        case EMERALD -> {
          incSingularTokenStack(tokenStackForGem, emeraldAmount);
        }
        case SAPPHIRE -> {
          incSingularTokenStack(tokenStackForGem, sapphireAmount);
        }
        case DIAMOND -> {
          incSingularTokenStack(tokenStackForGem, diamondAmount);
        }
        case ONYX -> {
          incSingularTokenStack(tokenStackForGem, onyxAmount);
        }
        case GOLD -> {
          incSingularTokenStack(tokenStackForGem, goldAmount);
        }
        default -> {
          continue;
        }
      }
    }
  }

  // may have off by one error
  private void incSingularTokenStack(TokenStack tokenStack, int incrementAmount) {
    if (incrementAmount > 0) {
      for (; incrementAmount != 0; incrementAmount--) {
        Token newToken = new Token();
        newToken.gem = tokenStack.getGem();
        tokenStack.addToken(newToken);
      }
    } else if (incrementAmount < 0) {
      for (; incrementAmount != 0 && !tokenStack.isEmpty(); incrementAmount++) {
        tokenStack.removeToken();
      }
    }
  }

}


