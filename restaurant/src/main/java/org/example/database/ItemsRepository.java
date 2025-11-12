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
                int id = resultSet.getInt(1);
                String item_name = resultSet.getString(2);
                float item_price = resultSet.getInt(3);
                Item item = new Item(id, item_name, item_price);
                allItemsArrayList.add(item);
            }
        }
        return allItemsArrayList;
    }

}
