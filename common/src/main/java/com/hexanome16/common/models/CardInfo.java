package com.hexanome16.common.models;

import com.hexanome16.common.models.price.PriceMap;

/**
 * Record for all card information.
 */
public record CardInfo(int id, int prestigePoint, String texturePath, PriceMap price) {
}
