package ru.vasiljev.UfanetTestTask.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.vasiljev.UfanetTestTask.models.events.OrderEvent;

import java.util.List;

@Builder
@Getter
@Setter
public class Order {
    private int id;
    private String status;
    private List<OrderEvent> events;
}
