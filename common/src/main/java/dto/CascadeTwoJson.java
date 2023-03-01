package dto;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * For converting json file to level card objects.
 * Only used for creating cards at the start of the game.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CascadeTwoJson extends BonusJson implements BroadcastContent {
}
