package org.example.entities;

public class OrderItem {
    private int id;
    private int order_id;
    private int item_id;
    private int quantity;
    private float price;

    public OrderItem() {
    }

    public OrderItem(int id, int order_id, int item_id, int quantity, float price) {
        this.id = id;
        this.order_id = order_id;
        this.item_id = item_id;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
