package org.example.dto;

import java.util.HashMap;


/**
 * DTO for sending back all restaurant tables data
 */
public class TableDTO {

    private final int table_id;
    private final boolean occupied;
    private final int table_number;
    private final int capacity;
    private final int number_of_quests;
    private final String status;
    private final String waitress;
    private final HashMap<Integer, Boolean> order_statuses;

    public TableDTO(int id, boolean occupied, int table_number, int capacity, int number_of_quests, String status, String waitress, HashMap<Integer, Boolean> order_statuses) {
        this.table_id = id;
        this.occupied = occupied;
        this.table_number = table_number;
        this.capacity = capacity;
        this.number_of_quests = number_of_quests;
        this.status = status;
        this.waitress = waitress;
        this.order_statuses = order_statuses;
    }

    public int getTable_id() {
        return table_id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getTable_number() {
        return table_number;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumber_of_quests() {
        return number_of_quests;
    }

    public String getStatus() {
        return status;
    }

    public String getWaitress() {
        return waitress;
    }

    public HashMap<Integer, Boolean> getOrder_statuses() {
        return order_statuses;
    }

    @Override
    public String toString() {
        return "TableDTO{" +
                "table_id=" + table_id +
                ", occupied=" + occupied +
                ", table_number=" + table_number +
                ", capacity=" + capacity +
                ", number_of_quests=" + number_of_quests +
                ", status='" + status + '\'' +
                ", waitress='" + waitress + '\'' +
                ", order_statuses=" + order_statuses +
                '}';
    }
}
