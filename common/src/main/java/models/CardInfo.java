package models;

import models.price.PriceMap;

/**
 * Record for all card information.
 */
public record CardInfo(int id, int prestigePoint, String texturePath, PriceMap price) {
}
