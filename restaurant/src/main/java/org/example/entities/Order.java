package org.example.entities;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int table_id;
    private boolean paid;
    private LocalDateTime order_time;
    private float order_price;

    public Order() {
    }

    public Order(int id, int table_id, boolean paid, LocalDateTime order_time, float order_price) {
        this.id = id;
        this.table_id = table_id;
        this.paid = paid;
        this.order_time = order_time;
        this.order_price = order_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public LocalDateTime getOrder_time() {
        return order_time;
    }

    public void setOrder_time(LocalDateTime order_time) {
        this.order_time = order_time;
    }

    public float getOrder_price() {
        return order_price;
    }

    public void setOrder_price(float order_price) {
        this.order_price = order_price;
    }
}
