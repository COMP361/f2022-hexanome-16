package com.hexanome16.common.dto.cards;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * For converting json file to level card objects.
 * Only used for creating cards at the start of the game.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DoubleJson extends BonusJson implements BroadcastContent {
}