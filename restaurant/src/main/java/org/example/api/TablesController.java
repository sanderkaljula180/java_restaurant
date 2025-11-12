package org.example.api;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.example.database.OrderRepository;
import org.example.database.TablesRepository;
import org.example.database.WaitressRepository;
import org.example.dto.TableDTO;
import org.example.entities.RestaurantTable;
import org.example.helpers.Mapper;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TablesController {

    private final TablesRepository tablesRepository;
    private final WaitressRepository waitressRepository;
    private final OrderRepository orderRepository;
    private final Mapper mapper;

    public TablesController(TablesRepository tablesRepository, WaitressRepository waitressRepository, OrderRepository orderRepository, Mapper mapper) {
        this.tablesRepository = tablesRepository;
        this.waitressRepository = waitressRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    public void getAllTables(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                LocalDateTime time =  LocalDateTime.now();
                Headers headers = httpExchange.getResponseHeaders();
                headers.add("Date", String.valueOf(time));
                headers.add("Content-Type", "text/html");
                headers.add("Connection", "keep-alive");
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream response = httpExchange.getResponseBody();
                List<RestaurantTable> restaurantTableList = tablesRepository.getAllTables();
                List<TableDTO> tableDTOList = restaurantTableList.stream()
                        .map(table -> {
                            try {
                                return mapper.toTableTdo(
                                        table,
                                        waitressRepository
                                                .findWaitressNameById(table.getWaitress_id()),
                                        orderRepository
                                                .findOrderByRestaurantTableId(table.getId())
                                        );
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                        }).collect(toList());
                System.out.println(tableDTOList);
                String tableTdoForTesting = tableDTOList.toString();
                byte[] bytes = tableTdoForTesting.getBytes();
                response.write(bytes);
                response.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
