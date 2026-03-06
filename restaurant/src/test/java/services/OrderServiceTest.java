package services;

import org.example.database.OrderRepository;
import org.example.dto.*;
import org.example.entities.Order;
import org.example.entities.RestaurantTable;
import org.example.exceptions.ConflictException;
import org.example.helpers.ObjectMapper;
import org.example.services.ItemService;
import org.example.services.OrderItemService;
import org.example.services.OrderService;
import org.example.services.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private TableService tableService;
    @Mock private OrderItemService orderItemService;
    @Mock private OrderRepository orderRepository;
    @Mock private ItemService itemService;
    @Mock private ObjectMapper objectMapper;

    private Clock clock;
    private OrderService orderService;

    private static final Instant FIXED_INSTANT = Instant.parse("2025-01-15T10:00:00Z");
    private static final ZoneId ZONE = ZoneId.of("UTC");

    private final RestaurantTable TABLE = new RestaurantTable(1, 5, true, 3, 4, 99, "WAITING_FOR_ORDER");
    private final Order SAVED_ORDER = new Order(1, 1, false, LocalDateTime.parse("2025-01-15T10:00:00"), BigDecimal.ZERO, 99, false);
    private final List<OrderItemDTO> ORDER_ITEMS = List.of(
            new OrderItemDTO(1, 10, 2, new BigDecimal("12.50"), false),
            new OrderItemDTO(1, 11, 1, new BigDecimal("7.50"), false)
    );
    private final TableStatusUpdateResponseDTO TABLE_STATUS_RESPONSE =
            new TableStatusUpdateResponseDTO(1, "READY_FOR_ORDER", "WAITING_FOR_ORDER", "Status updated");
    private final AddOrderResponseDTO EXPECTED_RESPONSE =
            new AddOrderResponseDTO("Order created", "WAITING_FOR_ORDER", ORDER_ITEMS, new BigDecimal("20.00"));
    private final List<OrderItemsForOrderRequestDTO> REQUESTED_ITEMS = List.of(
            new OrderItemsForOrderRequestDTO(10, 2),
            new OrderItemsForOrderRequestDTO(11, 1)
    );

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(FIXED_INSTANT, ZONE);
        orderService = new OrderService(
                tableService, orderItemService, orderRepository,
                itemService, objectMapper, clock
        );
    }

    @Test
    void addNewOrder_shouldReturnResponse_whenAllDependenciesSucceed() {
        // ARRANGE
        AddOrderRequestDTO request = new AddOrderRequestDTO(1, REQUESTED_ITEMS);
        when(itemService.areThereEnoughItems(REQUESTED_ITEMS)).thenReturn(true);
        when(tableService.findTableById(1)).thenReturn(TABLE);
        when(orderRepository.insertNewOrderAndReturnOrder(any(Order.class))).thenReturn(SAVED_ORDER);
        when(orderItemService.createOrderItems(REQUESTED_ITEMS, SAVED_ORDER.getId())).thenReturn(ORDER_ITEMS);
        when(tableService.updateTableStatusIntoWaitingForOrder(SAVED_ORDER.getTable_id())).thenReturn(TABLE_STATUS_RESPONSE);
        when(objectMapper.toAddOrderResponse(SAVED_ORDER, TABLE_STATUS_RESPONSE, ORDER_ITEMS)).thenReturn(EXPECTED_RESPONSE);

        // ACT
        AddOrderResponseDTO result = orderService.addNewOrder(request);

        // ASSERT
        assertThat(result).isEqualTo(EXPECTED_RESPONSE);
        assertThat(SAVED_ORDER.getOrder_price()).isEqualByComparingTo("20.00");
        verify(orderRepository).updateOrderPrice(SAVED_ORDER);
        verify(tableService).updateTableStatusIntoWaitingForOrder(1);
        verify(objectMapper).toAddOrderResponse(SAVED_ORDER, TABLE_STATUS_RESPONSE, ORDER_ITEMS);
    }

    @Test
    void addNewOrder_shouldThrow_andCallNothingElse_whenNotEnoughItems() {
        // ARRANGE
        AddOrderRequestDTO request = new AddOrderRequestDTO(1, REQUESTED_ITEMS);
        when(itemService.areThereEnoughItems(REQUESTED_ITEMS)).thenReturn(false);

        // ACT + ASSERT
        assertThatThrownBy(() -> orderService.addNewOrder(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Not enough items in stock");
        verifyNoInteractions(tableService, orderRepository, orderItemService, objectMapper);
    }

    @Test
    void isThereEnoughItems_shouldThrow_whenItemServiceReturnsFalse() {
        // ARRANGE
        AddOrderRequestDTO request = new AddOrderRequestDTO(1, REQUESTED_ITEMS);
        when(itemService.areThereEnoughItems(REQUESTED_ITEMS)).thenReturn(false);

        // ACT + ASSERT
        assertThatThrownBy(() -> orderService.isThereEnoughItems(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Not enough items in stock");
    }

    @Test
    void isThereEnoughItems_shouldNotThrow_whenItemServiceReturnsTrue() {
        // ARRANGE
        AddOrderRequestDTO request = new AddOrderRequestDTO(1, REQUESTED_ITEMS);
        when(itemService.areThereEnoughItems(REQUESTED_ITEMS)).thenReturn(true);

        // ACT + ASSERT
        assertThatNoException().isThrownBy(() -> orderService.isThereEnoughItems(request));
    }

    @Test
    void buildNewOrder_shouldBuildOrderWithCorrectFieldsFromTable() {
        // ARRANGE
        RestaurantTable table = new RestaurantTable(3, 5, true, 2, 4, 7, "OCCUPIED");
        LocalDateTime expectedTime = LocalDateTime.now(clock);

        // ACT
        Order result = orderService.buildNewOrder(table);

        // ASSERT
        assertThat(result.getTable_id()).isEqualTo(3);
        assertThat(result.getWaitress_id()).isEqualTo(7);
        assertThat(result.getOrder_time()).isEqualTo(expectedTime);
        assertThat(result.getOrder_price()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.isPaid()).isFalse();
        assertThat(result.isReady()).isFalse();
    }

    @Test
    void sumPrices_shouldReturnCorrectSum_whenListHasMultipleItems() {
        // ARRANGE
        List<OrderItemDTO> items = List.of(
                new OrderItemDTO(1, 10, 2, new BigDecimal("10.00"), false),
                new OrderItemDTO(1, 11, 1, new BigDecimal("5.50"), false),
                new OrderItemDTO(1, 12, 3, new BigDecimal("3.25"), false)
        );

        // ACT
        BigDecimal result = OrderService.sumPrices(items);

        // ASSERT
        assertThat(result).isEqualByComparingTo("18.75");
    }

    @Test
    void sumPrices_shouldReturnZero_whenListIsEmpty() {
        // ACT
        BigDecimal result = OrderService.sumPrices(List.of());

        // ASSERT
        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void sumPrices_shouldReturnSingleItemPrice_whenListHasOneItem() {
        // ARRANGE
        List<OrderItemDTO> items = List.of(
                new OrderItemDTO(1, 10, 1, new BigDecimal("9.99"), false)
        );

        // ACT
        BigDecimal result = OrderService.sumPrices(items);

        // ASSERT
        assertThat(result).isEqualByComparingTo("9.99");
    }

    @Test
    void markOrderAsComplete_shouldUpdateStatus_whenOrderIsNotYetReady() {
        // ARRANGE
        when(orderRepository.getOrderStatusById(5)).thenReturn(false);

        // ACT
        orderService.markOrderAsComplete(5);

        // ASSERT
        verify(orderRepository).updateOrderStatusIntoReady(5);
    }

    @Test
    void markOrderAsComplete_shouldThrowConflictException_whenOrderIsAlreadyReady() {
        // ARRANGE
        when(orderRepository.getOrderStatusById(5)).thenReturn(true);

        // ACT + ASSERT
        assertThatThrownBy(() -> orderService.markOrderAsComplete(5))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Order is already ready");
        verify(orderRepository, never()).updateOrderStatusIntoReady(anyInt());
    }

    @Test
    void checkIfOrderOrOrderItemIsReady_shouldMarkOrderComplete_whenAllItemsAreReady() {
        // ARRANGE
        OrderStatusUpdateDTO dto = new OrderStatusUpdateDTO(99, true);
        when(orderItemService.checkIfAllRelatedOrderItemsAreReady(1)).thenReturn(dto);
        when(orderRepository.getOrderStatusById(99)).thenReturn(false);

        // ACT
        orderService.checkIfOrderOrOrderItemIsReady(1);

        // ASSERT
        verify(orderRepository).updateOrderStatusIntoReady(99);
    }

    @Test
    void checkIfOrderOrOrderItemIsReady_shouldNotMarkOrderComplete_whenSomeItemsStillPending() {
        // ARRANGE
        OrderStatusUpdateDTO dto = new OrderStatusUpdateDTO(99, false);
        when(orderItemService.checkIfAllRelatedOrderItemsAreReady(1)).thenReturn(dto);

        // ACT
        orderService.checkIfOrderOrOrderItemIsReady(1);

        // ASSERT
        verifyNoInteractions(orderRepository);
    }

    @Test
    void checkIfOrderOrOrderItemIsReady_shouldThrowConflict_whenAllItemsReadyButOrderAlreadyComplete() {
        // ARRANGE
        OrderStatusUpdateDTO dto = new OrderStatusUpdateDTO(99, true);
        when(orderItemService.checkIfAllRelatedOrderItemsAreReady(1)).thenReturn(dto);
        when(orderRepository.getOrderStatusById(99)).thenReturn(true);

        // ACT + ASSERT
        assertThatThrownBy(() -> orderService.checkIfOrderOrOrderItemIsReady(1))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Order is already ready");
        verify(orderRepository, never()).updateOrderStatusIntoReady(anyInt());
    }
}