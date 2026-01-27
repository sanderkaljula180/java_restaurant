package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.dto.OrderItemsForKitchenDTO;
import org.example.dto.OrderItemsForOrderRequestDTO;
import org.example.dto.OrderStatusUpdateDTO;
import org.example.entities.Order;
import org.example.entities.OrderItem;
import org.example.exceptions.DatabaseUpdateException;
import org.example.exceptions.ResourceNotAvailable;
import org.example.exceptions.ResourcesNotFoundException;

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

    public boolean getOrderItemStatusById(int orderItemId) {
        String sqlStatement = "SELECT is_ready FROM order_items WHERE id = ?";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, orderItemId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("is_ready");
                } else {
                    throw new ResourcesNotFoundException("Order item not found: " + orderItemId);
                }
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    public void updateOrderItemIntoReady(int orderItemId) {
        String sqlStatement = "UPDATE order_items SET is_ready = true WHERE id = ?";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, orderItemId);
            int rowCount = statement.executeUpdate();
            if (rowCount != 1) {
                throw new DatabaseUpdateException("Database update query didn't work: " + statement);
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    public OrderStatusUpdateDTO areAllRelatedOrderItemsReady(int orderItemId) {
        String sqlStatement = "SELECT bool_and(is_ready), order_id FROM order_items WHERE order_id = (SELECT order_id FROM order_items WHERE id = ?) group by order_id";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, orderItemId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new OrderStatusUpdateDTO(
                      resultSet.getInt(2),
                      resultSet.getBoolean(1)
                    );
                } else {
                    throw new ResourcesNotFoundException("Order item not found: " + orderItemId);
                }
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

}
