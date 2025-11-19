package api;


import org.example.api.TablesController;
import org.example.database.OrderRepository;
import org.example.database.TablesRepository;
import org.example.database.WaitressRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * How to test? I always forget, this comment is for future references.
 *
 * '@Mock' is for creating mock dependency and '@InjectMocks' is for actual Object that we test
 *
 * Arrange:
 *
 * Act:
 *
 * Assert:
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TablesControllerTest {

    @Mock
    private TablesRepository tablesRepository;

    @Mock
    private WaitressRepository waitressRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TablesController tablesController;


    @Test
    void getAllTablesWhenCorrectTableTDOListReturned_thenConvertTDOIntoJsonBytes() {

        // Arrange

        // Act

        // Assert

    }



}
