package org.example.database;

import org.example.configuration.DBConnectionPool;
import org.example.entities.Item;

import java.sql.*;
import java.util.ArrayList;

public class ItemsRepository {
    private final DBConnectionPool cp;

    public ItemsRepository() {
        this.cp = new DBConnectionPool();
    }

    public ArrayList<Item> getAllItems() throws SQLException {
        ArrayList<Item> allItemsArrayList = new ArrayList<>();
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
        } catch (Exception e) {
            System.out.println(e);
        }
        return allItemsArrayList;
    }

}
