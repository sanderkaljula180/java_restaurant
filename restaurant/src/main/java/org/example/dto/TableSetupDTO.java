package org.example.dto;


import java.util.HashMap;

public class TableSetupDTO {
    private final int tableId;
    private final int tableNumber;
    private final int tableCapacity;
    private final HashMap<Integer, String> availableWaitresses;

    public TableSetupDTO(int tableId, int tableNumber, int tableCapacity, HashMap<Integer, String> availableWaitresses) {
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.tableCapacity = tableCapacity;
        this.availableWaitresses = availableWaitresses;
    }

    public int getTableId() {
        return tableId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public int getTableCapacity() {
        return tableCapacity;
    }

    public HashMap<Integer, String> getAvailableWaitresses() {
        return availableWaitresses;
    }
}
