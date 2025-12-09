package org.example;

import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.api.ItemController;
import org.example.api.TablesController;
import org.example.configuration.DBConnectionPool;
import org.example.configuration.HttpServerConfiguration;
import org.example.database.ItemsRepository;
import org.example.database.OrderRepository;
import org.example.database.TablesRepository;
import org.example.database.WaitressRepository;
import org.example.helpers.JsonResponseConverter;
import org.example.response.ApiResponse;
import org.example.response.ErrorResponse;
import org.example.router.ApiRouter;
import org.example.services.TableService;


import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {



        DBConnectionPool pool = new DBConnectionPool();
        ItemsRepository itemsRepository = new ItemsRepository(pool);
        TablesRepository tablesRepository = new TablesRepository(pool);
        WaitressRepository waitressRepository = new WaitressRepository(pool);
        OrderRepository orderRepository = new OrderRepository(pool);

        JsonResponseConverter jsonResponseConverter = new JsonResponseConverter();
        ErrorResponse errorResponse = new ErrorResponse(jsonResponseConverter);

        TableService tableService = new TableService(
                tablesRepository,
                waitressRepository,
                orderRepository
        );

        ApiResponse apiResponse = new ApiResponse();
        ItemController itemController = new ItemController(
                itemsRepository,
                errorResponse,
                apiResponse,
                jsonResponseConverter
        );
        TablesController tablesController = new TablesController(
                tableService,
                jsonResponseConverter,
                errorResponse,
                apiResponse
        );

        ApiRouter apiRouter = new ApiRouter(itemController, tablesController);
        HttpServerConfiguration config = new HttpServerConfiguration(apiRouter);
        HttpServer httpServer = config.httpServer();
        httpServer.start();
        log.info("APPLICATION HAS STARTED!");


    }
}