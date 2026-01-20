package org.example;

import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.api.ItemController;
import org.example.api.OrderController;
import org.example.api.OrderItemController;
import org.example.api.TablesController;
import org.example.configuration.DBConnectionPool;
import org.example.configuration.HttpServerConfiguration;
import org.example.database.*;
import org.example.entities.Order;
import org.example.helpers.JsonResponseConverter;
import org.example.response.ApiResponse;
import org.example.response.ErrorResponse;
import org.example.router.ApiRouter;
import org.example.services.ItemService;
import org.example.services.OrderItemService;
import org.example.services.OrderService;
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
        OrderItemRepository orderItemRepository = new OrderItemRepository(pool);

        JsonResponseConverter jsonResponseConverter = new JsonResponseConverter();
        ErrorResponse errorResponse = new ErrorResponse(jsonResponseConverter);

        ItemService itemService = new ItemService(
                itemsRepository
        );
        OrderItemService orderItemService = new OrderItemService(
                orderItemRepository,
                itemService
        );
        TableService tableService = new TableService(
                tablesRepository,
                waitressRepository,
                orderRepository
        );
        OrderService orderService = new OrderService(
                tableService,
                orderItemService,
                orderRepository,
                itemService
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
        OrderController orderController = new OrderController(
              orderService,
              jsonResponseConverter,
              errorResponse,
              apiResponse
        );
        OrderItemController orderItemController = new OrderItemController(
                jsonResponseConverter,
                errorResponse,
                orderItemService
        );

        ApiRouter apiRouter = new ApiRouter(itemController, tablesController, orderController, orderItemController);
        HttpServerConfiguration config = new HttpServerConfiguration(apiRouter);
        HttpServer httpServer = config.httpServer();
        httpServer.start();
        log.info("APPLICATION HAS STARTED!");


    }
}