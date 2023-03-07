package com.hexanome16.common.dto.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.models.Noble;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * A noble class made for long polling.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NobleDeckJson implements BroadcastContent {

  private Map<String, Noble> nobles = new HashMap<>();

  /**
   * Create noble MD5.
   *
   * @param nobleList the noble list
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  public NobleDeckJson(List<? extends Noble> nobleList) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    for (Noble noble : nobleList) {
      nobles.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(noble)), noble);
    }
  }

  @JsonIgnore
  @Override
  public boolean isEmpty() {
    return nobles.isEmpty();
  }
}
