package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.entities.RestaurantTable;
import org.example.exceptions.DatabaseUpdateException;
import org.example.exceptions.ResourceNotAvailable;
import org.example.exceptions.ResourcesNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TablesRepository {

    private final DBConnectionPool cp;

    public TablesRepository(DBConnectionPool cp) {
        this.cp = cp;
    }

    public List<RestaurantTable> getAllTables() {
        String sqlStatement = "SELECT * FROM restaurant_table";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<RestaurantTable> restaurantTableList = new ArrayList<>();
                while (resultSet.next()) {
                    restaurantTableList.add(helperForCreatingRestaurantTableObj(resultSet));
                }
                System.out.println(restaurantTableList);
                return restaurantTableList;
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    public RestaurantTable findTableByTableId(int tableId) {
        String sqlStatement = "SELECT * FROM restaurant_table WHERE id = ?";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, tableId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return helperForCreatingRestaurantTableObj(resultSet);
                } else {
                    throw new ResourcesNotFoundException("Table not found: " + tableId);
                }
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    public RestaurantTable updateRestaurantTableById(RestaurantTable restaurantTable) {
        String sqlStatement = "UPDATE restaurant_table SET " +
                "table_number = ?, " +
                "is_occupied = ?, " +
                "number_of_guests = ?, " +
                "table_capacity = ?, " +
                "waitress_id = ?, " +
                "status = ? " +
                "WHERE id = ?";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, restaurantTable.getTable_number());
            statement.setBoolean(2, restaurantTable.isOccupied());
            statement.setInt(3, restaurantTable.getNumber_of_guests());
            statement.setInt(4, restaurantTable.getTable_capacity());
            statement.setInt(5, restaurantTable.getWaitress_id());
            statement.setString(6, restaurantTable.getStatus());
            statement.setInt(7, restaurantTable.getId());
            return executeUpdateOnOneItemAndReturnObject(restaurantTable, statement);
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    public RestaurantTable updateRestaurantTableStatusById(RestaurantTable restaurantTable) {
        String sqlStatement = "UPDATE restaurant_table SET " +
                "status = ? " +
                "WHERE id = ?";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setString(1, restaurantTable.getStatus());
            statement.setInt(2, restaurantTable.getId());
            return executeUpdateOnOneItemAndReturnObject(restaurantTable, statement);
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    private RestaurantTable executeUpdateOnOneItemAndReturnObject(RestaurantTable restaurantTable, PreparedStatement statement) throws SQLException {
        int rowCount = statement.executeUpdate();
        if (rowCount == 1) {
            return findTableByTableId(restaurantTable.getId());
        }
        throw new DatabaseUpdateException("Database update query didn't work: " + statement);
    }

    private RestaurantTable helperForCreatingRestaurantTableObj(ResultSet resultSet) throws SQLException {
        return new RestaurantTable(
                resultSet.getInt(1),
                resultSet.getInt(2),
                resultSet.getBoolean(3),
                resultSet.getInt(4),
                resultSet.getInt(5),
                resultSet.getInt(6),
                resultSet.getString(7)
        );
    }


}
