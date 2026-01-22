package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.dto.OrderItemsForKitchenDTO;
import org.example.dto.OrderItemsForOrderRequestDTO;
import org.example.entities.Order;
import org.example.entities.OrderItem;
import org.example.exceptions.DatabaseUpdateException;
import org.example.exceptions.ResourceNotAvailable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<OrderItemsForKitchenDTO> getAllOrderItemsForKitchen() {
        String sqlStatement = "SELECT i.item_name, oi.quantity, oi.id, oi.is_ready FROM order_items oi JOIN items i ON oi.item_id = i.id";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<OrderItemsForKitchenDTO> orderItemsForKitchenDTO = new ArrayList<>();
                while (resultSet.next()) {
                    orderItemsForKitchenDTO.add(new OrderItemsForKitchenDTO(
                            resultSet.getString("item_name"),
                            resultSet.getInt("quantity"),
                            resultSet.getInt("id"),
                            resultSet.getBoolean("is_ready")
                    ));
                }
                return orderItemsForKitchenDTO;
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }

    }
}
