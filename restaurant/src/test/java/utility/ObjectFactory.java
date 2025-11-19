package utility;

import org.example.entities.Order;
import org.example.entities.RestaurantTable;
import org.example.entities.Waitress;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {


    public static List<RestaurantTable> createListOfRestaurantTablesWithData(int howManyObjects) {
        List<RestaurantTable> restaurantTableList = new ArrayList<>();
        for (int i=1; i <= howManyObjects; i++) {
            RestaurantTable restaurantTable = new RestaurantTable(
                    i,
                    i,
                    false,
                    0,
                    5,
                    i,
                    "FREE"
            );
            restaurantTableList.add(restaurantTable);
        }
        return restaurantTableList;
    }

    public static Waitress createAvailableWaitressObject(int waitressId, String name, boolean availability) {
        Waitress waitress = new Waitress(
                waitressId,
                name,
                availability
        );
        return waitress;
    }

    public static Order createNewOrderObject(int orderId, int tableId, boolean isPaid, LocalDateTime orderTime, float orderPrice, int waitressId, boolean isReady) {
        Order order = new Order(
                orderId,
                tableId,
                isPaid,
                orderTime,
                orderPrice,
                waitressId,
                isReady
        );

        return order;
    }




}
