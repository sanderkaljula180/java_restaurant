package org.example.entities;

import java.math.BigDecimal;

public class Item {
    private int id;
    private String itemName;
    private BigDecimal itemPrice;
    private int itemsInStock;

    public Item() {
    }

    public Item(int id, String itemName, BigDecimal itemPrice, int itemsInStock) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemsInStock = itemsInStock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_name() {
        return itemName;
    }

    public void setItem_name(String item_name) {
        this.itemName = item_name;
    }

    public BigDecimal getItem_price() {
        return itemPrice;
    }

    public void setItem_price(BigDecimal item_price) {
        this.itemPrice = item_price;
    }

    public int getItemsInStock() {
        return itemsInStock;
    }

    public void setItemsInStock(int itemsInStock) {
        this.itemsInStock = itemsInStock;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemsInStock=" + itemsInStock +
                '}';
    }
}
