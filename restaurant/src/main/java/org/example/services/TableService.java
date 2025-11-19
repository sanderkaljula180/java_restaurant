package org.example.services;

import org.example.database.OrderRepository;
import org.example.database.TablesRepository;
import org.example.database.WaitressRepository;
import org.example.dto.TableDTO;
import org.example.entities.RestaurantTable;
import org.example.helpers.Mapper;

import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableService {

    private final TablesRepository tablesRepository;
    private final WaitressRepository waitressRepository;
    private final OrderRepository orderRepository;
    private final Mapper mapper;

    public TableService(TablesRepository tablesRepository, WaitressRepository waitressRepository, OrderRepository orderRepository, Mapper mapper) {
        this.tablesRepository = tablesRepository;
        this.waitressRepository = waitressRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    public List<TableDTO> getAllTables() throws SQLException {
        List<RestaurantTable> restaurantTableList = tablesRepository.getAllTables();
        List<TableDTO> tableDTOList = restaurantTableList.stream()
                .map(table -> {
                    try {
                        return mapper.toTableTdo(
                                table,
                                waitressRepository.findWaitressById(table.getWaitress_id()),
                                orderRepository.findOrderByRestaurantTableId(table.getId())
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(toList());
        return tableDTOList;
    }
}
