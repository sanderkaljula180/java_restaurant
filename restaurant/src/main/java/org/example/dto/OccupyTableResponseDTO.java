package org.example.dto;

public class OccupyTableResponseDTO {

    private final int tableId;
    private final int numberOfQuests;
    private final int waitressId;
    private final String tableStatusUpdateMessage;
    private final String successMessage;

    public OccupyTableResponseDTO(int tableId, int numberOfQuests, int waitressId, String tableStatusUpdateMessage, String successMessage) {
        this.tableId = tableId;
        this.numberOfQuests = numberOfQuests;
        this.waitressId = waitressId;
        this.tableStatusUpdateMessage = tableStatusUpdateMessage;
        this.successMessage = successMessage;
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

    public String getTableStatusUpdateMessage() {
        return tableStatusUpdateMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }
}
