package org.example.api;

import com.sun.net.httpserver.HttpExchange;
import org.example.configuration.StacktraceConfig;
import org.example.dto.AddOrderRequestDTO;
import org.example.dto.OrderItemsForOrderRequestDTO;
import org.example.helpers.JsonResponseConverter;
import org.example.helpers.Mapper;
import org.example.response.ApiResponse;
import org.example.response.ErrorResponse;
import org.example.services.OrderService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderController {

    private final OrderService orderService;
    private final JsonResponseConverter jsonResponseConverter;
    private final ErrorResponse errorResponse;
    private final ApiResponse apiResponse;

    public OrderController(OrderService orderService, JsonResponseConverter jsonResponseConverter, ErrorResponse errorResponse, ApiResponse apiResponse) {
        this.orderService = orderService;
        this.jsonResponseConverter = jsonResponseConverter;
        this.errorResponse = errorResponse;
        this.apiResponse = apiResponse;
    }

    public void addNewOrder(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("POST")) {
            try {
                JSONObject jsonObject = jsonResponseConverter.convertRequestBodyJsonByteIntoJsonObject(httpExchange);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                List<OrderItemsForOrderRequestDTO> orderItemsForOrderRequestDTOList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    OrderItemsForOrderRequestDTO orderItemsForOrderRequestDTO = new OrderItemsForOrderRequestDTO(
                            jsonArray.getJSONObject(i).getInt("itemId"),
                            jsonArray.getJSONObject(i).getInt("quantity")
                    );
                    orderItemsForOrderRequestDTOList.add(orderItemsForOrderRequestDTO);
                }
                AddOrderRequestDTO addOrderRequestDTO = Mapper.toAddOrderRequestDTO(
                        jsonObject.getInt("tableId"),
                        orderItemsForOrderRequestDTOList
                );
                byte[] byteResponse = jsonResponseConverter.convertDTOIntoJsonByte(orderService.addNewOrder(addOrderRequestDTO));
                apiResponse.okResponse(byteResponse, httpExchange);
            } catch (Exception e) {
                StacktraceConfig.logStackTraceFromThread(e);
                errorResponse.errorResponse(httpExchange, e);
            }
        }
    }

}
