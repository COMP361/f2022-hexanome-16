package com.hexanome16.server.util.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import com.hexanome16.server.models.winconditions.WinCondition;
import java.io.IOException;

/**
 * Deserializer for {@link com.hexanome16.server.models.winconditions.WinCondition}.
 */
public class WinConditionDeserializer extends StdDeserializer<WinCondition> {

  /**
   * Default constructor.
   */
  public WinConditionDeserializer() {
    this(null);
  }

  /**
   * Constructor.
   *
   * @param vc The class.
   */
  public WinConditionDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public WinCondition deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, IllegalArgumentException {
    String gameServer = jp.getText();
    return switch (gameServer) {
      case "Splendor" -> new BaseWinCondition();
      default -> throw new IllegalArgumentException("Unexpected game server: " + gameServer);
    };
  }
}
