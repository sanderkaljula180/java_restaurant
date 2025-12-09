package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.configuration.ResourcesNotFoundException;
import org.example.entities.RestaurantTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TablesRepository {

    private final DBConnectionPool cp;

    public TablesRepository(DBConnectionPool cp) {
        this.cp = cp;
    }

    public List<RestaurantTable> getAllTables() throws SQLException {
        String sqlStatement = "SELECT * FROM restaurant_tables";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<RestaurantTable> restaurantTableList = new ArrayList<>();
                while (resultSet.next()) {
                    restaurantTableList.add(helperForCreatingRestaurantTableObj(resultSet));
                }
                return restaurantTableList;
            }
        }
    }

    public RestaurantTable findTableByTableId(int tableId) throws SQLException {
        String sqlStatement = "SELECT * FROM restaurant_tables WHERE id = ? AND occupied = false";
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
        }
    }


    public RestaurantTable helperForCreatingRestaurantTableObj(ResultSet resultSet) throws SQLException {
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
