package com.hexanome16.server.controllers;

import com.hexanome16.server.dto.InventoryDto;
import com.hexanome16.server.models.Inventory;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Noble;
import com.hexanome16.server.models.PlayerBank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class InventoryController {

    PlayerBank playerBank;
    List<Noble> ownedNobles;
    List<Noble> reservedNobles;
    List<LevelCard> ownedCards;
    LevelCard[] reservedCards = new LevelCard[3];


    @PostMapping(value = { "/games/{sessionId}/{playerId}/inventory/create"})
    public InventoryDto createInventory(@Valid @RequestBody InventoryDto request){
        // new Inventory
        Inventory inventory = new Inventory();
        return null;
    }




}
