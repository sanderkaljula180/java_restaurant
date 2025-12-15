package org.example.dto;

import java.util.HashMap;
import java.util.List;

public class AddOrderRequestDTO {
    private final int tableId;
    private final List<OrderItemsForOrderRequestDTO> items;

    public AddOrderRequestDTO(int tableId, List<OrderItemsForOrderRequestDTO> items) {
        this.tableId = tableId;
        this.items = items;
    }

    public int getTableId() {
        return tableId;
    }

    public List<OrderItemsForOrderRequestDTO> getItems() {
        return items;
    }

}
