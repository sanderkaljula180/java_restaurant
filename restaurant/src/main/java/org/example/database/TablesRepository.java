package org.example.database;

import org.example.configuration.DBConnectionPool;
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
        List<RestaurantTable> restaurantTableList = new ArrayList<>();
        String sqlStatement = "SELECT * FROM restaurant_tables";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
               RestaurantTable restaurantTable = new RestaurantTable(
                       resultSet.getInt(1),
                       resultSet.getInt(2),
                       resultSet.getBoolean(3),
                       resultSet.getInt(4),
                       resultSet.getInt(5),
                       resultSet.getInt(6),
                       resultSet.getString(7)
               );
               restaurantTableList.add(restaurantTable);
            }
        }
        return restaurantTableList;
    }


}
