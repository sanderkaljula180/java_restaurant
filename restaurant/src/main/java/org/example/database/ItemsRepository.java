package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.entities.Item;
import org.example.exceptions.DatabaseUpdateException;
import org.example.exceptions.ResourceNotAvailable;
import org.example.exceptions.ResourcesNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsRepository {

    private final DBConnectionPool cp;

    public ItemsRepository(DBConnectionPool cp) {
        this.cp = cp;
    }

    public List<Item> getAllItems() {
        List<Item> allItemsArrayList = new ArrayList<>();
        String sqlStatement = "SELECT * FROM items;";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getBigDecimal(3),
                        resultSet.getInt(4)
                );
                allItemsArrayList.add(item);
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
        return allItemsArrayList;
    }

    public Item findItemById(int itemId) {
        String sqlStatement = "SELECT * FROM items WHERE id = ?";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, itemId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return helperForCreatingItemObj(resultSet);
                } else {
                    throw new ResourcesNotFoundException("Item not found: " + itemId);
                }
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    public Map<Integer, Integer> findItemQuantitiesByIds(List<Integer> itemIds) {
        if (itemIds.isEmpty()) {
            throw new IllegalArgumentException("List for finding item quantities is empty");
        }
        StringBuilder sb = new StringBuilder("SELECT id, items_in_stock FROM items WHERE id IN (");
        for (int i = 0; i < itemIds.size(); i++) {
            sb.append("?");
            if (i < itemIds.size() - 1) sb.append(", ");
        }
        sb.append(")");
        String sqlStatement = sb.toString();
        Map<Integer, Integer> quantityForItemId = new HashMap<>();
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            int i = 1;
            for (Integer itemId : itemIds) {
                statement.setInt(i++, itemId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    quantityForItemId.put(resultSet.getInt(1), resultSet.getInt(2));
                }
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
        return quantityForItemId;
    }

    // IF YOU COME HERE WHEN REFACTORING THEN CHANGE PREPAREDSTATEMENT EVERY WHERE LIKE THIS CUZ IT TAKES DB RESOURCES
    public void reduceItemQuantity(int numberToReduce, int itemId) {
        String sqlStatement = "UPDATE items SET items_in_stock = items_in_stock - ? WHERE id = ?";
        try (Connection c = cp.createConnection();
             PreparedStatement statement = c.prepareStatement(sqlStatement)) {
            statement.setInt(1, numberToReduce);
            statement.setInt(2, itemId);
            int rowCount = statement.executeUpdate();
            if (rowCount != 1) {
                throw new DatabaseUpdateException("Database update query didn't work: " + statement);
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    private Item helperForCreatingItemObj(ResultSet resultSet) throws SQLException {
        return new Item(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getBigDecimal(3),
                resultSet.getInt(4)
        );
    }

}
