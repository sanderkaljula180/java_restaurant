package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.entities.Order;
import org.example.exceptions.DatabaseUpdateException;
import org.example.exceptions.ResourceNotAvailable;
import org.example.exceptions.ResourcesNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private final DBConnectionPool cp;


    public OrderRepository(DBConnectionPool cp) {
        this.cp = cp;
    }
    
    public List<Order> findOrdersByRestaurantTableId(int tableId) {
        List<Order> ordersByTableId = new ArrayList<>();
        String sqlStatement = "SELECT * FROM orders WHERE table_id = ? AND is_paid = FALSE";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, tableId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    ordersByTableId.add(helperForCreatingOrderObj(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
        return ordersByTableId;
    }

    public Order insertNewOrderAndReturnOrder(Order newOrder) {
        String sqlStatement = "INSERT INTO orders (table_id, is_paid, order_time, order_price, waitress_id, is_ready)" +
                " VALUES (?, ?, ?, ?, ?, ?) RETURNING id, table_id, is_paid, order_time, order_price, waitress_id, is_ready";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, newOrder.getTable_id());
            statement.setBoolean(2, newOrder.isPaid());
            statement.setObject(3, newOrder.getOrder_time());
            statement.setBigDecimal(4, newOrder.getOrder_price());
            statement.setInt(5, newOrder.getWaitress_id());
            statement.setBoolean(6, newOrder.isReady());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return helperForCreatingOrderObj(resultSet);
                } else {
                    throw new ResourcesNotFoundException("Order not found after creation.");
                }
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    public void updateOrderPrice(Order updateOrder) {
        String sqlStatement = "UPDATE orders SET order_price = ? WHERE id = ?";
        try (Connection connection = cp.createConnection();
            PreparedStatement statement = connection.prepareStatement(sqlStatement)) {
            statement.setBigDecimal(1, updateOrder.getOrder_price());
            statement.setInt(2, updateOrder.getId());
            int rowCount = statement.executeUpdate();
            if (rowCount != 1) {
                throw new DatabaseUpdateException("Database update query didn't work: " + statement);
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    private Order helperForCreatingOrderObj(ResultSet resultSet) throws SQLException {
        return new Order(
                resultSet.getInt(1),
                resultSet.getInt(2),
                resultSet.getBoolean(3),
                resultSet.getObject(4, LocalDateTime.class),
                resultSet.getBigDecimal(5),
                resultSet.getInt(6),
                resultSet.getBoolean(7)
        );
    }

}
