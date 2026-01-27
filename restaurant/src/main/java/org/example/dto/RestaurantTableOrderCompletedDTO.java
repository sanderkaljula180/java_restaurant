package org.example.dto;

public class RestaurantTableOrderCompletedDTO {
    private final int tableId;
    private final String status;
    private final boolean occupied;
    private final boolean allThisTableOrdersCompleted;

    public RestaurantTableOrderCompletedDTO(int tableId, String status, boolean occupied, boolean allThisTableOrdersCompleted) {
        this.tableId = tableId;
        this.status = status;
        this.occupied = occupied;
        this.allThisTableOrdersCompleted = allThisTableOrdersCompleted;
    }

    public int getTableId() {
        return tableId;
    }

    public String getStatus() {
        return status;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public boolean isAllThisTableOrdersCompleted() {
        return allThisTableOrdersCompleted;
    }
}
