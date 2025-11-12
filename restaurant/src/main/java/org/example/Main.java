package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.api.ItemController;
import org.example.api.TablesController;
import org.example.configuration.DBConnectionPool;
import org.example.configuration.HttpServerConfiguration;
import org.example.database.ItemsRepository;
import org.example.database.OrderRepository;
import org.example.database.TablesRepository;
import org.example.database.WaitressRepository;
import org.example.helpers.Mapper;
import org.example.router.ApiRouter;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        DBConnectionPool pool = new DBConnectionPool();
        ItemsRepository itemsRepository = new ItemsRepository(pool);
        TablesRepository tablesRepository = new TablesRepository(pool);
        WaitressRepository waitressRepository = new WaitressRepository(pool);
        OrderRepository orderRepository = new OrderRepository(pool);

        Mapper mapper = new Mapper();

        ItemController itemController = new ItemController(itemsRepository);
        TablesController tablesController = new TablesController(
                tablesRepository,
                waitressRepository,
                orderRepository,
                mapper
        );

        ApiRouter apiRouter = new ApiRouter(itemController, tablesController);
        HttpServerConfiguration config = new HttpServerConfiguration(apiRouter);
        HttpServer httpServer = config.httpServer();
        httpServer.start();

    }
}