package org.example.helpers;

import org.example.dto.TableDTO;
import org.example.entities.Order;
import org.example.entities.RestaurantTable;
import org.example.entities.Waitress;

import java.util.HashMap;
import java.util.List;

public class Mapper {

    public TableDTO toTableTdo(RestaurantTable restaurantTable, Waitress waitress, List<Order> orders) {
        int table_id = restaurantTable.getId();
        boolean occupied = restaurantTable.isOccupied();
        int table_number = restaurantTable.getTable_number();
        int capacity = restaurantTable.getTable_capacity();
        int number_of_quests = restaurantTable.getNumber_of_guests();
        String status = restaurantTable.getStatus();
        String waitress_name = waitress.getName();
        HashMap<Integer, Boolean> order_statuses = new HashMap<>();

        orders.forEach(order -> order_statuses.put(order.getId(), order.isReady()));

        return new TableDTO(
                table_id,
                occupied,
                table_number,
                capacity,
                number_of_quests,
                status,
                waitress_name,
                order_statuses
                );
    }


}
