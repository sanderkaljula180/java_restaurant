package org.example.helpers;

import org.example.dto.ErrorResponseDTO;
import org.example.dto.TableDTO;
import org.example.dto.TableSetupDTO;
import org.example.entities.Order;
import org.example.entities.RestaurantTable;
import org.example.entities.Waitress;

import java.util.HashMap;
import java.util.List;

public class Mapper {

    public static TableDTO toTableDto(RestaurantTable restaurantTable, Waitress waitress, List<Order> orders) {

        HashMap<Integer, Boolean> order_statuses = new HashMap<>();
        orders.forEach(order -> order_statuses.put(order.getId(), order.isReady()));

        String waitressName = "None";
        if (waitress != null) {
            waitressName = waitress.getName();
        }

        return new TableDTO(
                restaurantTable.getId(),
                restaurantTable.isOccupied(),
                restaurantTable.getTable_number(),
                restaurantTable.getTable_capacity(),
                restaurantTable.getNumber_of_guests(),
                restaurantTable.getStatus(),
                waitressName,
                order_statuses
                );
    }

    public static TableSetupDTO toTableSetupDto(RestaurantTable restaurantTable, List<Waitress> waitresses) {

        HashMap<Integer, String> availableWaitresses = new HashMap<>();
        waitresses.forEach(waitress -> availableWaitresses.put(waitress.getId(), waitress.getName()));

        return new TableSetupDTO(
                restaurantTable.getId(),
                restaurantTable.getTable_number(),
                restaurantTable.getTable_capacity(),
                availableWaitresses
        );
    }

}
