package com.hexanome16.common.dto.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.models.City;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.util.ObjectMapperUtils;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Json class for cities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitiesJson implements BroadcastContent {
  private Map<String, City> cities = new HashMap<>();

  /**
   * Create cities MD5.
   *
   * @param citiesList the cities list
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  public CitiesJson(List<? extends City> citiesList) throws JsonProcessingException {
    ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
    for (City city : citiesList) {
      cities.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(city)), city);
    }
  }

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return cities.isEmpty();
  }
}
