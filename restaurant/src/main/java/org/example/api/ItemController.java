package org.example.api;

import com.sun.net.httpserver.*;
import org.example.configuration.StacktraceConfig;
import org.example.database.ItemsRepository;
import org.example.entities.Item;
import org.example.helpers.JsonResponseConverter;
import org.example.response.ApiResponse;
import org.example.response.ErrorResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ItemController {

    private final ItemsRepository itemsRepository;
    private final ErrorResponse errorResponse;
    private final ApiResponse apiResponse;
    private final JsonResponseConverter jsonResponseConverter;

    public ItemController(ItemsRepository itemsRepository, ErrorResponse errorResponse, ApiResponse apiResponse, JsonResponseConverter jsonResponseConverter) {
        this.itemsRepository = itemsRepository;
        this.errorResponse = errorResponse;
        this.apiResponse = apiResponse;
        this.jsonResponseConverter = jsonResponseConverter;
    }

    public void getAllItems(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                byte[] byteResponse = jsonResponseConverter.convertArrayIntoJsonByte(itemsRepository.getAllItems());
                apiResponse.okResponse(byteResponse, httpExchange);
            } catch (SQLException e) {
                StacktraceConfig.logStackTraceFromThread(e);
                errorResponse.errorResponse(httpExchange, e);
            }
        }
    }
}