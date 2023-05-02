package ru.vasiljev.UfanetTestTask.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Order {
    private int id;
    private String status;
    private List<Event> events;
}
