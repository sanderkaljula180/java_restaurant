package org.example.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private final int order_id;
    private final int item_id;
    private final int quantity;
    private final BigDecimal price;
    private final boolean ready;

    public OrderItemDTO(int order_id, int item_id, int quantity, BigDecimal price, boolean ready) {
        this.order_id = order_id;
        this.item_id = item_id;
        this.quantity = quantity;
        this.price = price;
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getOrder_id() {
        return order_id;
    }
}
