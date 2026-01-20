package org.example.services;

import org.example.database.OrderRepository;
import org.example.dto.AddOrderRequestDTO;
import org.example.dto.AddOrderResponseDTO;
import org.example.dto.OrderItemDTO;
import org.example.entities.Order;
import org.example.entities.RestaurantTable;
import org.example.helpers.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final TableService tableService;
    private final OrderItemService orderItemService;
    private final OrderRepository orderRepository;
    private final ItemService itemService;

    public OrderService(TableService tableService, OrderItemService orderItemService, OrderRepository orderRepository, ItemService itemService) {
        this.tableService = tableService;
        this.orderItemService = orderItemService;
        this.orderRepository = orderRepository;
        this.itemService = itemService;
    }

    public AddOrderResponseDTO addNewOrder(AddOrderRequestDTO addOrderRequestDTO) {
        if (!itemService.areThereEnoughItems(addOrderRequestDTO.getItems())) {
            throw new IllegalStateException("Not enough items in stock");
        }

        RestaurantTable restaurantTable = tableService.findTableById(addOrderRequestDTO.getTableId());
        Order newOrder = new Order(
                restaurantTable.getId(),
                false,
                LocalDateTime.now(),
                new BigDecimal(0),
                restaurantTable.getWaitress_id(),
                false
        );
        newOrder = orderRepository.insertNewOrderAndReturnOrder(newOrder);
        List<OrderItemDTO> orderItemDTO = orderItemService.createOrderItems(addOrderRequestDTO.getItems(), newOrder.getId());

        newOrder.setOrder_price(orderItemDTO.stream().map(OrderItemDTO::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderRepository.updateOrderPrice(newOrder);

        return Mapper.addOrderResponseDTO(
                newOrder,
                tableService.updateTableStatusIntoWaitingForOrder(newOrder.getTable_id()),
                orderItemDTO
        );
    }
}
