package ru.vasiljev.UfanetTestTask.services;

import ru.vasiljev.UfanetTestTask.dto.Order;
import ru.vasiljev.UfanetTestTask.models.events.OrderEvent;

interface OrderService {

    void publishEvent(OrderEvent event);

    Order findOrder(int id);

}