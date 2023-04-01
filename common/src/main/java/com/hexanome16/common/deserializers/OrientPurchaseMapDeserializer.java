package com.hexanome16.common.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hexanome16.common.models.price.OrientPurchaseMap;
import com.hexanome16.common.models.price.PurchaseMap;
import java.io.IOException;

/**
 * Deserializes Orient Purchase Map.
 */
public class OrientPurchaseMapDeserializer extends StdDeserializer<OrientPurchaseMap> {

  /**
   * Instantiates a new Purchase map deserializer.
   */
  private final PurchaseMapDeserializer purchaseMapDeserializer =
      new PurchaseMapDeserializer();


  /**
   * Public constructor.
   */
  public OrientPurchaseMapDeserializer() {
    this(null);
  }

  protected OrientPurchaseMapDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public OrientPurchaseMap deserialize(JsonParser jp,
                                       DeserializationContext deserializationContext)
      throws IOException, JacksonException {
    JsonNode node = jp.getCodec().readTree(jp);
    PurchaseMap pm = purchaseMapDeserializer.parse(node);
    int goldenCardsAmount = node.get("goldenCardsAmount").asInt();
    return new OrientPurchaseMap(pm, goldenCardsAmount);
  }

}
