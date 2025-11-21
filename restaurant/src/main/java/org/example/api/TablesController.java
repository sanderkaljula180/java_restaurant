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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TablesController {

    private static final Logger log = LoggerFactory.getLogger(TablesController.class);
    private final TableService tableService;
    private final JsonResponseConverter jsonConverter;

    public TablesController(TableService tableService, JsonResponseConverter jsonConverter) {
        this.tableService = tableService;
        this.jsonConverter = jsonConverter;
    }

    public void getAllTables(HttpExchange httpExchange) {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                LocalDateTime time =  LocalDateTime.now();
                Headers headers = httpExchange.getResponseHeaders();
                headers.add("Date", String.valueOf(time));
                headers.add("Content-Type", "application/json");
                headers.add("Connection", "keep-alive");
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream response = httpExchange.getResponseBody();
                response.write(jsonConverter.convertArrayIntoJsonByte(
                           tableService.getAllTables()
                        ));
                response.close();
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }
    }

    // I have to check if there is a id behind that also, if not then return exception
    // ALso I have to send back bad request if checkForTableId fails. Create bad request method.
    public void getTableForSetup(HttpExchange httpExchange) {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                int tableId = checkForTableId(httpExchange.getRequestURI().getPath());
                LocalDateTime time =  LocalDateTime.now();
                Headers headers = httpExchange.getResponseHeaders();
                headers.add("Date", String.valueOf(time));
                headers.add("Content-Type", "application/json");
                headers.add("Connection", "keep-alive");
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream response = httpExchange.getResponseBody();
                response.write(jsonConverter
                        .convertDTOIntoJsonByte(
                                tableService.getTableForSetup(tableId)
                        ));
                response.close();
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }

        }
    }

    private static int checkForTableId(String getPath) {
        String[] getTableId = getPath.split("/");
        return Integer.parseInt(getTableId[4]);
    }
}
