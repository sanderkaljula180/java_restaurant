package org.example.services;

import org.example.database.OrderRepository;
import org.example.dto.*;
import org.example.entities.Order;
import org.example.entities.RestaurantTable;
import org.example.exceptions.ConflictException;
import org.example.helpers.Mapper;
import org.example.helpers.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final TableService tableService;
    private final OrderItemService orderItemService;
    private final OrderRepository orderRepository;
    private final ItemService itemService;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public OrderService(TableService tableService, OrderItemService orderItemService, OrderRepository orderRepository, ItemService itemService, ObjectMapper objectMapper, Clock clock) {
        this.tableService = tableService;
        this.orderItemService = orderItemService;
        this.orderRepository = orderRepository;
        this.itemService = itemService;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    public AddOrderResponseDTO addNewOrder(AddOrderRequestDTO addOrderRequestDTO) {
        isThereEnoughItems(addOrderRequestDTO);

        RestaurantTable restaurantTable =
                tableService.findTableById(addOrderRequestDTO.getTableId());

        Order createdOrder = buildNewOrder(restaurantTable);
        Order savedOrder =
                orderRepository.insertNewOrderAndReturnOrder(createdOrder);

        List<OrderItemDTO> orderItemDTO =
                orderItemService.createOrderItems(
                        addOrderRequestDTO.getItems(),
                        savedOrder.getId()
                );

        BigDecimal total = sumPrices(orderItemDTO);
        savedOrder.setOrder_price(total);
        orderRepository.updateOrderPrice(savedOrder);

        TableStatusUpdateResponseDTO tableStatusUpdateResponseDTO =
                tableService.updateTableStatusIntoWaitingForOrder(
                        savedOrder.getTable_id()
                );

        return objectMapper.toAddOrderResponse(
                savedOrder,
                tableStatusUpdateResponseDTO,
                orderItemDTO);
    }

    public void isThereEnoughItems(AddOrderRequestDTO addOrderRequestDTO) {
        if (!itemService.areThereEnoughItems(addOrderRequestDTO.getItems())) {
            throw new IllegalStateException("Not enough items in stock");
        }
    }

    public Order buildNewOrder(RestaurantTable restaurantTable) {
        return new Order(
                restaurantTable.getId(),
                false,
                LocalDateTime.now(clock),
                new BigDecimal(0),
                restaurantTable.getWaitress_id(),
                false
        );
    }

    public static BigDecimal sumPrices(List<OrderItemDTO> orderItemDTO) {
        return orderItemDTO.stream().map(OrderItemDTO::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void markOrderAsComplete(int orderId) {
        boolean orderStatus = orderRepository.getOrderStatusById(orderId);
        if (orderStatus) {
            throw new ConflictException("Order is already ready");
        }
        orderRepository.updateOrderStatusIntoReady(orderId);
    }

    public void checkIfOrderOrOrderItemIsReady(int orderItemId) {
        OrderStatusUpdateDTO orderStatusUpdateDTO = orderItemService.checkIfAllRelatedOrderItemsAreReady(orderItemId);
        if (orderStatusUpdateDTO.isOrderItemsStatuses()) {
            markOrderAsComplete(orderStatusUpdateDTO.getOrderId());
        }
    }
}
