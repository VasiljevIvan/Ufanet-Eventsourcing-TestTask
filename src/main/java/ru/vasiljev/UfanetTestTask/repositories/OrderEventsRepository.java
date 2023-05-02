package ru.vasiljev.UfanetTestTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vasiljev.UfanetTestTask.models.OrderId;
import ru.vasiljev.UfanetTestTask.models.events.OrderEvent;

import java.util.List;

public interface OrderEventsRepository extends JpaRepository<OrderEvent, Integer> {
    List<OrderEvent> getOrderEventsByOrderId(OrderId orderId);
}
