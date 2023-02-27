package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import models.price.PriceMap;

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

  @Override
  public boolean isEmpty() {
    return price == null;
  }
}
