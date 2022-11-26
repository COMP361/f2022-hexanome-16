package com.hexanome16.server.models;

import java.util.List;

/**
 * Player inventory class.
 */
public class Inventory {
    /*** fields *******************************************************************************************************/
    private PlayerBank playerBank;
    private List<Noble> ownedNobles;
    private List<Noble> reservedNobles;
    private List<LevelCard> ownedCards;
    private LevelCard[] reservedCards = new LevelCard[3];
    /*** setters ******************************************************************************************************/
    public void setPlayerBank(PlayerBank playerBank) {  this.playerBank = playerBank; }
    public void setOwnedNobles(List<Noble> ownedNobles) { this.ownedNobles = ownedNobles; }
    public void setReservedNobles(List<Noble> reservedNobles) { this.reservedNobles = reservedNobles; }
    public void setOwnedCards(List<LevelCard> ownedCards) { this.ownedCards = ownedCards; }
    public void setReservedCards(LevelCard[] reservedCards) { this.reservedCards = reservedCards; }

    /*** getters ******************************************************************************************************/
    public PlayerBank getPlayerBank() { return this.playerBank; }
    public List<Noble> getOwnedNobles() { return this.ownedNobles; }
    public List<Noble> getReservedNobles() { return this.reservedNobles; }
    public List<LevelCard> getOwnedCards() { return this.ownedCards; }
    public LevelCard[] getReservedCards() { return this.reservedCards; }
}
