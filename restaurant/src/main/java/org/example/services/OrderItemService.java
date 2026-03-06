package org.example.services;

import org.example.database.OrderItemRepository;
import org.example.dto.*;
import org.example.entities.OrderItem;
import org.example.exceptions.ConflictException;
import org.example.helpers.Mapper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ItemService itemService;

    public OrderItemService(OrderItemRepository orderItemRepository, ItemService itemService) {
        this.orderItemRepository = orderItemRepository;
        this.itemService = itemService;
    }

    // I have to send List of OrderItem entities into Repo for multiple VALUES insert.
    // This logic that is down here is almost correct. But I have to calculate price
    // Add transactiol db
    public List<OrderItemDTO> createOrderItems(List<OrderItemsForOrderRequestDTO> orderItems, int orderId) {
        reduceItemsQuantity(orderItems);

        List<OrderItem> orderItemsList = buildOrderItemsList(orderItems, orderId);
        orderItemRepository.insertNewOrderItems(orderItemsList);

        return orderItemsList.stream().map(item -> new OrderItemDTO(
                item.getOrder_id(),
                item.getItem_id(),
                item.getQuantity(),
                item.getPrice(),
                item.isReady()
        )).toList();
    }

    public List<OrderItemsForKitchenDTO> getAllOrderItems() {
        return orderItemRepository.getAllOrderItemsForKitchen();
    }

    private List<OrderItem> buildOrderItemsList(List<OrderItemsForOrderRequestDTO> orderItems, int orderId) {
        return orderItems.stream()
                .map(item -> new OrderItem(
                        orderId,
                        item.getItemId(),
                        item.getQuantity(),
                        orderItemPrice(item.getQuantity(), item.getItemId()),
                        false
                )).toList();
    }

    private void reduceItemsQuantity(List<OrderItemsForOrderRequestDTO> orderItems) {
        for (var i : orderItems) {
            itemService.reduceItemQuantity(i.getQuantity(), i.getItemId());
        }
    }

    private BigDecimal orderItemPrice(int quantity, int itemId) {
        ItemDTO itemDTO = itemService.findItemById(itemId);
        BigDecimal itemPrice = itemDTO.getItemPrice();
        return itemPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public OrderStatusUpdateDTO checkIfAllRelatedOrderItemsAreReady(int orderItemId) {
        boolean orderItemStatus = orderItemRepository.getOrderItemStatusById(orderItemId);
        if (orderItemStatus) {
            throw new ConflictException("Order item is already ready");
        }
        orderItemRepository.updateOrderItemIntoReady(orderItemId);
        return orderItemRepository.areAllRelatedOrderItemsReady(orderItemId);
    }

}
