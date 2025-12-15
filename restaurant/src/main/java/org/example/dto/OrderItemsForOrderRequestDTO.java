package org.example.dto;

public class OrderItemsForOrderRequestDTO {
    private final int itemId;
    private final int quantity;

    public OrderItemsForOrderRequestDTO(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }
}
