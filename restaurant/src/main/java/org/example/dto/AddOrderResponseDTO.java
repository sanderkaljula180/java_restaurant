package org.example.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddOrderResponseDTO {
    private final String message;
    private final String status;
    private final List<OrderItemDTO> orderItemDTO;
    private final BigDecimal orderPrice;

    public AddOrderResponseDTO(String message, String status, List<OrderItemDTO> orderItemDTO, BigDecimal orderPrice) {
        this.message = message;
        this.status = status;
        this.orderItemDTO = orderItemDTO;
        this.orderPrice = orderPrice;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItemDTO> getOrderItemDTO() {
        return orderItemDTO;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }
}
