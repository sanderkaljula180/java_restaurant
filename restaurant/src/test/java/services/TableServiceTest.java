package services;

import org.example.database.OrderRepository;
import org.example.database.TablesRepository;
import org.example.database.WaitressRepository;
import org.example.entities.Order;
import org.example.entities.RestaurantTable;
import org.example.entities.Waitress;
import org.example.helpers.Mapper;
import org.example.services.TableService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import utility.ObjectFactory;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TableServiceTest {
    @Mock
    private TablesRepository tablesRepository;

    @Mock
    private WaitressRepository waitressRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private TableService tableService;

    @Test
    public void getAllTables_correctTableTDOListCreatedWithOneOrder() {

        // Arrange
        List<RestaurantTable> restaurantTableList = ObjectFactory.createListOfRestaurantTablesWithData(4);
        List<Waitress> waitress = restaurantTableList.stream()
                .map(table -> ObjectFactory.createAvailableWaitressObject(
                        table.getWaitress_id(),
                        "Bob",
                        true
                ))
                .toList();

        // When



        // Act


        // Assert

    }

    @Test
    public void getAllTables_correctTableTDOListCreatedWithManyOrders() {

    }
}
