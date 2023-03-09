package com.hexanome16.common.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hexanome16.common.models.price.PriceMap;

/**
 * Deserializer for PriceMap.
 */
public class PriceMapDeserializer extends StdDeserializer<PriceMap> {

  /**
   * Default constructor.
   */
  public PriceMapDeserializer() {
    this(null);
  }

  /**
   * Constructor.
   *
   * @param vc class
   */
  public PriceMapDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public PriceMap deserialize(JsonParser jp, DeserializationContext ctxt)
      throws java.io.IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    return this.parse(node);
  }

  /**
   * Parses the json node to a PriceMap.
   *
   * @param node json node
   * @return PriceMap
   */
  public PriceMap parse(JsonNode node) {
    if (node.get("price") != null) {
      node = node.get("price");
    }
    if (node.get("priceMap") != null) {
      node = node.get("priceMap");
    }
    return new PriceMap(
        node.get("RUBY").asInt(),
        node.get("EMERALD").asInt(),
        node.get("SAPPHIRE").asInt(),
        node.get("DIAMOND").asInt(),
        node.get("ONYX").asInt()
    );
  }
}
