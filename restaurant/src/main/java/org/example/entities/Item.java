package org.example.entities;

public class Item {
    private int id;
    private String item_name;
    private float item_price;

    public Item() {
    }

    public Item(int id, String item_name, float item_price) {
        this.id = id;
        this.item_name = item_name;
        this.item_price = item_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public float getItem_price() {
        return item_price;
    }

    public void setItem_price(float item_price) {
        this.item_price = item_price;
    }
}
