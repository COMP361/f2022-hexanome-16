package com.hexanome16.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hexanome16.server.models.price.PriceMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * For converting json file to noble objects.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DevelopmentCardJson {
  @JsonProperty("price")
  private PriceMap price;
}
