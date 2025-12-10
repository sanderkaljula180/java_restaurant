package org.example.router;

import com.sun.net.httpserver.HttpServer;
import org.example.api.ItemController;
import org.example.api.TablesController;

import java.io.IOException;

public class ApiRouter {

    private final ItemController itemController;
    private final TablesController tablesController;

    public ApiRouter(ItemController itemController, TablesController tablesController) {
        this.itemController = itemController;
        this.tablesController = tablesController;
    }

    public void serverRouter(HttpServer httpServer) throws IOException {
        httpServer.createContext("/api/items/", itemController::getAllItems);
        httpServer.createContext("/api/tables/", tablesController::getAllTables);
        httpServer.createContext("/api/tables/table_setup/", tablesController::getTableForSetup);
        httpServer.createContext("/api/tables/occupy/", tablesController::occupyTable);
    }
}
