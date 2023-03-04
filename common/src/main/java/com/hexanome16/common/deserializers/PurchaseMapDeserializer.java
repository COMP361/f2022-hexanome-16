package com.hexanome16.common.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import java.io.IOException;

/**
 * Deserializer for PurchaseMap.
 */
public class PurchaseMapDeserializer extends StdDeserializer<PurchaseMap> {
  /**
   * Instantiates a new Purchase map deserializer.
   */
  private final PriceMapDeserializer priceMapDeserializer = new PriceMapDeserializer();

  /**
   * Default constructor.
   */
  public PurchaseMapDeserializer() {
    this(null);
  }

  /**
   * Constructor.
   *
   * @param vc class
   */
  public PurchaseMapDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public PurchaseMap deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    PriceMap priceMap = priceMapDeserializer.parse(node);
    if (node.get("price") != null) {
      node = node.get("price");
    }
    if (node.get("priceMap") != null) {
      node = node.get("priceMap");
    }
    int goldAmount = node.get("GOLD").asInt();
    return new PurchaseMap(priceMap, goldAmount);
  }
}
