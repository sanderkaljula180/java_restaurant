package org.example.dto;

public class OccupyTableRequestDTO {

    private final int tableId;
    private final int numberOfQuests;
    private final int waitressId;

    public OccupyTableRequestDTO(int tableId, int numberOfQuests, int waitressId) {
        this.tableId = tableId;
        this.numberOfQuests = numberOfQuests;
        this.waitressId = waitressId;
    }

    public int getTableId() {
        return tableId;
    }

    public int getNumberOfQuests() {
        return numberOfQuests;
    }

    public int getWaitressId() {
        return waitressId;
    }
}
