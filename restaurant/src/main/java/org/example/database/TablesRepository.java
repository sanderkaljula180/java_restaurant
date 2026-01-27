package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.dto.RestaurantTableOrderCompletedDTO;
import org.example.dto.TableStatusUpdateResponseDTO;
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

    // I have to refactor updateRestaurantTableStatusById and
    // updateRestaurantTableStatusIntoOrderCompleted. Make it use DTO both so I can delete one from here.
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

    public void updateRestaurantTableStatusIntoOrderCompleted(int tableId, String newStatus) {
        String sqlStatement = "UPDATE restaurant_table SET " +
                "status = ? " +
                "WHERE id = ?";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setString(1, newStatus);
            statement.setInt(2, tableId);
            int rowCount = statement.executeUpdate();
            if (rowCount != 1) {
                throw new DatabaseUpdateException("Database update query didn't work: " + statement);
            }
        } catch (SQLException e) {
            throw new ResourceNotAvailable("Database error", e);
        }
    }

    public RestaurantTableOrderCompletedDTO findTableWithJoinOrderByTableId(int tableId) {
        String sqlStatement = "SELECT rt.id, rt.status, rt.is_occupied, bool_and(o.is_ready) AS all_ready FROM restaurant_table rt JOIN orders o ON rt.id = o.table_id WHERE rt.id = ? GROUP BY rt.id, rt.status, rt.is_occupied";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setInt(1, tableId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new RestaurantTableOrderCompletedDTO(
                            resultSet.getInt("id"),
                            resultSet.getString("status"),
                            resultSet.getBoolean("is_occupied"),
                            resultSet.getBoolean("all_ready")
                    );
                } else {
                    throw new ResourcesNotFoundException("Table not found: " + tableId);
                }
            }
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
                resultSet.getInt("id"),
                resultSet.getInt("table_number"),
                resultSet.getBoolean("is_occupied"),
                resultSet.getInt("number_of_guests"),
                resultSet.getInt("table_capacity"),
                resultSet.getInt("waitress_id"),
                resultSet.getString("status")
        );
    }


}
