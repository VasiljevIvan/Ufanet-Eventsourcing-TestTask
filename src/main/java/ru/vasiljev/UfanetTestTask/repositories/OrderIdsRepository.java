package ru.vasiljev.UfanetTestTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vasiljev.UfanetTestTask.models.OrderId;

public interface OrderIdsRepository extends JpaRepository<OrderId, Integer> {

}
