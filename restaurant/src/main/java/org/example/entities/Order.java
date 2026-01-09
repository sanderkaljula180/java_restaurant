package org.example.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private int id;
    private int table_id;
    private boolean paid;
    private LocalDateTime order_time;
    private BigDecimal order_price;
    private int waitress_id;
    private boolean ready;

    public Order() {
    }

    public Order(int id, int table_id, boolean paid, LocalDateTime order_time, BigDecimal order_price, int waitressId, boolean ready) {
        this.id = id;
        this.table_id = table_id;
        this.paid = paid;
        this.order_time = order_time;
        this.order_price = order_price;
        this.waitress_id = waitressId;
        this.ready = ready;
    }

    public Order(int table_id, boolean paid, LocalDateTime order_time, BigDecimal order_price, int waitressId, boolean ready) {
        this.table_id = table_id;
        this.paid = paid;
        this.order_time = order_time;
        this.order_price = order_price;
        this.waitress_id = waitressId;
        this.ready = ready;
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

    public BigDecimal getOrder_price() {
        return order_price;
    }

    public void setOrder_price(BigDecimal order_price) {
        this.order_price = order_price;
    }

    public int getWaitress_id() {
        return waitress_id;
    }

    public void setWaitress_id(int waitress_id) {
        this.waitress_id = waitress_id;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", table_id=" + table_id +
                ", paid=" + paid +
                ", order_time=" + order_time +
                ", order_price=" + order_price +
                ", waitress_id=" + waitress_id +
                ", ready=" + ready +
                '}';
    }
}
