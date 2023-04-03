package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.models.City;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.common.util.ObjectMapperUtils;
import com.hexanome16.server.util.CustomResponseFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

/**
 * The type Choose city action.
 */
@Data
@NoArgsConstructor
@JsonDeserialize(as = ChooseCityAction.class)
public class ChooseCityAction implements Action {
  private City[] cities;
  private CustomHttpResponses.ActionType actionType = CustomHttpResponses.ActionType.CITY;

  /**
   * Instantiates a new Choose city action.
   *
   * @param cities the cities
   */
  public ChooseCityAction(City[] cities) {
    this.cities = cities;
  }

  @Override
  public ResponseEntity<String> getActionDetails() throws JsonProcessingException {
    String citiesJson = ObjectMapperUtils.getObjectMapper().writeValueAsString(cities);
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.CHOOSE_CITY,
        citiesJson, null);
  }
}
