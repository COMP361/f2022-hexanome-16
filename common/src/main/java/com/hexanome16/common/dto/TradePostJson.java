package com.hexanome16.common.dto;

import com.hexanome16.common.models.RouteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Json for trade post.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradePostJson {
  RouteType routeType;
}
