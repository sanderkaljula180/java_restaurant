package org.example.services;

import org.example.database.OrderRepository;
import org.example.dto.AddOrderRequestDTO;
import org.example.dto.AddOrderResponseDTO;
import org.example.dto.OrderItemDTO;
import org.example.entities.Order;
import org.example.entities.RestaurantTable;
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

    public AddOrderResponseDTO addNewOrder(AddOrderRequestDTO addOrderRequestDTO) throws SQLException {
        /** New Order object
         * FIRST THING IS ALSO VALIDATE THAT THERE IS ENOUG ITEMS LEFT
         * I have to create Order object first becuase OrderItem needs order ID and then I will just update the order if needed.
         *
         * Add tableid
         * add time
         * add order price
         * add waitress from table object
         * Validate that it is the correct table
         * Validate that it is the correct waitress
         * Change table status into WAITING_FOR_ORDER
         *
         *
         *
         * New OrderItem objects
         * Add order id
         * Add price
         * Add quantity
         * Add item id
         *
         * Create Item object
         * Remove Item stock quantity
         *
         *
         * No i gotta go from top to bottom
         * **/
        System.out.println("HERE");
        if (itemService.areThereEnoughItems(addOrderRequestDTO.getItems())) {
            System.out.println("SEE ON TRUE PRAEGU");
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
        List<OrderItemDTO> orderItemDTOList = orderItemService.createOrderItems(addOrderRequestDTO.getItems(), newOrder.getId());




        return null;
    }
}
