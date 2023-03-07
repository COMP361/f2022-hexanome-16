package com.hexanome16.common.dto;

import com.hexanome16.common.models.Player;
import com.hexanome16.common.models.RouteType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Dto for trade post.
 */
@Data
@AllArgsConstructor
public class TradePostJson {
  RouteType routeType;
  Player[] players;
}
