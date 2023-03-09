package com.hexanome16.server.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * The type Choose city action.
 */
public class ChooseCityAction implements Action {
  @Getter
  private final City[] cities;
  private final String citiesJson;
  @Getter
  private final CustomHttpResponses.ActionType actionType = CustomHttpResponses.ActionType.CITY;

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
