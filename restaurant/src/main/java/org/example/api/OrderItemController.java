package org.example.api;

import com.sun.net.httpserver.HttpExchange;
import org.example.configuration.StacktraceConfig;
import org.example.dto.OrderItemDTO;
import org.example.helpers.JsonResponseConverter;
import org.example.response.ApiResponse;
import org.example.response.ErrorResponse;
import org.example.services.OrderItemService;
import org.json.JSONObject;

import java.io.IOException;

public class OrderItemController {

    private final JsonResponseConverter jsonResponseConverter;
    private final ErrorResponse errorResponse;
    private final OrderItemService orderItemService;
    private final ApiResponse apiResponse;

    public OrderItemController(JsonResponseConverter jsonResponseConverter, ErrorResponse errorResponse, OrderItemService orderItemService, ApiResponse apiResponse) {
        this.jsonResponseConverter = jsonResponseConverter;
        this.errorResponse = errorResponse;
        this.orderItemService = orderItemService;
        this.apiResponse = apiResponse;
    }

    public void getAllOrderItems(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                byte[] byteResponse = jsonResponseConverter.convertArrayIntoJsonByte(orderItemService.getAllOrderItems());
                apiResponse.okResponse(byteResponse, httpExchange);
            } catch (Exception e) {
                StacktraceConfig.logStackTraceFromThread(e);
                errorResponse.errorResponse(httpExchange, e);
            }
        }
    }

}
