package org.example.dto;

import java.math.BigDecimal;

public class ItemDTO {

    private final int id;
    private final String itemName;
    private final BigDecimal itemPrice;
    private final int itemsInStock;

    public ItemDTO(int id, String itemName, BigDecimal itemPrice, int itemsInStock) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemsInStock = itemsInStock;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public int getItemsInStock() {
        return itemsInStock;
    }
}
