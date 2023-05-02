package ru.vasiljev.UfanetTestTask.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vasiljev.UfanetTestTask.models.Order;
import ru.vasiljev.UfanetTestTask.models.Event;
import ru.vasiljev.UfanetTestTask.models.events.OrderEvent;
import ru.vasiljev.UfanetTestTask.models.events.RegisterOrderEvent;
import ru.vasiljev.UfanetTestTask.repositories.CustomersRepository;
import ru.vasiljev.UfanetTestTask.repositories.EmployeesRepository;
import ru.vasiljev.UfanetTestTask.repositories.OrderEventsRepository;
import ru.vasiljev.UfanetTestTask.repositories.OrderIdsRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.vasiljev.UfanetTestTask.Constants.*;

@Service
public class OrderEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventHandler.class);
    private final OrderIdsRepository orderIdsRepository;
    private final EmployeesRepository employeesRepository;
    private final CustomersRepository customersRepository;
    private final OrderEventsRepository orderEventsRepository;

    public OrderEventHandler(OrderIdsRepository orderIdsRepository, EmployeesRepository employeesRepository, CustomersRepository customersRepository,
                             OrderEventsRepository orderEventsRepository) {
        this.orderIdsRepository = orderIdsRepository;
        this.employeesRepository = employeesRepository;
        this.customersRepository = customersRepository;
        this.orderEventsRepository = orderEventsRepository;
    }

    public void handleOrderEvent(OrderEvent event) {
        checkEmployeeExists(event.getEmployee().getId());
        if (event.getEventType().equals(REGISTER)) {
            checkCustomerExists(((RegisterOrderEvent) event).getCustomer().getId());
            orderIdsRepository.save(event.getOrderId());
            saveOrderEvent(event);
        } else {
            checkOrderExists(event.getOrderId().getVal());
            String status = getOrderStatus(event.getOrderId().getVal());
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
                    else throw new RuntimeException("Заказ должен иметь статус 'Зарегистрирован'");
                }
                case ISSUED -> {
                    if (status.equals(READY))
                        saveOrderEvent(event);
                    else throw new RuntimeException("Заказ должен иметь статус 'Зарегистрирован'");
                }
            }
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
        if (orderIdsRepository.findById(id).isEmpty())
            throw new RuntimeException("Заказ с таким id не найден");
    }

    @Transactional
    public void saveOrderEvent(OrderEvent event) {
        orderEventsRepository.save(event);
        switch (event.getEventType()) {
            case REGISTER -> logger.info(String.format("Заказ №%d создан", event.getOrderId().getVal()));
            case CANCEL -> logger.info(String.format("Заказ №%d отменен", event.getOrderId().getVal()));
            case TAKEN_TO_WORK -> logger.info(String.format("Заказ №%d взят в работу", event.getOrderId().getVal()));
            case READY -> logger.info(String.format("Заказ №%d готов", event.getOrderId().getVal()));
            case ISSUED -> logger.info(String.format("Заказ №%d выдан", event.getOrderId().getVal()));
        }
    }

    public String getOrderStatus(int id) {
        List<OrderEvent> orderEvents = orderEventsRepository
                .getOrderEventsByOrderId(orderIdsRepository.findById(id).orElse(null));
        orderEvents.sort(Comparator.comparing(OrderEvent::getCreatedAt));
        return orderEvents.get(orderEvents.size() - 1).getEventType();
    }

    public Order recreateOrder(int id) {
        List<Event> events = new ArrayList<>();
        List<OrderEvent> orderEvents = orderEventsRepository
                .getOrderEventsByOrderId(orderIdsRepository.findById(id).orElse(null));
        orderEvents.sort(Comparator.comparing(OrderEvent::getCreatedAt));
        String status = orderEvents.get(orderEvents.size() - 1).getEventType();
        for (OrderEvent orderEvent : orderEvents)
            events.add(orderEventToEvent(orderEvent));
        return Order.builder()
                .id(id)
                .status(status)
                .events(events).build();
    }

    public Event orderEventToEvent(OrderEvent orderEvent) {
        return new Event(orderEvent.getCreatedAt(), orderEvent.getEventType());
    }
}
