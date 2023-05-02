package ru.vasiljev.UfanetTestTask.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vasiljev.UfanetTestTask.dto.Order;
import ru.vasiljev.UfanetTestTask.models.events.OrderEvent;
import ru.vasiljev.UfanetTestTask.models.events.RegisterOrderEvent;
import ru.vasiljev.UfanetTestTask.repositories.CustomersRepository;
import ru.vasiljev.UfanetTestTask.repositories.EmployeesRepository;
import ru.vasiljev.UfanetTestTask.repositories.OrderEventsRepository;

import java.util.List;

import static ru.vasiljev.UfanetTestTask.Constants.*;

@Slf4j
@Service
public class OrderEventHandler {
    private final EmployeesRepository employeesRepository;
    private final CustomersRepository customersRepository;
    private final OrderEventsRepository orderEventsRepository;

    public OrderEventHandler(EmployeesRepository employeesRepository, CustomersRepository customersRepository,
                             OrderEventsRepository orderEventsRepository) {
        this.employeesRepository = employeesRepository;
        this.customersRepository = customersRepository;
        this.orderEventsRepository = orderEventsRepository;
    }

    public void handleOrderEvent(OrderEvent event) {
        checkEmployeeExists(event.getEmployee().getId());
        if (event.getEventType().equals(REGISTER)) {
            checkCustomerExists(((RegisterOrderEvent) event).getCustomer().getId());
            saveOrderEvent(event);
        } else {
            checkOrderExists(event.getOrderId());
            String status = getOrderStatus(event.getOrderId());
            switch (event.getEventType()) {
                case CANCEL -> {
                    if (!status.equals(CANCEL) && !status.equals(ISSUED))
                        saveOrderEvent(event);
                    else throw new RuntimeException("Заказ уже выдан или отменен");
                }
                case TAKEN_TO_WORK -> {
                    if (status.equals(REGISTER))
                        saveOrderEvent(event);
                    else throw new RuntimeException("Заказ должен иметь статус 'Зарегистрирован'");
                }
                case READY -> {
                    if (status.equals(TAKEN_TO_WORK))
                        saveOrderEvent(event);
                    else throw new RuntimeException("Заказ должен иметь статус 'Взят в работу'");
                }
                case ISSUED -> {
                    if (status.equals(READY))
                        saveOrderEvent(event);
                    else throw new RuntimeException("Заказ должен иметь статус 'Готов к выдаче'");
                }
            }
        }
    }

    @Transactional
    public void saveOrderEvent(OrderEvent event) {
        orderEventsRepository.save(event);
        switch (event.getEventType()) {
            case REGISTER -> log.info(String.format("Заказ №%d создан", event.getOrderId()));
            case CANCEL -> log.info(String.format("Заказ №%d отменен", event.getOrderId()));
            case TAKEN_TO_WORK -> log.info(String.format("Заказ №%d взят в работу", event.getOrderId()));
            case READY -> log.info(String.format("Заказ №%d готов", event.getOrderId()));
            case ISSUED -> log.info(String.format("Заказ №%d выдан", event.getOrderId()));
        }
    }

    public void checkEmployeeExists(int id) {
        if (employeesRepository.findById(id).isEmpty())
            throw new RuntimeException("Сотрудник с таким id не найден");
    }

    public void checkCustomerExists(int id) {
        if (customersRepository.findById(id).isEmpty())
            throw new RuntimeException("Покупатель с таким id не найден");
    }

    public void checkOrderExists(int id) {
        if (getOrderEventsByOrderId(id).isEmpty())
            throw new RuntimeException("Заказ с таким id не найден");
    }

    public List<OrderEvent> getOrderEventsByOrderId(int id) {
        return orderEventsRepository.findOrderEventsByOrderIdOrderByCreatedAt(id);
    }

    public String getOrderStatus(int id) {
        List<OrderEvent> orderEvents = getOrderEventsByOrderId(id);
        return orderEvents.get(orderEvents.size() - 1).getEventType();
    }

    public Order recreateOrder(int id) {
        List<OrderEvent> orderEvents = getOrderEventsByOrderId(id);
        return Order.builder()
                .id(id)
                .status(orderEvents.get(orderEvents.size() - 1).getEventType())
                .events(orderEvents).build();
    }
}
