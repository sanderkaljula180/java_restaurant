package org.example.services;

import org.example.database.ItemsRepository;
import org.example.dto.ItemDTO;
import org.example.dto.OrderItemsForOrderRequestDTO;
import org.example.helpers.Mapper;

import java.util.List;
import java.util.Map;

public class ItemService {

    private final ItemsRepository itemsRepository;

    public ItemService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    public ItemDTO findItemById(int itemId) {
        return Mapper.itemDTO(itemsRepository.findItemById(itemId));
    }


    public boolean areThereEnoughItems(List<OrderItemsForOrderRequestDTO> items) {
        List<Integer> itemIds = items.stream().map(OrderItemsForOrderRequestDTO::getItemId).toList();
        Map<Integer, Integer> itemQuantitiesById = itemsRepository.findItemQuantitiesByIds(itemIds);
        return true;
    }

    public void removeItemQuantity() {

    }

}
