package org.example.services;

import org.example.database.OrderRepository;
import org.example.database.TablesRepository;
import org.example.database.WaitressRepository;
import org.example.dto.*;
import org.example.entities.RestaurantTable;
import org.example.entities.Waitress;
import org.example.exceptions.ConflictException;
import org.example.exceptions.ResourceNotAvailable;
import org.example.helpers.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// I SHOULD NOT USE WAITRESS REPOSITORY STRAIGHT FROM HERE. I SHOULD USE WAITRESS SERVICE INSTEAD AND THIS GOES FOR EVERYTHING.
// REFACTOR THAT SHIT LATER
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

    public RestaurantTable getTableById(int tableId) throws SQLException {
        return tablesRepository.findTableByTableId(tableId);
    }

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

        if (!restaurantTable.getStatus().equals("AVAILABLE")) {
            throw new ConflictException("Table status field must be AVAILABLE, but the database contains " + restaurantTable.getStatus());
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

        return Mapper.toOccupyTableResponseDTO(
                tablesRepository.updateRestaurantTableById(restaurantTable),
                waitressRepository.updateTableWaitresses(waitress)
        );
    }

    public TableStatusUpdateResponseDTO updateTableStatusIntoReadyForOrder(int tableId) throws SQLException {
        RestaurantTable restaurantTable = tablesRepository.findTableByTableId(tableId);
        if (restaurantTable.getStatus().equals("READY_FOR_ORDER")) {
            throw new ConflictException("Table has already status READY_FOR_ORDER");
        }

        if (restaurantTable.getStatus().equals("AVAILABLE")) {
            String exceptionMessage = restaurantTable.isOccupied()
                    ? "Table is AVAILABLE but table is also occupied. This can't happen."
                    : "Table is AVAILABLE, so occupy table first.";
            throw new ConflictException(exceptionMessage);
        }

        String oldStatus = restaurantTable.getStatus();
        restaurantTable.setStatus("READY_FOR_ORDER");
        return Mapper.toTableStatusUpdateResponseDTO(
                tablesRepository.updateRestaurantTableStatusById(restaurantTable),
                oldStatus
        );

    }

}
