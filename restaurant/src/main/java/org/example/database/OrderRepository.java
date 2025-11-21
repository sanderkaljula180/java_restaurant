package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.entities.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private final DBConnectionPool cp;


    public OrderRepository(DBConnectionPool cp) {
        this.cp = cp;
    }
    
    public List<Order> findOrdersByRestaurantTableId(int tableId) throws SQLException {
        List<Order> ordersByTableId = new ArrayList<>();
        String sqlStatement = "SELECT * FROM orders WHERE table_id = ? AND paid = FALSE";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, tableId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    Order order = new Order(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getBoolean(3),
                            resultSet.getObject(4, LocalDateTime.class),
                            resultSet.getFloat(5),
                            resultSet.getInt(6),
                            resultSet.getBoolean(7)
                    );
                    ordersByTableId.add(order);
                }
            }
        }
        return ordersByTableId;
    }
}
