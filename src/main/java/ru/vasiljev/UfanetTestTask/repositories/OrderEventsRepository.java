package ru.vasiljev.UfanetTestTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vasiljev.UfanetTestTask.models.events.OrderEvent;

import java.util.List;

public interface OrderEventsRepository extends JpaRepository<OrderEvent, Integer> {
    @Query(value = "select nextval('order_id_seq')", nativeQuery = true)
    int nextOrderId();

    List<OrderEvent> findOrderEventsByOrderIdOrderByCreatedAt(int id);
}
