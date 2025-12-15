package org.example.services;

import org.example.dto.AddOrderRequestDTO;
import org.example.dto.AddOrderResponseDTO;
import org.example.entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(TableService.class);
    private final TableService tableService;

    public OrderService(TableService tableService) {
        this.tableService = tableService;
    }

    public AddOrderResponseDTO addNewOrder(AddOrderRequestDTO addOrderRequestDTO) throws SQLException {
        /** New Order object
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
         * **/

//        Order newOrder = new Order(
//                tableService.getTableById(addOrderRequestDTO.getTableId()),
//                false,
//                LocalDateTime.now(),
//
//        );




        return null;
    }
}
