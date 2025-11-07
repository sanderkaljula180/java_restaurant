package org.example.database;

import org.example.configuration.DBConnectionPool;

public class TablesRepository {

    private final DBConnectionPool cp;

    public TablesRepository(DBConnectionPool cp) {
        this.cp = cp;
    }



}
