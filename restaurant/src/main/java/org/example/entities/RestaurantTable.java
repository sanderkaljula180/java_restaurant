package org.example.entities;

public class RestaurantTable {
    private int id;
    private int table_number;
    private boolean occupied;
    private int number_of_guests;

    public RestaurantTable() {
    }

    public RestaurantTable(int id, int table_number, boolean occupied, int number_of_guests) {
        this.id = id;
        this.table_number = table_number;
        this.occupied = occupied;
        this.number_of_guests = number_of_guests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
