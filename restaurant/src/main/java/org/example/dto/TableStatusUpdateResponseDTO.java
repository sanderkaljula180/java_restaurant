package org.example.dto;

public class TableStatusUpdateResponseDTO {

    private final int tableId;
    private final String previousStatus;
    private final String newStatus;
    private final String successMessage;

    public TableStatusUpdateResponseDTO(int tableId, String oldStatus, String newStatus, String successMessage) {
        this.tableId = tableId;
        this.previousStatus = oldStatus;
        this.newStatus = newStatus;
        this.successMessage = successMessage;
    }

    public int getTableId() {
        return tableId;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public String getSuccessMessage() {
        return successMessage;
    }
}
