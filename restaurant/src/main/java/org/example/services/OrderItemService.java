package org.example.services;

import org.example.database.OrderItemRepository;
import org.example.dto.ItemDTO;
import org.example.dto.OrderItemDTO;
import org.example.dto.OrderItemsForOrderRequestDTO;
import org.example.entities.OrderItem;

import java.math.BigDecimal;
import java.sql.SQLException;
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
    public List<OrderItemDTO> createOrderItems(List<OrderItemsForOrderRequestDTO> orderItems, int orderId) {
        List<OrderItem> orderItemsListForDB = orderItems.stream()
                .map(item -> new OrderItem(
                        orderId,
                        item.getItemId(),
                        itemService.reduceItemQuantity(item.getQuantity(), item.getItemId()),
                        orderItemPrice(item.getQuantity(), item.getItemId()),
                        false
                )).toList();

        orderItemRepository.insertNewOrderItems(orderItemsListForDB);
        return null;
    }

    private BigDecimal orderItemPrice(int quantity, int itemId) {
        ItemDTO itemDTO = itemService.findItemById(itemId);
        BigDecimal itemPrice = itemDTO.getItemPrice();
        return itemPrice.multiply(BigDecimal.valueOf(quantity));
    }

}
