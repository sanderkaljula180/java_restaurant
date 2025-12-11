package org.example.services;

import org.example.database.OrderRepository;
import org.example.database.TablesRepository;
import org.example.database.WaitressRepository;
import org.example.dto.OccupyTableRequestDTO;
import org.example.dto.OccupyTableResponseDTO;
import org.example.dto.TableDTO;
import org.example.dto.TableSetupDTO;
import org.example.entities.Order;
import org.example.entities.RestaurantTable;
import org.example.entities.Waitress;
import org.example.exceptions.ConflictException;
import org.example.exceptions.ResourceNotAvailable;
import org.example.helpers.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableService {

    private static final Logger log = LoggerFactory.getLogger(TableService.class);
    private final TablesRepository tablesRepository;
    private final WaitressRepository waitressRepository;
    private final OrderRepository orderRepository;

    public TableService(TablesRepository tablesRepository, WaitressRepository waitressRepository, OrderRepository orderRepository) {
        this.tablesRepository = tablesRepository;
        this.waitressRepository = waitressRepository;
        this.orderRepository = orderRepository;
    }

    public List<TableDTO> getAllTables() throws SQLException {
        List<RestaurantTable> restaurantTableList = tablesRepository.getAllTables();
        List<TableDTO> tableDTOS = new ArrayList<>();
        for (RestaurantTable table : restaurantTableList) {
            tableDTOS.add(Mapper.toTableDto(
                    table,
                    waitressRepository.findWaitressById(table.getWaitress_id()),
                    orderRepository.findOrdersByRestaurantTableId(table.getId())
            ));
        }
        return tableDTOS;
    }

    // Just in case I have to validate also if that table is occupied or not.
    // Also custom Exception for if table is not actually available.Also waitress
    // Then I will call that custom exception and catch it in controller layer and
    // call a new method that sends back correct OutputStream response
    public TableSetupDTO getTableForSetup(int tableId) throws SQLException {
        RestaurantTable restaurantTable = tablesRepository.findTableByTableId(tableId);
        if (restaurantTable == null) {
            System.out.println("This table is not available. Try again.");
        }

        List<Waitress> waitresses = waitressRepository.findAllAvailableWaitresses();
        if (waitresses.isEmpty()) {
            System.out.println("No available waitresses currently.");
        }

        return Mapper.toTableSetupDto(
                restaurantTable,
                waitresses
        );
    }

    public OccupyTableResponseDTO occupyTable(OccupyTableRequestDTO occupyTableRequestDTO) throws SQLException {
        RestaurantTable restaurantTable = tablesRepository.findTableByTableId(occupyTableRequestDTO.getTableId());
        if (restaurantTable.isOccupied()) {
            throw new ResourceNotAvailable("Table " + restaurantTable.getId() + " is not available");
        }

        if (restaurantTable.getTable_capacity() < occupyTableRequestDTO.getNumberOfQuests()) {
            throw new ResourceNotAvailable("This table doesn’t hold that many quests. It only holds " + restaurantTable.getTable_capacity());
        }

        if (!restaurantTable.getStatus().equals("FREE")) {
            throw new ConflictException("Table status field must be FREE, but the database contains " + restaurantTable.getStatus());
        }

        Waitress waitress = waitressRepository.findWaitressById(occupyTableRequestDTO.getWaitressId());
        if (!waitress.isAvailable()) {
            throw new ResourceNotAvailable("Waitress " + waitress.getName() + " is not available");
        }

        restaurantTable.setOccupied(true);
        restaurantTable.setNumber_of_guests(occupyTableRequestDTO.getNumberOfQuests());
        restaurantTable.setWaitress_id(waitress.getId());
        restaurantTable.setStatus("CHOOSING_ITEMS");
        waitress.setAvailable(false);

        restaurantTable = tablesRepository.updateTableRestaurantTables(restaurantTable);

        System.out.println(restaurantTable);

        return null;
    }

}
