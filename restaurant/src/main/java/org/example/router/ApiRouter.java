package org.example.router;

import com.sun.net.httpserver.HttpServer;
import org.example.api.ItemController;
import org.example.api.OrderController;
import org.example.api.OrderItemController;
import org.example.api.TablesController;

import java.io.IOException;

public class ApiRouter {

    private final ItemController itemController;
    private final TablesController tablesController;
    private final OrderController orderController;
    private final OrderItemController orderItemController;

    public ApiRouter(ItemController itemController, TablesController tablesController, OrderController orderController, OrderItemController orderItemController) {
        this.itemController = itemController;
        this.tablesController = tablesController;
        this.orderController = orderController;
        this.orderItemController = orderItemController;
    }

    public void serverRouter(HttpServer httpServer) throws IOException {
        httpServer.createContext("/api/items/", itemController::getAllItems);
        httpServer.createContext("/api/tables/", tablesController::getAllTables);
        httpServer.createContext("/api/tables/table_setup/", tablesController::getTableForSetup);
        httpServer.createContext("/api/tables/occupy/", tablesController::occupyTable);
        httpServer.createContext("/api/tables/change_status/ready_for_order", tablesController::changeTableStatusIntoReadyForOrder);
        httpServer.createContext("/api/order/add_order", orderController::addNewOrder);
        httpServer.createContext("/api/order-items", orderItemController::getAllOrderItems);
    }
}
