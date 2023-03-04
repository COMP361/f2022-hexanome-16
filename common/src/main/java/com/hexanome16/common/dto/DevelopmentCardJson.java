package com.hexanome16.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hexanome16.common.models.price.PriceMap;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
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
public class DevelopmentCardJson implements BroadcastContent {
  @JsonProperty("price")
  private PriceMap price;

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return price == null;
  }
}
