package org.example.api;

import com.sun.net.httpserver.HttpExchange;
import org.example.configuration.StacktraceConfig;
import org.example.dto.OrderItemDTO;
import org.example.helpers.JsonResponseConverter;
import org.example.response.ErrorResponse;
import org.example.services.OrderItemService;
import org.json.JSONObject;

import java.io.IOException;

public class OrderItemController {

    private final JsonResponseConverter jsonResponseConverter;
    private final ErrorResponse errorResponse;
    private final OrderItemService orderItemService;

    public OrderItemController(JsonResponseConverter jsonResponseConverter, ErrorResponse errorResponse, OrderItemService orderItemService) {
        this.jsonResponseConverter = jsonResponseConverter;
        this.errorResponse = errorResponse;
        this.orderItemService = orderItemService;
    }

    public void getAllOrderItems(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                orderItemService.getAllOrderItems();
            } catch (Exception e) {
                StacktraceConfig.logStackTraceFromThread(e);
                errorResponse.errorResponse(httpExchange, e);
            }
        }
    }

}
