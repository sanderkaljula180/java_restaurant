package org.example.entities;

public class RestaurantTable {
    private int id;
    private int table_number;
    private boolean occupied;
    private int number_of_guests;
    private int table_capacity;
    private int waitress_id;
    private String status;

    public RestaurantTable() {
    }

    public RestaurantTable(int id, int table_number, boolean occupied, int number_of_guests, int tableCapacity, int waitressId, String status) {
        this.id = id;
        this.table_number = table_number;
        this.occupied = occupied;
        this.number_of_guests = number_of_guests;
        this.table_capacity = tableCapacity;
        this.waitress_id = waitressId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getTable_number() {
        return table_number;
    }

    public void setTable_number(int table_number) {
        this.table_number = table_number;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public int getNumber_of_guests() {
        return number_of_guests;
    }

    public void setNumber_of_guests(int number_of_guests) {
        this.number_of_guests = number_of_guests;
    }

    public int getTable_capacity() {
        return table_capacity;
    }

    public void setTable_capacity(int table_capacity) {
        this.table_capacity = table_capacity;
    }

    public int getWaitress_id() {
        return waitress_id;
    }

    public void setWaitress_id(int waitress_id) {
        this.waitress_id = waitress_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RestaurantTable{" +
                "id=" + id +
                ", table_number=" + table_number +
                ", occupied=" + occupied +
                ", number_of_guests=" + number_of_guests +
                ", table_capacity=" + table_capacity +
                ", waitress_id=" + waitress_id +
                ", status='" + status + '\'' +
                '}';
    }
}
