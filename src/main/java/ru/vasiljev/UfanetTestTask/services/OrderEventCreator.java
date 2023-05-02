package ru.vasiljev.UfanetTestTask.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vasiljev.UfanetTestTask.dto.OrderEventDTO;
import ru.vasiljev.UfanetTestTask.models.Customer;
import ru.vasiljev.UfanetTestTask.models.Employee;
import ru.vasiljev.UfanetTestTask.models.Product;
import ru.vasiljev.UfanetTestTask.models.events.*;
import ru.vasiljev.UfanetTestTask.repositories.OrderEventsRepository;
import ru.vasiljev.UfanetTestTask.repositories.ProductsRepository;

import java.time.LocalDateTime;

import static ru.vasiljev.UfanetTestTask.Constants.*;

@Service
@Transactional(readOnly = true)
public class OrderEventCreator {
    private final OrderEventsRepository orderEventsRepository;
    private final ProductsRepository productsRepository;

    public OrderEventCreator(OrderEventsRepository orderEventsRepository, ProductsRepository productsRepository) {
        this.orderEventsRepository = orderEventsRepository;
        this.productsRepository = productsRepository;
    }

    @Transactional
    public RegisterOrderEvent createRegisterOrderEvent(OrderEventDTO orderEventDTO) {
        checkRequiredRegisterParams(orderEventDTO);
        LocalDateTime createdAt = LocalDateTime.now();
        int orderId = orderEventsRepository.nextOrderId();
        Employee employee = new Employee(orderEventDTO.getEmployeeId());
        Customer customer = new Customer(orderEventDTO.getCustomerId());
        Product product = productsRepository.findById(orderEventDTO.getProductId())
                .orElseThrow(()->(new RuntimeException("Продукт с таким id не найден")));
        LocalDateTime expectedCompletionTime = calculateExpectedCompletionTime(createdAt, product);
        return RegisterOrderEvent.builder()
                .createdAt(createdAt)
                .orderId(orderId)
                .employee(employee)
                .customer(customer)
                .product(product)
                .productPrice(product.getPrice())
                .expectedCompletionTime(expectedCompletionTime).build();
    }

    public CancelOrderEvent createCancelOrderEvent(OrderEventDTO orderEventDTO) {
        checkRequiredParameters(orderEventDTO);
        String cancelReason = orderEventDTO.getCancelReason();
        if (cancelReason == null)
            throw new RuntimeException("Не указан обязательный параметр");
        Employee employee = new Employee(orderEventDTO.getEmployeeId());
        int orderId = orderEventDTO.getOrderId();
        return CancelOrderEvent.builder()
                .orderId(orderId)
                .employee(employee)
                .cancelReason(orderEventDTO.getCancelReason())
                .createdAt(LocalDateTime.now()).build();
    }

    public TakenToWorkOrderEvent createTakenToWorkOrderEvent(OrderEventDTO orderEventDTO) {
        checkRequiredParameters(orderEventDTO);
        Employee employee = new Employee(orderEventDTO.getEmployeeId());
        int orderId = orderEventDTO.getOrderId();
        return TakenToWorkOrderEvent.builder()
                .orderId(orderId)
                .employee(employee)
                .createdAt(LocalDateTime.now()).build();
    }

    public ReadyOrderEvent createReadyOrderEvent(OrderEventDTO orderEventDTO) {
        checkRequiredParameters(orderEventDTO);
        Employee employee = new Employee(orderEventDTO.getEmployeeId());
        int orderId = orderEventDTO.getOrderId();
        return ReadyOrderEvent.builder()
                .orderId(orderId)
                .employee(employee)
                .createdAt(LocalDateTime.now()).build();
    }

    public IssuedOrderEvent createIssuedOrderEvent(OrderEventDTO orderEventDTO) {
        checkRequiredParameters(orderEventDTO);
        Employee employee = new Employee(orderEventDTO.getEmployeeId());
        int orderId = orderEventDTO.getOrderId();
        return IssuedOrderEvent.builder()
                .orderId(orderId)
                .employee(employee)
                .createdAt(LocalDateTime.now()).build();
    }

    private void checkRequiredParameters(OrderEventDTO orderEventDTO) {
        Integer orderId = orderEventDTO.getOrderId();
        Integer employeeId = orderEventDTO.getEmployeeId();
        if (orderId == null || employeeId == null)
            throw new RuntimeException("Не указан обязательный параметр");
    }


    private void checkRequiredRegisterParams(OrderEventDTO orderEventDTO) {
        Integer employeeId = orderEventDTO.getEmployeeId();
        Integer customerId = orderEventDTO.getCustomerId();
        Integer productId = orderEventDTO.getProductId();
        if (employeeId == null || customerId == null || productId == null)
            throw new RuntimeException("Не указан обязательный параметр");
    }

    private LocalDateTime calculateExpectedCompletionTime(LocalDateTime createdAt, Product product) {
        return createdAt.plusMinutes(product.getMinutesToPrepare());
    }
}