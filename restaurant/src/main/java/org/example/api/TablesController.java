package org.example.api;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.example.database.OrderRepository;
import org.example.database.TablesRepository;
import org.example.database.WaitressRepository;
import org.example.dto.TableDTO;
import org.example.entities.RestaurantTable;
import org.example.helpers.JsonResponseConverter;
import org.example.helpers.Mapper;
import org.example.services.TableService;
import org.json.JSONArray;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TablesController {

    private final TableService tableService;
    private final JsonResponseConverter jsonConverter;

    public TablesController(TableService tableService, JsonResponseConverter jsonConverter) {
        this.tableService = tableService;
        this.jsonConverter = jsonConverter;
    }

    public void getAllTables(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                LocalDateTime time =  LocalDateTime.now();
                Headers headers = httpExchange.getResponseHeaders();
                headers.add("Date", String.valueOf(time));
                headers.add("Content-Type", "application/json");
                headers.add("Connection", "keep-alive");
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream response = httpExchange.getResponseBody();
                response.write(jsonConverter
                        .convertArrayIntoJsonByte(
                           tableService.getAllTables()
                        ));
                response.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
