package com.hexanome16.server.models;

import com.hexanome16.server.models.price.PriceInterface;

/**
 * Record for all card information.
 */
public record CardInfo(int id, int prestigePoint, String texturePath, PriceInterface price) {
}
