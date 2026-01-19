package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.entities.Order;
import org.example.entities.OrderItem;
import org.example.exceptions.DatabaseUpdateException;
import org.example.exceptions.ResourceNotAvailable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderItemRepository {
    private final DBConnectionPool cp;

    public OrderItemRepository(DBConnectionPool cp) {
        this.cp = cp;
    }

    public void insertNewOrderItems(List<OrderItem> orderItems) {
        String sqlStatement = "INSERT INTO order_items (order_id, item_id, quantity, price, is_ready)" +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            for (OrderItem item : orderItems) {
                statement.setInt(1, item.getOrder_id());
                statement.setInt(2, item.getItem_id());
                statement.setInt(3, item.getQuantity());
                statement.setBigDecimal(4, item.getPrice());
                statement.setBoolean(5, item.isReady());
                int rowCount = statement.executeUpdate();
                if (rowCount != 1) {
                    throw new DatabaseUpdateException("Database insert query didn't work: " + statement);
                }
            }


        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }

    }
}
