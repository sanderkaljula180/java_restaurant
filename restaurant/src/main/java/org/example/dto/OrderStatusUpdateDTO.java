package org.example.dto;

public class OrderStatusUpdateDTO {
    private final int orderId;
    private final boolean orderItemsStatuses;

    public OrderStatusUpdateDTO(int orderId, boolean orderItemsStatuses) {
        this.orderId = orderId;
        this.orderItemsStatuses = orderItemsStatuses;
    }

    public int getOrderId() {
        return orderId;
    }

    public boolean isOrderItemsStatuses() {
        return orderItemsStatuses;
    }
}
