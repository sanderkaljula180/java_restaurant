package org.example.database;

import org.example.configuration.DBConnectionPool;

public class OrderItemRepository {
    private final DBConnectionPool cp;

    public OrderItemRepository(DBConnectionPool cp) {
        this.cp = cp;
    }


}
