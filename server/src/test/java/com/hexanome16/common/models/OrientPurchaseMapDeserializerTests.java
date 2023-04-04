package com.hexanome16.common.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.deserializers.OrientPurchaseMapDeserializer;
import com.hexanome16.common.models.price.OrientPurchaseMap;
import lombok.SneakyThrows;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests {@link com.hexanome16.common.deserializers.OrientPurchaseMapDeserializer}.
 */
public class OrientPurchaseMapDeserializerTests {
  /**
   * Tests OrientPurchaseMap deserializer.
   */
  @SneakyThrows
  @Test
  public void testDeserializer() {
    OrientPurchaseMap target = new OrientPurchaseMap(1, 1, 1, 1, 1,
        1, 1);
    ObjectMapper om = new ObjectMapper();
    JsonNode jsonNode =
        om.readTree(om.writeValueAsString(target));
    OrientPurchaseMapDeserializer underTest = new OrientPurchaseMapDeserializer();
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    ObjectCodec objectCodec = Mockito.mock(ObjectCodec.class);
    when(jsonParser.getCodec()).thenReturn(objectCodec);
    when(objectCodec.readTree(any(JsonParser.class))).thenReturn(jsonNode);
    assertEquals(target, underTest.deserialize(jsonParser, null));
  }
}
