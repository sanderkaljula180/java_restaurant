package org.example.helpers;

import org.example.dto.AddOrderResponseDTO;
import org.example.dto.OrderItemDTO;
import org.example.dto.TableStatusUpdateResponseDTO;
import org.example.entities.Order;

import java.util.List;

public class DefaultObjectMapper implements ObjectMapper {
    @Override
    public AddOrderResponseDTO toAddOrderResponse(Order order, TableStatusUpdateResponseDTO tableStatusUpdateResponseDTO, List<OrderItemDTO> orderItemDTO) {
        return new AddOrderResponseDTO(
                "Order created for table " + order.getTable_id(),
                tableStatusUpdateResponseDTO.getNewStatus(),
                orderItemDTO,
                order.getOrder_price()
        );
    }
}
