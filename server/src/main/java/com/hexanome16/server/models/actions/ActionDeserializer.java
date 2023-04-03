package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hexanome16.common.models.City;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.util.ObjectMapperUtils;
import java.io.IOException;

/**
 * This class is responsible for deserializing an Action.
 */
public class ActionDeserializer extends JsonDeserializer<Action> {
  @Override
  public Action deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JacksonException {
    ObjectMapper mapper = ObjectMapperUtils.getObjectMapper();
    JsonNode root = mapper.readTree(jsonParser);
    if (root.has("actionType")) {
      return switch (root.get("actionType").asText("")) {
        case "LEVEL_ONE" -> mapper.readValue(root.toString(), TakeOneAction.class);
        case "LEVEL_TWO" -> mapper.readValue(root.toString(), TakeTwoAction.class);
        case "NOBLE" -> mapper.readValue(root.toString(), ChooseNobleAction.class);
        case "CITY" -> mapper.readValue(root.toString(), ChooseCityAction.class);
        case "DISCARD_TOKEN" -> mapper.readValue(root.toString(), DiscardTokenAction.class);
        case "TAKE_TOKEN" -> mapper.readValue(root.toString(), TakeTokenAction.class);
        case "ASSOCIATE_BAG" -> mapper.readValue(root.toString(), AssociateCardAction.class);
        case "NOBLE_RESERVE" -> mapper.readValue(root.toString(), ReserveNobleAction.class);
        default -> null;
      };
    } else {
      return null;
    }
  }

}
