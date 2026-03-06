package org.example.helpers;

import org.example.dto.AddOrderResponseDTO;
import org.example.dto.OrderItemDTO;
import org.example.dto.TableStatusUpdateResponseDTO;
import org.example.entities.Order;

import java.util.List;

public interface ObjectMapper {
    AddOrderResponseDTO toAddOrderResponse(Order order, TableStatusUpdateResponseDTO tableStatusUpdateResponseDTO, List<OrderItemDTO> orderItemDTO);
}
