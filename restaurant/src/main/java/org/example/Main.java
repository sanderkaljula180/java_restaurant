package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.api.ItemController;
import org.example.configuration.DBConnectionPool;
import org.example.configuration.HttpServerConfiguration;
import org.example.database.ItemsRepository;
import org.example.router.ApiRouter;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        DBConnectionPool pool = new DBConnectionPool();
        ItemsRepository repository = new ItemsRepository(pool);
        ItemController itemController = new ItemController(repository);
        ApiRouter apiRouter = new ApiRouter(itemController);
        HttpServerConfiguration config = new HttpServerConfiguration(apiRouter);
        HttpServer httpServer = config.httpServer();
        httpServer.start();

    }
}