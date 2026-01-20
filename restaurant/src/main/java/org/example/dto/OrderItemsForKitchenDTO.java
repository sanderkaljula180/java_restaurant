package org.example.dto;

public class OrderItemsForKitchenDTO {

    private final String name;
    private final int quantity;
    private final int orderItemId;
    private final boolean ready;

    public OrderItemsForKitchenDTO(String name, int quantity, int orderItemId, boolean ready) {
        this.name = name;
        this.quantity = quantity;
        this.orderItemId = orderItemId;
        this.ready = ready;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public boolean isReady() {
        return ready;
    }
}
