package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.models.City;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.cards.ServerCity;
import com.hexanome16.server.util.CustomResponseFactory;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

/**
 * The type Choose city action.
 */
@Data
@NoArgsConstructor
@JsonDeserialize(as = ChooseCityAction.class)
public class ChooseCityAction implements Action {
  @Getter
  private City[] cities;
  private String citiesJson;
  @Getter
  private CustomHttpResponses.ActionType actionType = CustomHttpResponses.ActionType.CITY;

  /**
   * Instantiates a new Choose city action.
   *
   * @param cities the cities
   * @throws JsonProcessingException thrown if cities cannot be parsed
   */
  public ChooseCityAction(City[] cities) throws JsonProcessingException {
    this.cities = cities;
    ObjectMapper ob = new ObjectMapper();
    this.citiesJson = ob.writeValueAsString(cities);
  }

  @Override
  public ResponseEntity<String> getActionDetails() {
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.CHOOSE_CITY,
        citiesJson, null);
  }
}
