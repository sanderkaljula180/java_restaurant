package org.example.router;

import com.sun.net.httpserver.HttpServer;
import org.example.api.ItemController;

import java.io.IOException;

public class ApiRouter {

    private final ItemController itemController;

    public ApiRouter(ItemController itemController) {
        this.itemController = itemController;
    }

    public HttpServer serverRouter(HttpServer httpServer) throws IOException {
        httpServer.createContext("/api/items", itemController::getAllItems);
        return httpServer;
    }
}
