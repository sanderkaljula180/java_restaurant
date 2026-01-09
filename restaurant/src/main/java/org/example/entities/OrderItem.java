package org.example.entities;

import java.math.BigDecimal;

public class OrderItem {
    private int id;
    private int order_id;
    private int item_id;
    private int quantity;
    private BigDecimal price;
    private boolean ready;

    public OrderItem() {
    }

    public OrderItem(int id, int order_id, int item_id, int quantity, BigDecimal price, boolean ready) {
        this.id = id;
        this.order_id = order_id;
        this.item_id = item_id;
        this.quantity = quantity;
        this.price = price;
        this.ready = ready;
    }

    public OrderItem(int order_id, int item_id, int quantity, BigDecimal price, boolean ready) {
        this.order_id = order_id;
        this.item_id = item_id;
        this.quantity = quantity;
        this.price = price;
        this.ready = ready;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
