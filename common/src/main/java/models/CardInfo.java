package models;

import models.price.PriceInterface;

/**
 * Record for all card information.
 */
public record CardInfo(int id, int prestigePoint, String texturePath, PriceInterface price) {
}
