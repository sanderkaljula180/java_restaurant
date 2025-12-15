package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.entities.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemsRepository {

    private final DBConnectionPool cp;

    public ItemsRepository(DBConnectionPool cp) {
        this.cp = cp;
    }

    public List<Item> getAllItems() throws SQLException {
        List<Item> allItemsArrayList = new ArrayList<>();
        String sqlStatement = "SELECT * FROM items;";
        try (Connection connection = cp.createConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4)
                );
                allItemsArrayList.add(item);
            }
        }
        return allItemsArrayList;
    }

}
