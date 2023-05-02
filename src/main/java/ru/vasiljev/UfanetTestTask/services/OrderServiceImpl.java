package ru.vasiljev.UfanetTestTask.services;

import org.springframework.stereotype.Service;
import ru.vasiljev.UfanetTestTask.dto.Order;
import ru.vasiljev.UfanetTestTask.models.events.OrderEvent;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderEventHandler orderEventHandler;

    public OrderServiceImpl(OrderEventHandler orderEventHandler) {
        this.orderEventHandler = orderEventHandler;
    }

    @Override
    public void publishEvent(OrderEvent event) {
        orderEventHandler.handleOrderEvent(event);
    }

    @Override
    public Order findOrder(int id) {
        return orderEventHandler.recreateOrder(id);
    }
}
