package com.hexanome16.common.dto.cards;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * For converting json file to noble objects.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class NobleJson extends DevelopmentCardJson implements BroadcastContent {
  private int prestigePoint;
}
